package kreadcn.homework.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：
 *
 * @author He.Y
 * @date 2023/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SeasonQueryDTO extends PageQueryDTO {
    private String seasonName;
}
