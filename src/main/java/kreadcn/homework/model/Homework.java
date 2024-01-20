package kreadcn.homework.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:homework表的实体类
 * @version
 * @author:  stone
 * @创建时间: 2023-09-18
 */
@Data
public class Homework {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String courseName;

    /**
     * 
     */
    private Boolean finished;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Integer createdBy;

    /**
     * 
     */
    private Integer updatedBy;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;
}