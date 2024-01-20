package kreadcn.homework.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordQueryDTO extends PageQueryDTO {
    private String keyword;
    private String orderBy;
    private Integer createdBy;
}
