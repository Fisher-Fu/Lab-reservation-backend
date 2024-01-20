package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.CourseScheduleDTO;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.dto.query.CourseScheduleQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.CourseScheduleService;
import kreadcn.homework.vo.CourseScheduleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courseSchedule")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class CourseScheduleController {
    @Autowired
    private CourseScheduleService courseScheduleService;

    @PostMapping("addCourseSchedule")
    @Privilege("add")
    public Integer addCourseSchedule(@RequestBody CourseScheduleDTO courseScheduleDTO) {
        return courseScheduleService.addCourseSchedule(courseScheduleDTO);
    }

    @PostMapping("deleteCourseSchedule")
    @Privilege("delete")
    public Integer deleteCourseSchedule(@RequestBody List<Integer> courseScheduleIds) {
        return courseScheduleService.deleteByIds(courseScheduleIds);
    }

    @PostMapping("updateCourseSchedule")
    @Privilege("update")
    public void updateCourseSchedule(@RequestBody CourseScheduleDTO courseScheduleDTO) {
        courseScheduleService.updateCourseSchedule(courseScheduleDTO);
    }

    @PostMapping("listCourseSchedule")
    @Privilege("page")
    public Page<CourseScheduleVO> listCourseSchedule(@RequestBody CourseScheduleQueryDTO queryDTO) {
        return courseScheduleService.listByPage(queryDTO);
    }

    @PostMapping("cancelCourseSchedule")
    @Privilege("update")
    public void cancelCourseSchedule(@RequestBody ScheduleVerifyDTO scheduleVerifyDTO) {
        courseScheduleService.cancelCourseSchedule(scheduleVerifyDTO);
    }
}
