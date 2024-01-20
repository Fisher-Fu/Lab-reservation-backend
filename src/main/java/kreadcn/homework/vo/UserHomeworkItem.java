package kreadcn.homework.vo;

import kreadcn.homework.model.UserHomework;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserHomeworkItem {
    private Integer userId;
    private String userCode;
    private String name;
    private Date createdAt;
    private List<UserHomework> files;
}
