package kreadcn.homework.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDetailVO {
    private Integer id;
    private String name;
    private List<UserVO> userList;
}
