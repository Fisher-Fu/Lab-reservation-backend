package kreadcn.homework.dao;

import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(Admin record);

    Admin selectByPrimaryKey(Integer userid);

    int updateByPrimaryKey(Admin record);

    Admin login(@Param("userCode") String userCode, @Param("password") String password);

    List<Admin> listByUserIds(@Param("ids") List<Integer> ids);

    Integer count(@Param("queryDTO") KeywordQueryDTO queryDTO);

    List<Admin> list(
            @Param("queryDTO") KeywordQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    int delete(@Param("ids") List<Integer> ids);

    Admin getByUserId(@Param("userId") Integer userId);

    Admin getByUserCode(@Param("userCode") String userCode);

}