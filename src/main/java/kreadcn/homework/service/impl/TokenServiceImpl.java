package kreadcn.homework.service.impl;

import kreadcn.homework.dao.AdminMapper;
import kreadcn.homework.dao.AdminPrivMapper;
import kreadcn.homework.dao.DepartmentMapper;
import kreadcn.homework.dao.UserMapper;
import kreadcn.homework.dto.UpdateSelfDTO;
import kreadcn.homework.model.*;
import kreadcn.homework.service.TokenService;
import kreadcn.homework.service.utils.TokenUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.OnlineUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminPrivMapper adminPrivMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Value("${timeout:20}")
    private int timeout;


    private Map<String, CurrentUser> tokenMap = new ConcurrentHashMap<>(1 << 8);

    /**
     * 用户登录，返回令牌信息
     *
     * @param userId    用户id
     * @param password  密码
     * @param ipAddress
     * @return 令牌信息
     */
    @Override
    public CurrentUser login(String userId, String password, String ipAddress) {
        CurrentUser token = new CurrentUser();

        User user = userMapper.login(userId, FormatUtils.password(password));
        if (user != null) {

            Assert.isTrue(Boolean.TRUE.equals(user.getEnabled()), "此账户已经禁用，不能登录");
            Department department = departmentMapper.selectByPrimaryKey(user.getDepartmentId());
            if (department != null) {
                token.setDepartment(department.getDepartmentName());
            }

            token.setUserId(user.getId());
            token.setSex(user.getSex());
            token.setName(user.getName());
            token.setPrivSet(new HashSet<>());
            user.setLastLoginAt(new Date());
            if (user.getFirstLoginAt() == null) {
                user.setFirstLoginAt(new Date());
            }
            userMapper.updateByPrimaryKey(user);
        } else {
            Admin admin = adminMapper.login(userId, FormatUtils.password(password));
            Assert.notNull(admin, "用户名或者密码错误");
            Assert.isTrue(Boolean.TRUE.equals(admin.getEnabled()), "此账户已经禁用，不能登录");
            token.setAdminId(admin.getId());
            token.setDepartment(admin.getDepartment());
            token.setSex(admin.getSex());
            token.setName(admin.getName());
            //login时获取当前用户的adminType信息
            token.setAdminType(admin.getAdminType());
            token.setPrivSet(new HashSet<>());
            List<AdminPriv> privList = adminPrivMapper.list(admin.getId());
            token.setPrivSet(new HashSet<>());
            for (AdminPriv priv : privList) {
                token.getPrivSet().add(priv.getModId() + '.' + priv.getPriv());
            }
        }

        token.setLastAction(new Date());
        token.setIpAddress(ipAddress);
        token.setUserCode(userId);
        token.setAccessToken(makeToken());
        tokenMap.put(token.getAccessToken(), token);
        return token;
    }

    /**
     * 根据token获取令牌信息
     *
     * @param accessToken token
     * @return 令牌信息
     */
    @Override
    public CurrentUser getToken(String accessToken) {
        if (FormatUtils.isEmpty(accessToken)) {
            return null;
        }

        return tokenMap.get(accessToken);
    }

    /**
     * 登出系统
     *
     * @param accessToken 令牌token
     */
    @Override
    public void logout(String accessToken) {
        tokenMap.remove(accessToken);
    }

    /**
     * 获取在线用户列表
     *
     * @return
     */
    @Override
    public List<OnlineUserVO> list() {
        Collection<CurrentUser> tokens = tokenMap.values();
        return tokens.stream().map(item -> TokenUtils.convertToVO(item)).collect(Collectors.toList());
    }

    /**
     * 将在线用户踢出系统
     *
     * @param accessToken 用户的accessToken
     */
    @Override
    public void kick(String accessToken) {

    }

    @Override
    public void updateSelf(UpdateSelfDTO selfDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        if (token.getAdminId() != null) {
            Admin admin = adminMapper.getByUserCode(token.getUserCode());
            Assert.notNull(admin, "账户不存在");
            if (!FormatUtils.isEmpty(selfDTO.getOldPassword())) {
                Assert.hasText(selfDTO.getNewPassword(), "请提供新密码");
                Assert.notNull(adminMapper.login(token.getUserCode(), FormatUtils.password(selfDTO.getOldPassword())), "旧密码不正确");
                admin.setPassword(FormatUtils.password(selfDTO.getNewPassword()));
            }

            if (StringUtils.hasText(selfDTO.getPhone())) {
                admin.setPhone(selfDTO.getPhone());
            }

            if (StringUtils.hasText(selfDTO.getEmail())) {
                admin.setEmail(selfDTO.getEmail());
            }

            token.setEmail(selfDTO.getEmail());
            token.setPhone(selfDTO.getPhone());

            adminMapper.updateByPrimaryKey(admin);
        } else {
            User user = userMapper.getByCode(token.getUserCode());
            Assert.notNull(user, "请重新登录");
            user.setPassword(null);
            Assert.notNull(user, "未找到该用户");

            if (!FormatUtils.isEmpty(selfDTO.getOldPassword())) {
                Assert.hasText(selfDTO.getNewPassword(), "请提供新密码");
                Assert.notNull(userMapper.login(token.getUserCode(), FormatUtils.password(selfDTO.getOldPassword())), "旧密码不正确");
                user.setPassword(FormatUtils.password(selfDTO.getNewPassword()));
            }
            user.setPhone(selfDTO.getPhone());
            user.setEmail(selfDTO.getEmail());
            userMapper.updateByPrimaryKey(user);
        }
    }

    private String makeToken() {
        return UUID.randomUUID().toString().replaceAll("-", "") + "";
    }

    @Scheduled(fixedRate = 1000 * 60)
    public void timeoutCheck() {
        Iterator<Map.Entry<String, CurrentUser>> iterator = tokenMap.entrySet().iterator();
        Date now = new Date();
        while (iterator.hasNext()) {
            Map.Entry<String, CurrentUser> entry = iterator.next();
            if (now.getTime() - entry.getValue().getLastAction().getTime() > 60 * timeout * 1000) {
                iterator.remove();
            }
        }
    }
}
