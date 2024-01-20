package kreadcn.homework.service;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/5/11
 */
public interface SecretAuditService {
    /**
     * 每当用户登陆失败的时候调用
     *
     * @param userCode  用户ID
     * @param ipAddress IP地址
     */
    void addErrorLogin(String userCode, String ipAddress);

    /**
     * 判断用户ID或者ipaddress是否需要显示验证码
     *
     * @param userCode  用户ID
     * @param ipAddress IP地址
     * @return
     */
    void checkAbnormal(String userCode, String ipAddress);
}
