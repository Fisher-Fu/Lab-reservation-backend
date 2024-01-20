package kreadcn.homework.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


/**
 * @author He.Y
 * @date 2023/10/20
 */
@Data
public class SeasonDTO {
    private Integer id;
    /**
     * 学期名称
     */
    private String seasonName;
    /**
     * 学期是否可用
     */
    private Boolean enabled;
    /**
     * 学期开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startedAt;
}
