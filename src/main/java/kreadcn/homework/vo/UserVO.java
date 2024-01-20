package kreadcn.homework.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import kreadcn.homework.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserVO extends UserDTO {
    private String sexDesc;
    private String roleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastLoginAt;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;

    private Integer updatedBy;

    private String updatedByDesc;
}
