package kreadcn.homework.model;

import lombok.Data;

/**
 * 描述:schedule_lab表的实体类
 *
 * @author: He.Y
 * @创建时间: 2023-11-19
 */
@Data
public class ScheduleLab {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer scheduleId;

    /**
     * 实验室id
     */
    private Integer labId;

    /**
     *
     */
    private Integer createdBy;
}