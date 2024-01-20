package kreadcn.homework.service.impl;

import kreadcn.homework.dao.CourseScheduleMapper;
import kreadcn.homework.dao.LabMapper;
import kreadcn.homework.dao.SeasonMapper;
import kreadcn.homework.dto.CourseScheduleDTO;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.dto.query.CourseScheduleQueryDTO;
import kreadcn.homework.enums.AdminType;
import kreadcn.homework.enums.CourseScheduleStatus;
import kreadcn.homework.model.AllocSchedule;
import kreadcn.homework.model.CourseSchedule;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.CourseScheduleService;
import kreadcn.homework.service.utils.CourseScheduleUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.CourseScheduleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author He.Y
 * @description
 * @date 2023/11/14 14:44
 */
@Service
public class CourseScheduleServiceImpl implements CourseScheduleService {
    @Autowired
    private CourseScheduleMapper courseScheduleMapper;

    @Autowired
    private SeasonMapper seasonMapper;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public Integer addCourseSchedule(CourseScheduleDTO courseScheduleDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //身份信息校验, 本功能只对"教师"开放
        Assert.isTrue(AdminType.TEACHER.getValue().equals(token.getAdminType()), "您没有操作权限!");
        CourseScheduleUtils.validateCourseScheduleForTeacher(courseScheduleDTO);
        CourseSchedule courseSchedule = new CourseSchedule();
        courseSchedule.setCourseName(courseScheduleDTO.getCourseName());
        //位运算, 以按位或代替求和, 默认无值返回0
        courseSchedule.setSection(courseScheduleDTO.getSection().stream()
                .mapToInt(num -> 1 << num)
                .reduce((a, b) -> a | b)
                .orElse(0));
        courseSchedule.setWeekRange(courseScheduleDTO.getWeekRange().stream()
                .mapToInt(num -> 1 << num)
                .reduce((a, b) -> a | b)
                .orElse(0));
        courseSchedule.setWeekDay(courseScheduleDTO.getWeekDay().stream()
                .mapToInt(num -> 1 << num)
                .reduce((a, b) -> a | b)
                .orElse(0));
        courseSchedule.setStudentCount(courseScheduleDTO.getStudentCount());
        courseSchedule.setRoomCount(courseScheduleDTO.getRoomCount());
        courseSchedule.setDescription(courseScheduleDTO.getDescription());
        Assert.notNull(seasonMapper.selectByEnabled(true), "没有可用的学期");
        courseSchedule.setSeasonId(seasonMapper.selectByEnabled(true).getId());
        courseSchedule.setCreatedAt(new Date());
        courseSchedule.setUpdatedAt(new Date());
        courseSchedule.setCreatedBy(token.getAdminId());
        courseSchedule.setUpdatedBy(token.getAdminId());
        //审核状态status, 默认未审核(0), 该字段由"实验员"填写
        courseSchedule.setStatus(CourseScheduleStatus.PENDING.getValue());
        //进行时间冲突检查
        Map<Integer, AllocSchedule> allocMap = CourseScheduleUtils.getAllAllocScheduleMap(courseScheduleMapper.getSchedules(CourseScheduleStatus.PASS.getValue(),
                seasonMapper.selectByEnabled(true).getId()), labMapper.getAllLabs());
        Integer availableLabCount = CourseScheduleUtils.countAvailableLabForTeacher(allocMap, courseScheduleDTO, labMapper.getAllLabs());
        Assert.isTrue(availableLabCount >= courseScheduleDTO.getRoomCount(), "当前时间没有足够的可用实验室");
        BeanUtils.copyProperties(courseScheduleDTO, courseSchedule);
        // 调用DAO方法保存到数据库表
        courseScheduleMapper.insert(courseSchedule);
        return courseSchedule.getId();
    }

    @Override
    public int deleteByIds(List<Integer> courseScheduleIds) {
        Assert.notEmpty(courseScheduleIds, "未能选中预约申请");
        int result = courseScheduleMapper.deleteByIds(courseScheduleIds);
        return result;
    }

    /**
     * 教师更新预约申请数据
     * 教师的申请, 只有在"驳回修改"状态才能进行修改, 否则不允许修改
     *
     * @param courseScheduleDTO 输入对象
     * @return 预约申请编码
     */
    @Override
    public void updateCourseSchedule(CourseScheduleDTO courseScheduleDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //"教师"身份校验
        Assert.isTrue(AdminType.TEACHER.getValue().equals(token.getAdminType()), "您没有操作权限!");
        CourseScheduleUtils.validateCourseScheduleForTeacher(courseScheduleDTO);
        Assert.notNull(courseScheduleDTO.getId(), "预约申请的id不能为空");
        CourseSchedule courseSchedule = courseScheduleMapper.selectByPrimaryKey(courseScheduleDTO.getId());
        Assert.notNull(courseSchedule, "不存在Id为" + courseScheduleDTO.getId() + "的预约申请记录");
        //判断当前是否处于"驳回修改"状态, 状态不符不提供修改权限
        CourseScheduleUtils.validateUpdateCourseSchedule(courseSchedule);
        //检查修改后的时间是否有冲突
        Map<Integer, AllocSchedule> allocMap = CourseScheduleUtils.getAllAllocScheduleMap(courseScheduleMapper.getSchedules(CourseScheduleStatus.PASS.getValue(),
                seasonMapper.selectByEnabled(true).getId()), labMapper.getAllLabs());
        Integer availableLabCount = CourseScheduleUtils.countAvailableLabForTeacher(allocMap, courseScheduleDTO, labMapper.getAllLabs());
        Assert.isTrue(availableLabCount >= courseScheduleDTO.getRoomCount(), "当前时间没有足够的可用实验室");

        BeanUtils.copyProperties(courseScheduleDTO, courseSchedule);
        courseSchedule.setUpdatedBy(token.getAdminId());
        courseSchedule.setUpdatedAt(new Date());
        courseScheduleMapper.updateByPrimaryKey(courseSchedule);
    }

    /**
     * 教师分页获取(自己)所有的预约申请列表
     */
    @Override
    public Page<CourseScheduleVO> listByPage(CourseScheduleQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "请输入查询参数");
        queryDTO.setCourseName(FormatUtils.makeFuzzySearchTerm(queryDTO.getCourseName()));
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //命中的查询条件数
        Integer size = courseScheduleMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        Page<CourseScheduleVO> data = new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), new ArrayList<>());

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<CourseSchedule> list = courseScheduleMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(CourseSchedule::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(CourseSchedule::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<CourseScheduleVO> voList = list.stream()
                .map(item -> CourseScheduleUtils.convertToVO(item, nameMap))
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    /**
     * 撤销某一条申请
     * 只能撤销处于"未审核" & "驳回修改"状态的申请
     *
     * @param scheduleVerifyDTO
     */
    @Override
    public void cancelCourseSchedule(ScheduleVerifyDTO scheduleVerifyDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //身份信息校验
        Assert.isTrue(AdminType.TEACHER.getValue().equals(token.getAdminType()), "您没有操作权限!");
        Assert.notNull(scheduleVerifyDTO.getId(), "预约申请的id不能为空");
        CourseSchedule courseSchedule = courseScheduleMapper.selectByPrimaryKey(scheduleVerifyDTO.getId());
        Assert.notNull(courseSchedule, "不存在Id为" + scheduleVerifyDTO.getId() + "的预约申请记录");
        //判断选中的申请是否处于可撤销的状态
        CourseScheduleUtils.validateCancelCourseSchedule(courseSchedule);
        //status = 4: "已撤销"
        courseSchedule.setStatus(CourseScheduleStatus.CANCEL.getValue());
        //reason : 为什么撤销(撤销原因), 非必须字段
        courseSchedule.setReason(scheduleVerifyDTO.getReason());
        BeanUtils.copyProperties(scheduleVerifyDTO, courseSchedule);

        courseSchedule.setUpdatedBy(token.getAdminId());
        courseSchedule.setUpdatedAt(new Date());
        courseScheduleMapper.updateByPrimaryKey(courseSchedule);
    }
}
