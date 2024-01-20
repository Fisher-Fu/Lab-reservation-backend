package kreadcn.homework.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseScheduleVO {
    private Integer id;
    private String courseName;
    private List<Integer> section;
    private List<Integer> weekRange;
    private List<Integer> weekDay;
    private Integer studentCount;
    private Integer status;
    private Integer seasonId;
    private String seasonName;
    private String statusDesc;
    private String description;
    private Integer roomCount;
    private String reason;
    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建者id
     */
    private Integer createdBy;

    /**
     * 创建者
     */
    private String createdByDesc;
}
