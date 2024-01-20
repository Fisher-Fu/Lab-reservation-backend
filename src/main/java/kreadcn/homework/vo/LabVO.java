package kreadcn.homework.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 何一凡
 * @description
 * @date 2023/10/20 15:31
 */
@Data
public class LabVO {
    /**
     * id
     */
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
     * 实验室设施描述
     */
    private String description;

    /**
     * 实验室最大容纳人数
     */
    private Integer capacity;

    /**
     * 面积
     */
    private Integer area;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建者
     */
    private Integer createdBy;

    /**
     * 创建者
     */
    private String createdByDesc;

}
