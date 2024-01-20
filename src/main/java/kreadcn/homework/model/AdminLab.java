package kreadcn.homework.model;

import lombok.Data;

/**
 * ����:admin_lab���ʵ����
 *
 * @author: 27775
 * @����ʱ��: 2023-10-26
 */
@Data
public class AdminLab {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer adminId;

    /**
     * ʵ����id
     */
    private Integer labId;

    /**
     *
     */
    private Integer createdBy;
}