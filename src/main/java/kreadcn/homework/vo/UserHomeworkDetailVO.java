package kreadcn.homework.vo;

import kreadcn.homework.model.UserHomework;
import lombok.Data;

import java.util.List;

/**
 * 用于学生端
 */
@Data
public class UserHomeworkDetailVO {
    private Integer id;
    private String name;
    private String courseName;
    private List<UserHomework> files;
}
