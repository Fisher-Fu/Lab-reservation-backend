package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:season表的实体类
 *
 * @author: He.Y
 * @创建时间: 2023-10-24
 */
@Data
public class Season {
    /**
     *
     */
    private Integer id;

    /**
     * 学期名称
     */
    private String seasonName;

    /**
     * 第一周开始日期
     */
    private Date startedAt;

    /**
     * 是否可用
     */
    private Boolean enabled;

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