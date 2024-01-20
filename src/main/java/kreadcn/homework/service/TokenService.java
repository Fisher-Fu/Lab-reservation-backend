package kreadcn.homework.service;


import kreadcn.homework.dto.UpdateSelfDTO;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.vo.OnlineUserVO;

import java.util.List;

/**
 * @author 李洪文
 * @date 2019/11/14 10:38
 */
public interface TokenService {
    /**
     * 用户登录，返回令牌信息
     *
     * @param userId   用户id
     * @param password 密码
     * @return 令牌信息
     */
    CurrentUser login(String userId, String password, String ipAddress);

    /**
     * 根据CurrentUser获取令牌信息
     *
     * @param accessCurrentUser CurrentUser
     * @return 令牌信息
     */
    CurrentUser getToken(String accessCurrentUser);

    /**
     * 登出系统
     *
     * @param accessCurrentUser 令牌CurrentUser
     */
    void logout(String accessCurrentUser);


    /**
     * 获取在线用户列表
     *
     * @return
     */
    List<OnlineUserVO> list();

    /**
     * 将在线用户踢出系统
     *
     * @param accessCurrentUser 用户的accessCurrentUser
     */
    void kick(String accessCurrentUser);

    void updateSelf(UpdateSelfDTO selfDTO);
}
