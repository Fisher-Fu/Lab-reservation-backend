package kreadcn.homework.model;

import lombok.Data;

/**
 * ����:schedule_lab���ʵ����
 *
 * @author: He.Y
 * @����ʱ��: 2023-11-19
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
     * ʵ����id
     */
    private Integer labId;

    /**
     *
     */
    private Integer createdBy;
}