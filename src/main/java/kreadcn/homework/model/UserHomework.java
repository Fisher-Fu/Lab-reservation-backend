package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * 描述:user_homework表的实体类
 *
 * @author: stone
 * @创建时间: 2023-09-18
 */
@Data
public class UserHomework {
    /**
     *
     */
    private Integer id;
    /**
     *
     */
    private Integer homeworkId;

    /**
     *
     */
    private String sha1;

    /**
     *
     */
    private Integer fileSize;

    /**
     *
     */
    private String fileName;

    private String ipAddress;

    /**
     *
     */
    private Integer createdBy;

    /**
     *
     */
    private Date createdAt;
}