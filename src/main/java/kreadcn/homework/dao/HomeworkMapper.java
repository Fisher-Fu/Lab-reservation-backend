package kreadcn.homework.dao;

import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Homework;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomeworkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Homework record);

    Homework selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Homework record);

    Integer count(@Param("queryDTO") KeywordQueryDTO queryDTO);

    List<Homework> list(
            @Param("queryDTO") KeywordQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    int deleteByIds(@Param("ids") List<Integer> ids, @Param("adminId") Integer adminId);

    List<Homework> listByIds(@Param("ids") List<Integer> ids);
}