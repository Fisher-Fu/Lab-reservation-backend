package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:lab表的实体类
 *
 * @author: He.Y
 * @创建时间: 2023-10-20
 */
@Data
public class Lab {
    /**
     *
     */
    private Integer id;

    /**
     * 实验室名称
     */
    private String name;

    /**
     * 实验室地点
     */
    private String address;

    /**
     * 实验室设施描述
     */
    private String description;

    /**
     * 实验室最大容纳人数
     */
    private Integer capacity;

    /**
     * 面积
     */
    private Integer area;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建者
     */
    private Integer createdBy;

    /**
     * 更新者
     */
    private Integer updatedBy;
}