package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.service.ScheduleVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author He.Y
 */
@RestController
@RequestMapping("/api/scheduleVerify")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class ScheduleVerifyController {
    @Autowired
    private ScheduleVerifyService scheduleVerifyService;

    @PostMapping("verify")
    @Privilege("update")
    public Integer scheduleVerify(@RequestBody ScheduleVerifyDTO scheduleVerifyDTO) {
        return scheduleVerifyService.scheduleVerify(scheduleVerifyDTO);
    }

}
