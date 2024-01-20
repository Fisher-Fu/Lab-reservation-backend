package kreadcn.homework.dto;

import lombok.Data;

import java.util.List;

/**
 * @author He.Y
 * @date 2023/10/20
 */
@Data
public class LabDTO {
    private Integer id;
    /**
     * 实验室名称
     */
    private String name;
    /**
     * 实验室地点
     */
    private String address;
    /**
     * 实验室最大容纳人数
     */
    private Integer capacity;
    /**
     * 实验室面积
     */
    private Integer area;
    /**
     * 实验室详细描述
     */
    private String description;
    /**
     * 实验室所属管理员id列表
     */
    private List<Integer> adminIds;
}
