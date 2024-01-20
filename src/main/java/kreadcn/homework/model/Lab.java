package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * ����:lab���ʵ����
 *
 * @author: He.Y
 * @����ʱ��: 2023-10-20
 */
@Data
public class Lab {
    /**
     *
     */
    private Integer id;

    /**
     * ʵ��������
     */
    private String name;

    /**
     * ʵ���ҵص�
     */
    private String address;

    /**
     * ʵ������ʩ����
     */
    private String description;

    /**
     * ʵ���������������
     */
    private Integer capacity;

    /**
     * ���
     */
    private Integer area;

    /**
     * ����ʱ��
     */
    private Date createdAt;

    /**
     * ����ʱ��
     */
    private Date updatedAt;

    /**
     * ������
     */
    private Integer createdBy;

    /**
     * ������
     */
    private Integer updatedBy;
}