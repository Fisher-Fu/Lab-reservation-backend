package kreadcn.homework.dao;

import kreadcn.homework.model.ResourceDocType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceDocTypeMapper {
    int insert(ResourceDocType record);

    void delete(@Param("ids") List<Integer> ids);

    List<Integer> listByResourceId(Integer id);

    List<ResourceDocType> listAll();
}