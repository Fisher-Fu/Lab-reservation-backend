package kreadcn.homework.dao;

import kreadcn.homework.dto.query.UserQueryDTO;
import kreadcn.homework.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(@Param("user") User user);

    User selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKey(@Param("user") User user);

    Integer count(@Param("queryDTO") UserQueryDTO queryDTO);

    List<User> list(
            @Param("queryDTO") UserQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    User getByCode(@Param("userCode") String userCode);

    /**
     * @param userName userCode或者身份证号
     * @return
     */
    User getByUserName(@Param("userName") String userName);

    int delete(@Param("ids") List<Integer> ids);

    User login(
            @Param("userCode") String userCode,
            @Param("password") String password);

    List<User> listByIds(@Param("ids") List<Integer> ids);
}