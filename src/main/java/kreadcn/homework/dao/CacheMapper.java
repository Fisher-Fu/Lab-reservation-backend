package kreadcn.homework.dao;

import kreadcn.homework.model.Cache;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CacheMapper {
    int insert(Cache record);

    int count(@Param("resultId") int resultId);

    List<Cache> list(
            @Param("resultId") int resultId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    void delete(Integer rsId);

    void deleteAll();
}