package kreadcn.homework.dto;

import lombok.Data;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/4/26
 */
@Data
public class UpdateSelfDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String phone;
    private String email;
}
