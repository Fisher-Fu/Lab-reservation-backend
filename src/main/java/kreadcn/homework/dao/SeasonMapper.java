package kreadcn.homework.dao;

import kreadcn.homework.dto.query.SeasonQueryDTO;
import kreadcn.homework.model.Season;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学期数据访问组件
 *
 * @author He.Y
 * @date 2023/10/24 15:50
 */
public interface SeasonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Season record);

    int insertSelective(Season record);

    Season selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Season record);

    int updateByPrimaryKey(Season record);

    /**
     * 根据id列表批量删除学期
     *
     * @param seasonIds id列表
     */
    void deleteByIds(@Param("seasonIds") List<Integer> seasonIds);

    /**
     * 根据查询条件获取命中个数
     * 不使用 @Param 注解
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(SeasonQueryDTO queryDTO);

    /**
     * 根据查询条件获取学期列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 学期列表
     */
    List<Season> list(
            @Param("queryDTO") SeasonQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 根据可用性获取可用的学期信息
     *
     * @param enabled
     * @return
     */
    Season selectByEnabled(Boolean enabled);
}