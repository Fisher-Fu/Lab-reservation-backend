package kreadcn.homework.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/25
 */
@Data
public class UserDTO {
    private Integer id;

    private String userCode;

    private String name;

    private Integer sex;

    private String identityNumber;

    private String email;

    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiredAt;

    private Boolean enabled;

    private Boolean local;

    private String description;
}
