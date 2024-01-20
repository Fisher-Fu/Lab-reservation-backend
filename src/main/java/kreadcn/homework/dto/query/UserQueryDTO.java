package kreadcn.homework.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageQueryDTO {
    private String keyword;
    private Integer roleId;
    private Integer expired;
    private Integer enabled;
    private Integer logined;
    private String orderBy;
}
