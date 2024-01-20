package kreadcn.homework.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author He.Y
 * @date 2023/11/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseScheduleQueryDTO extends PageQueryDTO {
    /**
     * 根据"课程名称(courseName)"查询
     */
    private String courseName;
}
