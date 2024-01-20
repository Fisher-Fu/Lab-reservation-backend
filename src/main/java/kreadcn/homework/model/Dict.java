package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:dict表的实体类
 *
 * @author: stone
 * @创建时间: 2022-04-27
 */
@Data
public class Dict {
    /**
     * 字典主键
     */
    private Integer id;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典编码
     */
    private Integer dictCode;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
}