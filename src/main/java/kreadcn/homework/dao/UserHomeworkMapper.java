package kreadcn.homework.dao;

import kreadcn.homework.model.UserHomework;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserHomeworkMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserHomework record);

    int insertSelective(UserHomework record);

    UserHomework selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserHomework record);

    int updateByPrimaryKey(UserHomework record);

    List<Integer> listByUserId(Integer userId);

    List<UserHomework> listFiles(@Param("userId") Integer userId, @Param("homeworkId") Integer homeworkId);

    List<UserHomework> listByHomeworkId(Integer id);
}