package kreadcn.homework.service.impl;

import kreadcn.homework.dao.*;
import kreadcn.homework.dto.HomeworkDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.*;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.DepartmentService;
import kreadcn.homework.service.HomeworkService;
import kreadcn.homework.service.UserService;
import kreadcn.homework.service.utils.AdminUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.HomeworkDetailVO;
import kreadcn.homework.vo.HomeworkVO;
import kreadcn.homework.vo.UserHomeworkItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkDeptMapper homeworkDeptMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserHomeworkMapper userHomeworkMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    @Value("${uploadPath:}")
    private String uploadPath;

    private void validate(HomeworkDTO homeworkDTO) {
        Assert.notNull(homeworkDTO, "请提供参数");
        Assert.notEmpty(homeworkDTO.getDeptIds(), "请提供班级列表");
        Assert.hasText(homeworkDTO.getName(), "请输入作业名称");
        Assert.hasText(homeworkDTO.getCourseName(), "请输入课程名称");
        for (Integer deptId : homeworkDTO.getDeptIds()) {
            Assert.notNull(departmentMapper.selectByPrimaryKey(deptId), "班级不存在：id=" + deptId);
        }
    }

    @Override
    public Page<HomeworkVO> listByPage(KeywordQueryDTO queryDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.notNull(queryDTO, "请提供查询参数");
        queryDTO.setKeyword(FormatUtils.makeFuzzySearchTerm(queryDTO.getKeyword()));
        if ("root".equalsIgnoreCase(token.getUserCode())) {
            queryDTO.setCreatedBy(null);
        } else {
            queryDTO.setCreatedBy(token.getAdminId());
        }

        Integer size = homeworkMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Homework> list = homeworkMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Homework::getCreatedBy).collect(Collectors.toSet());
        Set<Integer> homeworkIds = list.stream().map(Homework::getId).collect(Collectors.toSet());

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);
        Map<Integer, List<Integer>> homeDeptMap = new HashMap<>();
        List<HomeworkDept> homeDeptList = homeworkDeptMapper.listByHomeworkIds(new ArrayList<>(homeworkIds));
        Set<Integer> deptIds = homeDeptList.stream().map(HomeworkDept::getDepartmentId).collect(Collectors.toSet());
        Map<Integer, String> deptMap = departmentService.getNameMap(deptIds);
        homeDeptList.forEach(item -> {
            List<Integer> ar = homeDeptMap.computeIfAbsent(item.getHomeworkId(), k -> new ArrayList<>());
            ar.add(item.getDepartmentId());
        });


        List<HomeworkVO> voList = list.stream()
                .map(item -> {
                    HomeworkVO vo = new HomeworkVO();
                    BeanUtils.copyProperties(item, vo);
                    vo.setCreatedByDesc(nameMap.get(item.getCreatedBy()));
                    vo.setDeptIds(homeDeptMap.get(item.getId()));
                    vo.setDepartmentNames(new ArrayList<>());
                    if (!CollectionUtils.isEmpty(vo.getDeptIds())) {
                        vo.getDeptIds().forEach(id -> vo.getDepartmentNames().add(deptMap.get(id)));
                    }
                    return vo;
                }).toList();
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    @Transactional
    public Integer add(HomeworkDTO homeworkDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        validate(homeworkDTO);
        Homework homework = new Homework();
        homework.setName(homeworkDTO.getName());
        homework.setCourseName(homeworkDTO.getCourseName());
        homework.setDescription(homeworkDTO.getDescription());
        homework.setFinished(false);
        homework.setCreatedAt(new Date());
        homework.setUpdatedAt(new Date());
        homework.setCreatedBy(token.getAdminId());
        homework.setUpdatedBy(token.getAdminId());
        homeworkMapper.insert(homework);

        for (Integer deptId : homeworkDTO.getDeptIds()) {
            HomeworkDept hd = new HomeworkDept();
            hd.setHomeworkId(homework.getId());
            hd.setDepartmentId(deptId);
            hd.setCreatedBy(token.getAdminId());
            homeworkDeptMapper.insert(hd);
        }

        return homework.getId();
    }

    @Override
    @Transactional
    public Integer update(HomeworkDTO homeworkDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        validate(homeworkDTO);
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkDTO.getId());
        Assert.notNull(homework, "作业不存在，id=" + homeworkDTO.getId());
        Assert.isTrue(homework.getCreatedBy().equals(token.getAdminId()), "您不能修改其他老师的作业");

        homework.setName(homeworkDTO.getName());
        homework.setCourseName(homeworkDTO.getCourseName());
        homework.setDescription(homeworkDTO.getDescription());
        homework.setUpdatedBy(token.getAdminId());
        homework.setFinished(false);
        homeworkMapper.updateByPrimaryKey(homework);
        homeworkDeptMapper.deleteByHomeworkId(homeworkDTO.getId());
        for (Integer deptId : homeworkDTO.getDeptIds()) {
            HomeworkDept hd = new HomeworkDept();
            hd.setHomeworkId(homework.getId());
            hd.setDepartmentId(deptId);
            hd.setCreatedBy(token.getAdminId());
            homeworkDeptMapper.insert(hd);
        }

        return homework.getId();
    }

    @Override
    public int deleteByIds(List<Integer> ids) {
        Integer adminId = AdminUtils.getCurrentAdminId();
        int size = homeworkMapper.deleteByIds(ids, adminId);
        homeworkDeptMapper.deleteByHomeworkIds(ids, adminId);
        return size;
    }

    @Override
    public HomeworkDetailVO getDetail(Integer id) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Homework homework = homeworkMapper.selectByPrimaryKey(id);
        Assert.notNull(homework, "作业不存在，id=" + id);
        Assert.isTrue(homework.getCreatedBy().equals(token.getAdminId()), "您不能查看其他老师的作业");

        HomeworkDetailVO vo = new HomeworkDetailVO();
        BeanUtils.copyProperties(homework, vo);
        List<UserHomework> list = userHomeworkMapper.listByHomeworkId(id);
        Map<Integer, UserHomeworkItem> map = new HashMap<>();
        for (UserHomework userHomework : list) {
            UserHomeworkItem uh = map.computeIfAbsent(userHomework.getCreatedBy(), k -> {
                UserHomeworkItem o = new UserHomeworkItem();
                o.setUserId(k);
                o.setFiles(new ArrayList<>());
                return o;
            });
            uh.getFiles().add(userHomework);
            uh.setCreatedAt(userHomework.getCreatedAt());
        }

        List<HomeworkDept> deptList = homeworkDeptMapper.listByHomeworkIds(Arrays.asList(id));
        Set<Integer> deptIds = deptList.stream().map(HomeworkDept::getDepartmentId).collect(Collectors.toSet());
        Set<Integer> userIds = new HashSet<>(userDepartmentMapper.listByDepartmentIds(new ArrayList<>(deptIds)));
        userIds.addAll(map.keySet());
        Map<Integer, User> nameMap = userService.getNameMap(userIds);

        map.values().forEach(item -> {
            User user = nameMap.get(item.getUserId());
            if (user != null) {
                item.setName(user.getName());
                item.setUserCode(user.getUserCode());
            }
        });

        for (Integer userId : userIds) {
            UserHomeworkItem item = map.get(userId);
            if (item == null) {
                User user = nameMap.get(userId);
                if (user == null) {
                    continue;
                }
                item = new UserHomeworkItem();
                item.setUserId(userId);
                item.setUserCode(user.getUserCode());
                item.setName(user.getName());
                item.setFiles(Collections.emptyList());
                map.put(userId, item);
            }
        }

        vo.setUserList(map.values().stream().toList());
        return vo;
    }

    @Override
    public void downloadZip(Integer homeworkId, OutputStream outputStream) throws IOException {
        Homework homework = homeworkMapper.selectByPrimaryKey(homeworkId);
        Assert.notNull(homework, "作业不存在，id=" + homeworkId);
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.isTrue(homework.getCreatedBy().equals(token.getAdminId()), "您不能下载其他老师的作业");
        List<UserHomework> list = userHomeworkMapper.listByHomeworkId(homeworkId);
        Set<Integer> ids = list.stream().map(UserHomework::getCreatedBy).collect(Collectors.toSet());
        Map<Integer, User> nameMap = userService.getNameMap(ids);
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        for (UserHomework userHomework : list) {
            User user = nameMap.get(userHomework.getCreatedBy());
            if (user == null) {
                continue;
            }

            File file = new File(uploadPath + File.separator + homeworkId + File.separator + userHomework.getId());
            String extension = FormatUtils.getFileExtesion(userHomework.getFileName());
            String filename = String.format("%s_%s_%s%s", user.getUserCode(), user.getName(), sdf.format(userHomework.getCreatedAt()), extension);
            zos.putNextEntry(new ZipEntry(filename));
            FileInputStream fis = new FileInputStream(file);
            byte b[] = new byte[1024];
            int n = 0;
            while ((n = fis.read(b)) != -1) {
                zos.write(b, 0, n);
            }
            zos.flush();
            fis.close();
        }
        //设置解压文件后的注释内容
        zos.flush();
        zos.close();
    }

    @Override
    public void finishHomework(int id) {
        Homework homework = homeworkMapper.selectByPrimaryKey(id);
        Assert.notNull(homework, "作业不存在，id=" + id);
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.isTrue(homework.getCreatedBy().equals(token.getAdminId()), "您不能结束其他老师的作业");
        homework.setFinished(true);
        homeworkMapper.updateByPrimaryKey(homework);
    }
}
