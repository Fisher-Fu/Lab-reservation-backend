package kreadcn.homework.service;


import kreadcn.homework.dto.CourseScheduleDTO;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.dto.query.CourseScheduleQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.CourseScheduleVO;

import java.util.List;

/**
 * 课程预约模块服务接口
 *
 * @author He.Y
 * @date 2023-11-14
 */
public interface CourseScheduleService {
    Integer addCourseSchedule(CourseScheduleDTO courseScheduleDTO);

    int deleteByIds(List<Integer> courseScheduleIds);

    void updateCourseSchedule(CourseScheduleDTO courseScheduleDTO);

    Page<CourseScheduleVO> listByPage(CourseScheduleQueryDTO courseScheduleQueryDTO);

    void cancelCourseSchedule(ScheduleVerifyDTO scheduleVerifyDTO);
}
