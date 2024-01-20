package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * ����:season���ʵ����
 *
 * @author: He.Y
 * @����ʱ��: 2023-10-24
 */
@Data
public class Season {
    /**
     *
     */
    private Integer id;

    /**
     * ѧ������
     */
    private String seasonName;

    /**
     * ��һ�ܿ�ʼ����
     */
    private Date startedAt;

    /**
     * �Ƿ����
     */
    private Boolean enabled;

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