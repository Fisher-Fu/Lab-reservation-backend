package kreadcn.homework.dto.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/4/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginLogQueryDTO extends PageQueryDTO {
    private String userCode;
    private String ipAddress;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    private String orderBy;
}
