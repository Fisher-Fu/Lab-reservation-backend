package kreadcn.homework.model;

import lombok.Data;

/**
 * 描述:homework_dept表的实体类
 * @version
 * @author:  stone
 * @创建时间: 2023-09-18
 */
@Data
public class HomeworkDept {
    /**
     * 
     */
    private Integer id;

    /**
     * 部门名称
     */
    private Integer departmentId;

    /**
     * 联系人
     */
    private Integer homeworkId;

    /**
     * 创建人
     */
    private Integer createdBy;
}