package kreadcn.homework.dao;

import kreadcn.homework.model.AdminLab;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminLabMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminLab record);

    int insertSelective(AdminLab record);

    AdminLab selectByPrimaryKey(Integer id);

    List<AdminLab> selectByAdminId(Integer adminId);

    List<AdminLab> selectByLabId(Integer labId);

    int updateByPrimaryKeySelective(AdminLab record);

    int updateByPrimaryKey(AdminLab record);

    void deleteByAdminIds(@Param("ids") List<Integer> ids);

    void deleteByLabIds(@Param("ids") List<Integer> ids);
}