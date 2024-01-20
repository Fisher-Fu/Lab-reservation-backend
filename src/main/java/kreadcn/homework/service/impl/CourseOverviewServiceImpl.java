package kreadcn.homework.service.impl;

import kreadcn.homework.dao.CourseScheduleMapper;
import kreadcn.homework.dao.LabMapper;
import kreadcn.homework.dao.SeasonMapper;
import kreadcn.homework.dto.AllocatedScheduleDTO;
import kreadcn.homework.enums.CourseScheduleStatus;
import kreadcn.homework.model.AllocSchedule;
import kreadcn.homework.model.Lab;
import kreadcn.homework.service.CourseOverviewService;
import kreadcn.homework.service.utils.CourseScheduleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseOverviewServiceImpl implements CourseOverviewService {
    @Autowired
    CourseScheduleMapper courseScheduleMapper;
    @Autowired
    SeasonMapper seasonMapper;
    @Autowired
    LabMapper labMapper;

    @Override
    public Map<Integer, AllocSchedule> getDetails() {
        List<AllocatedScheduleDTO> allocList = courseScheduleMapper.getSchedules(CourseScheduleStatus.PASS.getValue(),
                seasonMapper.selectByEnabled(true).getId());
        List<Lab> labList = labMapper.getAllLabs();

        return CourseScheduleUtils.getAllAllocScheduleMap(allocList, labList);
    }
}
