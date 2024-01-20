package kreadcn.homework.service;

import kreadcn.homework.dto.UserDTO;
import kreadcn.homework.dto.query.UserQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.model.User;
import kreadcn.homework.vo.UserVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/22
 */
public interface UserService {
    Page<UserVO> listByPage(UserQueryDTO queryDTO);

    UserDTO getDetail(Integer id);

    Integer updateUser(UserDTO userDTO);

    Integer addUser(UserDTO userDTO);

    int delete(List<Integer> codes);

    Map<Integer, User> getNameMap(Set<Integer> ids);

    void resetPassword(Integer id);
}
