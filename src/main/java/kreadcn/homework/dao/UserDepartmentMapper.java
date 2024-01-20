package kreadcn.homework.dao;

import kreadcn.homework.model.UserDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDepartmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDepartment record);

    int insertSelective(UserDepartment record);

    UserDepartment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDepartment record);

    int updateByPrimaryKey(UserDepartment record);

    void deleteByDepartmentId(@Param("ids") List<Integer> ids, @Param("adminId") Integer adminId);

    List<UserDepartment> listByDepartmentId(@Param("id") Integer id, @Param("adminId") Integer adminId);

    int deleteByUserIds(@Param("departmentId") Integer departmentId, @Param("ids") List<Integer> ids);

    List<Integer> listByUserId(Integer userId);

    List<Integer> listByDepartmentIds(@Param("ids") List<Integer> deptIds);
}