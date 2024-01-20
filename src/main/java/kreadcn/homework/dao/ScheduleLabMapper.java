package kreadcn.homework.dao;

import kreadcn.homework.model.ScheduleLab;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleLabMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ScheduleLab record);

    int insertSelective(ScheduleLab record);

    ScheduleLab selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScheduleLab record);

    int updateByPrimaryKey(ScheduleLab record);

    void deleteByScheduleIds(@Param("ids") List<Integer> ids);
}