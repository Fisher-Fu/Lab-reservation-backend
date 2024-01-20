package kreadcn.homework.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述：
 *
 * @author He.Y
 * @date 2023/10/20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LabQueryDTO extends PageQueryDTO {

    private String name;
    private String address;
}
