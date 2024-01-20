package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:cache表的实体类
 *
 * @author: stone
 * @创建时间: 2022-05-02
 */
@Data
public class Cache {
    /**
     *
     */
    private Integer id;

    private Integer resourceId;
    /**
     *
     */
    private Integer resultId;

    /**
     * 记录
     */
    private String json;

    /**
     * 创建时间
     */
    private Date createdAt;
}