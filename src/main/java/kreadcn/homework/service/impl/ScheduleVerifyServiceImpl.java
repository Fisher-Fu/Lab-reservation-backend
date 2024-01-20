package kreadcn.homework.service.impl;

import kreadcn.homework.dao.CourseScheduleMapper;
import kreadcn.homework.dao.LabMapper;
import kreadcn.homework.dao.ScheduleLabMapper;
import kreadcn.homework.dao.SeasonMapper;
import kreadcn.homework.dto.AllocatedScheduleDTO;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.enums.AdminType;
import kreadcn.homework.enums.CourseScheduleStatus;
import kreadcn.homework.model.*;
import kreadcn.homework.service.ScheduleVerifyService;
import kreadcn.homework.service.utils.CourseScheduleUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleVerifyServiceImpl implements ScheduleVerifyService {

    @Autowired
    private CourseScheduleMapper courseScheduleMapper;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private ScheduleLabMapper scheduleLabMapper;

    @Autowired
    private SeasonMapper seasonMapper;

    /**
     * 实验员更新预约申请数据(实验员审核功能)
     *
     * @param scheduleVerifyDTO 输入对象
     * @return 预约申请编码
     */
    @Override
    @CacheEvict("allAllocSchedule")
    @Transactional
    public Integer scheduleVerify(ScheduleVerifyDTO scheduleVerifyDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //身份信息校验
        Assert.isTrue(AdminType.TECHNICIAN.getValue().equals(token.getAdminType()), "您没有审核权限, 该功能仅对实验员开放!");
        CourseScheduleUtils.validateCourseScheduleForTechnician(scheduleVerifyDTO);
        Assert.notNull(scheduleVerifyDTO.getId(), "预约申请的id不能为空");
        CourseSchedule courseSchedule = courseScheduleMapper.selectByPrimaryKey(scheduleVerifyDTO.getId());
        Assert.notNull(courseSchedule, "不存在Id为" + scheduleVerifyDTO.getId() + "的预约申请记录");
        //审核时触发申请审批状态校验功能
        CourseScheduleUtils.validateVerifySchedule(courseSchedule);
        BeanUtils.copyProperties(scheduleVerifyDTO, courseSchedule);

        courseSchedule.setUpdatedBy(token.getAdminId());
        courseSchedule.setUpdatedAt(new Date());
        List<Integer> ids = new ArrayList<>();
        ids.add(courseSchedule.getId());
        scheduleLabMapper.deleteByScheduleIds(ids);
        updateOtherInfo(scheduleVerifyDTO);
        courseScheduleMapper.updateByPrimaryKey(courseSchedule);
        return courseSchedule.getId();
    }

    /**
     * 添加schedule-lab的关联信息
     *
     * @param scheduleVerifyDTO
     */
    private void updateOtherInfo(ScheduleVerifyDTO scheduleVerifyDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        CourseSchedule courseSchedule = courseScheduleMapper.selectByPrimaryKey(scheduleVerifyDTO.getId());
        if (CollectionUtils.isEmpty(scheduleVerifyDTO.getLabIds())) {
            return;
        }
        //检验分配的实验室数量是否与申请人的要求一致
        Assert.isTrue(scheduleVerifyDTO.getLabIds().size() == courseSchedule.getRoomCount(), "您分配的实验室数量不合对方要求");
        //检测分配的实验室是否合理
        CourseScheduleUtils.checkLabs(listAllAllocSchedule(), courseSchedule, scheduleVerifyDTO.getLabIds(), labMapper.getAllLabs());
        for (Integer id : scheduleVerifyDTO.getLabIds()) {
            Lab lab = labMapper.selectByPrimaryKey(id);
            Assert.notNull(lab, "不存在id为" + id + "实验室");
            ScheduleLab scheduleLab = new ScheduleLab();
            scheduleLab.setScheduleId(scheduleVerifyDTO.getId());
            scheduleLab.setLabId(id);
            scheduleLab.setCreatedBy(token.getAdminId());
            scheduleLabMapper.insert(scheduleLab);
        }
    }

    /**
     * 获取全部已通过审核的申请明细
     *
     * @return 全部已通过审核的申请明细
     */
    @Cacheable("allAllocSchedule")
    public Map<Integer, AllocSchedule> listAllAllocSchedule() {
        List<AllocatedScheduleDTO> allocList = courseScheduleMapper.getSchedules(CourseScheduleStatus.PASS.getValue(),
                seasonMapper.selectByEnabled(true).getId());
        List<Lab> labList = labMapper.getAllLabs();
        return CourseScheduleUtils.getAllAllocScheduleMap(allocList, labList);
    }
}
