package kreadcn.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleVerifyDTO {
    private Integer id;
    private String reason;
    private Integer status;
    /**
     * 申请分配的实验室id列表
     */
    private List<Integer> labIds;
}
