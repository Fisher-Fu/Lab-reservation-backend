package kreadcn.homework.service.impl;

import kreadcn.homework.dao.HomeworkDeptMapper;
import kreadcn.homework.dao.HomeworkMapper;
import kreadcn.homework.dao.UserDepartmentMapper;
import kreadcn.homework.dao.UserHomeworkMapper;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.model.Homework;
import kreadcn.homework.model.UserHomework;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.UserHomeworkService;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.HomeworkVO;
import kreadcn.homework.vo.UserHomeworkDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserHomeworkServiceImpl implements UserHomeworkService {
    @Autowired
    private UserHomeworkMapper userHomeworkMapper;

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkDeptMapper homeworkDeptMapper;

    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    @Autowired
    private AdminService adminService;

    @Value("${uploadPath:}")
    private String uploadPath;

    @Override
    public List<HomeworkVO> listUserHomework() {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.notNull(token.getUserId(), "只有学生可以使用");
        // 先获取学生所属班级列表
        List<Integer> deptIds = userDepartmentMapper.listByUserId(token.getUserId());
        // 根据班级列表获取作业
        List<Integer> homeworkIds = homeworkDeptMapper.listByDeptIds(deptIds);
        if (CollectionUtils.isEmpty(homeworkIds)) {
            return Collections.emptyList();
        }

        Set<Integer> finishedIds = new HashSet<>(userHomeworkMapper.listByUserId(token.getUserId()));
        List<Homework> homeworkList = homeworkMapper.listByIds(homeworkIds);
        Set<Integer> adminIds = homeworkList.stream().map(Homework::getCreatedBy).collect(Collectors.toSet());
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);
        return homeworkList
                .stream()
                .map(item -> {
                    HomeworkVO vo = new HomeworkVO();
                    BeanUtils.copyProperties(item, vo);
                    vo.setFinished(finishedIds.contains(item.getId()));
                    vo.setCreatedByDesc(nameMap.get(item.getCreatedBy()));
                    return vo;
                })
                .toList();
    }

    @Override
    public UserHomeworkDetailVO getDetail(Integer homeworkId) {
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        Assert.notNull(homework, "未找到作业");
        Assert.isTrue(Boolean.FALSE.equals(homework.getFinished()), "作业已经停止提交，请联系老师");
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        UserHomeworkDetailVO vo = new UserHomeworkDetailVO();
        vo.setFiles(userHomeworkMapper.listFiles(token.getUserId(), homeworkId));
        vo.setId(homeworkId);
        vo.setName(homework.getName());
        vo.setCourseName(homework.getCourseName());
        return vo;
    }

    @Override
    @Transactional
    public int uploadFile(Integer homeworkId, String fileName, File file, String ipAddress) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss");
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        Assert.notNull(homework, "未找到作业");
        Assert.isTrue(Boolean.FALSE.equals(homework.getFinished()), "作业已经停止提交，请联系老师");
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        UserHomework userHomework = new UserHomework();
        userHomework.setHomeworkId(homeworkId);
        userHomework.setCreatedBy(token.getUserId());
        userHomework.setFileName(fileName);
        userHomework.setFileSize((int) file.length());
        userHomework.setSha1(FormatUtils.getFileSha1(file, ""));
        userHomework.setCreatedAt(new Date());
        userHomework.setIpAddress(ipAddress);
        userHomeworkMapper.insert(userHomework);
        File destDir = new File(uploadPath + File.separator + homeworkId);
        destDir.mkdir();
        File targetFile = new File(uploadPath + File.separator + homeworkId + File.separator + userHomework.getId());
        Files.move(file.toPath(), targetFile.toPath());
        return userHomework.getId();
    }

    @Override
    @Transactional
    public int deleteFiles(List<Integer> ids) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        int cnt = 0;
        for (Integer id : ids) {
            UserHomework userHomework = userHomeworkMapper.selectByPrimaryKey(id);
            if (userHomework == null || !userHomework.getCreatedBy().equals(token.getUserId())) {
                continue;
            }

            cnt += userHomeworkMapper.deleteByPrimaryKey(id);
            try {
                File targetFile = new File(uploadPath + File.separator + userHomework.getHomeworkId() + File.separator + userHomework.getId());
                targetFile.delete();
            } catch (Exception ex) {
            }
        }
        return cnt;
    }
}
