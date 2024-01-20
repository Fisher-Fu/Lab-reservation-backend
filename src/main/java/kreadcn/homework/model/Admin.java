package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

/**
 * @author He.Y
 * @description 添加了"adminType"字段
 * @date 2023/10/20
 */
@Data
public class Admin {
    private Integer id;

    /**
     * 租户代码，为空表示平台管理员
     */
    private String tenantCode;

    private String userCode;

    /**
     * 物流点地址代码
     */
    private Integer roleId;

    private String name;

    private Integer sex;

    private Boolean enabled;

    private String password;

    private String department;

    private String phone;

    private String email;

    private Integer adminType;

    private Date createdAt;

    private Integer createdBy;

    private Date updatedAt;

    private Integer updatedBy;
}