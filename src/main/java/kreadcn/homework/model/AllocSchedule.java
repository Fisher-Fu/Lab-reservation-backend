package kreadcn.homework.model;

import kreadcn.homework.dto.AllocatedScheduleDTO;
import lombok.Data;

import java.util.Map;

@Data
public class AllocSchedule {
    private int labId;
    private String labName;
    //key: "weekRange.weekDay.section"
    //value: 对应的申请明细
    private Map<String, AllocatedScheduleDTO> allocMap;
}
