package kreadcn.homework.vo;

import lombok.Data;

import java.util.List;

/**
 * 用于教师端
 */
@Data
public class HomeworkDetailVO {
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
    private String description;

    private List<UserHomeworkItem> userList;
}
