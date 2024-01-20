package kreadcn.homework.dao;

import kreadcn.homework.dto.query.LabQueryDTO;
import kreadcn.homework.model.Lab;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 实验室数据访问组件
 *
 * @author He.Y
 * @date 2023/10/20 14:38
 */
public interface LabMapper {
    Lab selectByPrimaryKey(Integer id);

    int insertSelective(Lab record);

    int deleteByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Lab record);

    /**
     * 新增记录
     *
     * @param record
     * @return
     */
    int insert(Lab record);

    /**
     * 根据主键更新记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Lab record);

    /**
     * 根据查询条件获取命中个数
     * 不使用 @Param 注解
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(LabQueryDTO queryDTO);

    /**
     * 根据查询条件获取实验室列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 实验室列表
     */
    List<Lab> list(
            @Param("queryDTO") LabQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 根据id列表批量删除实验室
     *
     * @param labIds id列表
     */
    int deleteByIds(@Param("labIds") List<Integer> labIds);

    List<Lab> getAllLabs();
}