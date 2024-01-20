package kreadcn.homework.vo;

import kreadcn.homework.dto.HomeworkDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeworkVO extends HomeworkDTO {
    private Boolean finished;
    private List<String> departmentNames;
    private String createdByDesc;
    private Date createdAt;
}
