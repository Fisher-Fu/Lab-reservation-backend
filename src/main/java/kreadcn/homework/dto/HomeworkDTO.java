package kreadcn.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class HomeworkDTO {
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

    private List<Integer> deptIds;
}
