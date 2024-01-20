package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:resource表的实体类
 *
 * @author: stone
 * @创建时间: 2022-07-05
 */
@Data
public class Resource {
    /**
     * 资源ID
     */
    private Integer id;

    /**
     * rCenter的资源id
     */
    private Integer dispatchId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 维护
     */
    private Boolean available;

    /**
     * 点击次数
     */
    private Long clicks;

    /**
     *
     */
    private String config;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 更新人
     */
    private Integer updatedBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
}