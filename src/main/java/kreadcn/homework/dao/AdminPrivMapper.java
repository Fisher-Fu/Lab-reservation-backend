package kreadcn.homework.dao;

import kreadcn.homework.model.AdminPriv;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminPrivMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminPriv record);

    AdminPriv selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(AdminPriv record);

    List<AdminPriv> list(Integer id);

    void deleteByAdminIds(@Param("ids") List<Integer> ids);
}