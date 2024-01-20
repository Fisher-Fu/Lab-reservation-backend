package kreadcn.homework.dto;

import kreadcn.homework.model.Lab;
import lombok.Data;

@Data
public class AllocatedScheduleDTO {
    /**
     * 预约申请的id
     */
    private Integer id;
    /**
     * 申请分配的lab的id
     */
    private Integer labId;
    /**
     * 分配的实验室明细
     */
    private Lab lab;
    /**
     * 课节
     */
    private Integer section;
    /**
     * 上课周
     */
    private Integer weekRange;
    /**
     * 上课星期
     */
    private Integer weekDay;
    /**
     * 上课教师id
     */
    private Integer createdBy;
    /**
     * 课程名
     */
    private String courseName;
}
