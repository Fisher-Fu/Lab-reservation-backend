package kreadcn.homework.utils;

import kreadcn.homework.model.CurrentUser;
import org.springframework.util.Assert;

/**
 * token 存取工具类
 *
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:24
 */
public class ThreadContextHolder {
    public static final String SESSION_TIMEOUT_ERROR = "未找到访问令牌，请重新登录";

    private static ThreadLocal<CurrentUser> tokenHolder = new ThreadLocal<>();

    public static CurrentUser getCurrentUser() {
        CurrentUser currentUser = tokenHolder.get();
        Assert.notNull(currentUser, SESSION_TIMEOUT_ERROR);
        return currentUser;
    }

    public static void setCurrentUser(CurrentUser token) {
        tokenHolder.set(token);
    }

    public static CurrentUser getCurrentUserWithoutAssert() {
        return tokenHolder.get();
    }
}
