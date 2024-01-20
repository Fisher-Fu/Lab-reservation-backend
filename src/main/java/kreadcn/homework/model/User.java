package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String userCode;

    private String password;

    private String name;

    private Integer sex;

    private Integer roleId;

    private Integer departmentId;

    private String identityNumber;

    private String email;

    private String phone;

    private Date expiredAt;

    private Boolean enabled;
    private Boolean local;

    private String description;

    private Date firstLoginAt;

    private Date lastLoginAt;

    private Date createdAt;

    private Integer createdBy;

    private Date updatedAt;

    private Integer updatedBy;
}