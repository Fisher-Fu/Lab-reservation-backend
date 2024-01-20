package kreadcn.homework.model;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class CurrentUser {
    public static final String ROOT_USER_CODE = "root";
    private String accessToken;
    private String userCode;
    private String email;
    private String phone;
    private String name;
    private String browser;
    private String avatar;
    private String os;
    private String device;
    private Integer sex;
    private String department;
    private String ipAddress;
    private Date lastAction;
    private Integer userId;
    private Integer adminId;
    /**
     * 后台管理权限
     */
    private Set<String> privSet;
    /**
     * 用户类型
     */
    private Integer adminType;
}
