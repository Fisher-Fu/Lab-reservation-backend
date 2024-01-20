package kreadcn.homework.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author 何一凡
 * @description
 * @date 2023/10/25 19:23
 */
@Data
public class SeasonVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 学期名称
     */
    private String seasonName;

    /**
     * 第一周开始日期
     */
    private Date startedAt;

    /**
     * 是否可用
     */
    private Boolean enabled;

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
