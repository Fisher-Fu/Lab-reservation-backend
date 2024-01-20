package kreadcn.homework.dto;

import lombok.Data;

import java.util.List;

/**
 * @author He.Y
 * @date 2023/11/14
 */
@Data
public class CourseScheduleDTO {
    private Integer id;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 课节
     */
    private List<Integer> section;
    /**
     * 上课周
     */
    private List<Integer> weekRange;
    /**
     * 上课星期
     */
    private List<Integer> weekDay;
    /**
     * 上课人数
     */
    private Integer studentCount;
    /**
     * 需要实验室数量
     */
    private Integer roomCount;
    /**
     * 备注
     */
    private String description;
}
