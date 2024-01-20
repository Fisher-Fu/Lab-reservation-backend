package kreadcn.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteDepartmentUserDTO {
    private Integer departmentId;
    private List<Integer> ids;
}
