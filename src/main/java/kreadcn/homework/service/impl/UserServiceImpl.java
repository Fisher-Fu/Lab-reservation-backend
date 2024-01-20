package kreadcn.homework.service.impl;

import kreadcn.homework.dao.UserMapper;
import kreadcn.homework.dto.UserDTO;
import kreadcn.homework.dto.query.UserQueryDTO;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.model.Page;
import kreadcn.homework.model.User;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.DepartmentService;
import kreadcn.homework.service.UserService;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/24
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public Page<UserVO> listByPage(UserQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "参数不能为空");
        FormatUtils.trimFieldToNull(queryDTO);

        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));
        queryDTO.setKeyword(FormatUtils.makeFuzzySearchTerm(queryDTO.getKeyword()));

        Integer size = userMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }

        List<User> userList = userMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        Set<Integer> adminIds = userList.stream().map(User::getUpdatedBy).collect(Collectors.toSet());
        Map<Integer, String> adminMap = adminService.getNameMap(adminIds);

        List<UserVO> resourceVOList = userList
                .stream()
                .map(item -> {
                    UserVO userVO = new UserVO();
                    BeanUtils.copyProperties(item, userVO);
                    userVO.setUpdatedByDesc(adminMap.get(item.getCreatedBy()));
                    userVO.setSexDesc(Integer.valueOf(0).equals(item.getSex()) ? "女" : "男");
                    return userVO;
                })
                .collect(Collectors.toList());
        //返回分页对象
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), resourceVOList);
    }

    @Override
    public UserDTO getDetail(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        Assert.notNull(user, "未找到用户");
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public Integer updateUser(UserDTO userDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        User user = userMapper.selectByPrimaryKey(userDTO.getId());
        Assert.notNull(user, "用户不存在：id=" + userDTO.getId());
        validateUser(userDTO);
        BeanUtils.copyProperties(userDTO, user, "userCode", "name");
        user.setPassword(null);
        user.setUpdatedBy(token.getUserId());
        user.setUpdatedAt(new Date());
        userMapper.updateByPrimaryKey(user);
        return user.getId();
    }

    @Override
    public Integer addUser(UserDTO userDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        validateUser(userDTO);
        Assert.isTrue(userDTO.getUserCode().indexOf("@") < 0, "学号里不能含有@");
        Assert.isNull(userMapper.getByCode(userDTO.getUserCode()), "学号已经存在");
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(FormatUtils.password(userDTO.getUserCode()));
        user.setUpdatedBy(token.getUserId());
        user.setCreatedBy(token.getUserId());
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public int delete(List<Integer> ids) {
        Assert.notEmpty(ids, "删除列表不能为空");

        int result = userMapper.delete(ids);
        return result;
    }

    @Override
    public Map<Integer, User> getNameMap(Set<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }

        return userMapper.listByIds(new ArrayList<>(ids))
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    @Override
    public void resetPassword(Integer id) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        User user = userMapper.selectByPrimaryKey(id);
        Assert.notNull(user, "用户不存在：id=" + id);
        user.setUpdatedBy(token.getAdminId());
        user.setUpdatedAt(new Date());
        user.setPassword(FormatUtils.password(user.getUserCode()));
        userMapper.updateByPrimaryKey(user);
    }

    private void validateUser(UserDTO userDTO) {
        Assert.notNull(userDTO, "用户信息不能为空");
        FormatUtils.trimFieldToNull(userDTO);
        Assert.hasText(userDTO.getUserCode(), "用户登录名不能为空");
        userDTO.setUserCode(userDTO.getUserCode().toLowerCase());
        Assert.hasText(userDTO.getName(), "用户名称不能为空");
        Assert.notNull(userDTO.getExpiredAt(), "请指定一个过期时间");
    }
}
