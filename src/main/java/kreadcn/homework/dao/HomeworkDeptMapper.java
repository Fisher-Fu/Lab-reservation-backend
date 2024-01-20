package kreadcn.homework.dao;

import kreadcn.homework.model.HomeworkDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomeworkDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomeworkDept record);

    HomeworkDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(HomeworkDept record);

    void deleteByHomeworkId(Integer id);

    void deleteByHomeworkIds(@Param("ids") List<Integer> ids, @Param("adminId") Integer adminId);

    List<HomeworkDept> listByHomeworkIds(@Param("ids") List<Integer> ids);

    List<Integer> listByDeptIds(@Param("ids") List<Integer> ids);
}