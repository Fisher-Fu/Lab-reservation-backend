package kreadcn.homework.dao;

import kreadcn.homework.model.Resource;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);

    Integer count(@Param("queryDTO") KeywordQueryDTO queryDTO);

    List<Resource> list(
            @Param("queryDTO") KeywordQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    Integer delete(@Param("ids") List<Integer> ids);

    List<Resource> listByIds(@Param("ids") List<Integer> ids);

    List<Resource> listAll();
}