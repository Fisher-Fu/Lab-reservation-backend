package kreadcn.homework.service.impl;

import kreadcn.homework.dao.DepartmentMapper;
import kreadcn.homework.dao.UserDepartmentMapper;
import kreadcn.homework.dao.UserMapper;
import kreadcn.homework.dto.DeleteDepartmentUserDTO;
import kreadcn.homework.dto.DepartmentDTO;
import kreadcn.homework.dto.UserDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.*;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.DepartmentService;
import kreadcn.homework.service.utils.DepartmentUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.utils.XlsUtils;
import kreadcn.homework.vo.DepartmentDetailVO;
import kreadcn.homework.vo.DepartmentVO;
import kreadcn.homework.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static kreadcn.homework.service.utils.AdminUtils.getCurrentAdminId;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:33
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDepartmentMapper userDepartmentMapper;

    /**
     * 分页获取部门信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的部门数据列表
     */
    @Override
    public Page<DepartmentVO> listByPage(KeywordQueryDTO queryDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.notNull(queryDTO, "请提供查询参数");
        queryDTO.setKeyword(FormatUtils.makeFuzzySearchTerm(queryDTO.getKeyword()));
        if ("root".equalsIgnoreCase(token.getUserCode())) {
            queryDTO.setCreatedBy(null);
        } else {
            queryDTO.setCreatedBy(token.getAdminId());
        }

        Integer size = departmentMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Department> list = departmentMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Department::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Department::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<DepartmentVO> voList = new ArrayList<>();
        for (Department department : list) {
            // Department对象转VO对象
            DepartmentVO vo = DepartmentUtils.convertToVO(department, nameMap);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    /**
     * 更新部门数据
     *
     * @param departmentDTO 部门输入对象
     * @return 部门编码
     */
    @Override
    public Integer updateDepartment(DepartmentDTO departmentDTO) {
        // 校验输入数据正确性
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        DepartmentUtils.validateDepartment(departmentDTO);
        Assert.notNull(departmentDTO.getId(), "部门id不能为空");
        Department department = departmentMapper.selectByPrimaryKey(departmentDTO.getId());
        Assert.notNull(department, "没有找到部门，Id为：" + departmentDTO.getId());

        BeanUtils.copyProperties(departmentDTO, department);
        department.setUpdatedBy(token.getAdminId());
        department.setUpdatedAt(new Date());
        departmentMapper.updateByPrimaryKey(department);
        return department.getId();
    }

    /**
     * 根据编码列表，批量删除部门
     *
     * @param ids 编码列表
     */
    @Override
    @Transactional
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "部门id列表不能为空");
        Integer adminId = getCurrentAdminId();
        departmentMapper.deleteByIds(ids, getCurrentAdminId());
        userDepartmentMapper.deleteByDepartmentId(ids, adminId);
    }

    @Override
    public Map<Integer, String> getNameMap(Set<Integer> adminIds) {
        if (CollectionUtils.isEmpty(adminIds)) {
            return Collections.emptyMap();
        }

        List<Department> depts = departmentMapper.listByIds(new ArrayList<>(adminIds));
        return depts.stream().collect(Collectors.toMap(Department::getId, Department::getDepartmentName));
    }

    @Override
    @Transactional
    public int importDepartment(String departmentName, String contact, String contactPhone, String description, String fileName, InputStream inputStream) throws Exception {
        Assert.hasText(departmentName, "班级名称不能为空");
        // 先创建部门
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Department department = new Department();
        // 将输入的字段全部复制到实体对象中
        department.setDepartmentName(departmentName);
        department.setContact(contact);
        department.setContactPhone(contactPhone);
        department.setDescription(description);
        department.setCreatedAt(new Date());
        department.setUpdatedAt(new Date());
        department.setCreatedBy(token.getAdminId());
        department.setUpdatedBy(token.getAdminId());
        // 调用DAO方法保存到数据库表
        departmentMapper.insert(department);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 4);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("姓名", "name");
        map.put("学号", "userCode");
        AtomicInteger size = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (userDTO) -> {
            if (!StringUtils.hasText(userDTO.getUserCode()) || !StringUtils.hasText(userDTO.getName())) {
                return;
            }

            User user = userMapper.getByUserName(userDTO.getUserCode());
            if (user == null) {
                user = new User();
                user.setUserCode(userDTO.getUserCode());
                user.setName(userDTO.getName());
                user.setEnabled(true);
                user.setPassword(FormatUtils.password(user.getUserCode()));
                user.setExpiredAt(calendar.getTime());
                user.setCreatedAt(new Date());
                user.setUpdatedAt(new Date());
                user.setCreatedBy(token.getAdminId());
                user.setUpdatedBy(token.getAdminId());
                userMapper.insert(user);
            }

            // 新建班级学生关联
            UserDepartment userDepartment = new UserDepartment();
            userDepartment.setDepartmentId(department.getId());
            userDepartment.setUserId(user.getId());
            userDepartment.setCreatedBy(token.getAdminId());
            userDepartmentMapper.insert(userDepartment);
            size.incrementAndGet();
        }, map, UserDTO.class);

        return size.get();
    }

    @Override
    public DepartmentDetailVO getDetail(Integer id) {
        Department department = departmentMapper.selectByPrimaryKey(id);
        Assert.notNull(department, "未找到部门：id=" + id);
        DepartmentDetailVO vo = new DepartmentDetailVO();
        vo.setId(id);
        vo.setName(department.getDepartmentName());
        Integer adminId = getCurrentAdminId();

        List<UserDepartment> list = userDepartmentMapper.listByDepartmentId(id, adminId);
        Set<Integer> ids = list.stream().map(UserDepartment::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(ids)) {
            vo.setUserList(Collections.emptyList());
        } else {
            List<UserVO> userList = userMapper.listByIds(new ArrayList<>(ids))
                    .stream()
                    .map(item -> {
                        UserVO u = new UserVO();
                        BeanUtils.copyProperties(item, u);
                        return u;
                    })
                    .toList();
            vo.setUserList(userList);
        }
        return vo;
    }

    @Override
    public int deleteUser(DeleteDepartmentUserDTO delDTO) {
        Assert.notNull(delDTO, "参数不能为空");
        Assert.notEmpty(delDTO.getIds(), "删除用户列表不能为空");
        Department department = departmentMapper.selectByPrimaryKey(delDTO.getDepartmentId());
        Assert.notNull(department, "未找到部门：id=" + delDTO.getDepartmentId());
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.isTrue(department.getCreatedBy().equals(token.getAdminId()), "您不能修改其他老师的班级数据");
        return userDepartmentMapper.deleteByUserIds(delDTO.getDepartmentId(), delDTO.getIds());
    }

    @Override
    public int addUser(Integer departmentId, String userCode, String name) {
        Department department = departmentMapper.selectByPrimaryKey(departmentId);
        Assert.notNull(department, "未找到部门：id=" + departmentId);
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 4);
        User user = userMapper.getByUserName(userCode);
        if (user == null) {
            user = new User();
            user.setUserCode(userCode);
            user.setName(name);
            user.setEnabled(true);
            user.setPassword(FormatUtils.password(user.getUserCode()));
            user.setExpiredAt(calendar.getTime());
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setCreatedBy(token.getAdminId());
            user.setUpdatedBy(token.getAdminId());
            userMapper.insert(user);
        }

        // 新建班级学生关联
        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setDepartmentId(departmentId);
        userDepartment.setUserId(user.getId());
        userDepartment.setCreatedBy(token.getAdminId());
        userDepartmentMapper.insert(userDepartment);
        return user.getId();
    }
}
