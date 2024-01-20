package kreadcn.homework.controller.system;

import kreadcn.homework.model.AllocSchedule;
import kreadcn.homework.service.CourseOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/courseOverview")
public class CourseOverviewController {
    @Autowired
    private CourseOverviewService courseOverviewService;

    @PostMapping("listDetails")
    public Map<Integer, AllocSchedule> getDetails() {
        return courseOverviewService.getDetails();
    }
}
