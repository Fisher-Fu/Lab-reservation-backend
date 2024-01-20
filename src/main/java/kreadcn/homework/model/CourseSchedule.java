package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

@Data
public class CourseSchedule {
    /**
     *
     */
    private Integer id;

    /**
     *
     */
    private String courseName;

    /**
     *
     */
    private Integer section;

    /**
     *
     */
    private Integer weekRange;

    /**
     *
     */
    private Integer weekDay;

    /**
     *
     */
    private Integer studentCount;

    /**
     *
     */
    private Integer status;

    /**
     *
     */
    private Integer seasonId;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String reason;

    /**
     *
     */
    private Integer roomCount;

    /**
     *
     */
    private Date createdAt;

    /**
     *
     */
    private Date updatedAt;

    /**
     *
     */
    private Integer createdBy;

    /**
     *
     */
    private Integer updatedBy;
}