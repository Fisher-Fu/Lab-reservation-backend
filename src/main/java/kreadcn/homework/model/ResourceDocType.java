package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:resource_doc_type表的实体类
 *
 * @author: stone
 * @创建时间: 2022-07-05
 */
@Data
public class ResourceDocType {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private Integer resourceId;

    /**
     *
     */
    private Integer docType;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 创建时间
     */
    private Date createdAt;
}