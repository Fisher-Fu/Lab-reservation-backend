package kreadcn.homework.service.impl;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.dao.AdminLabMapper;
import kreadcn.homework.dao.AdminMapper;
import kreadcn.homework.dao.AdminPrivMapper;
import kreadcn.homework.dao.LabMapper;
import kreadcn.homework.dto.AdminDTO;
import kreadcn.homework.dto.AdminModDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.*;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.utils.AdminUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.AdminVO;
import kreadcn.homework.vo.ModuleVO;
import kreadcn.homework.vo.PrivilegeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminPrivMapper adminPrivMapper;

    @Autowired
    private AdminLabMapper adminLabMapper;

    @Autowired
    private LabMapper labMapper;

    @Override
    public List<ModuleVO> listModules() {
        List<ModuleVO> moduleVOList = new ArrayList<>();
        Map<String, Object> serviceBeansMap = applicationContext.getBeansWithAnnotation(BackendModule.class);
        for (Object bean : serviceBeansMap.values()) {
            Class<?> cls = bean.getClass();
            BackendModule moduleAnnotation = AnnotationUtils.findAnnotation(cls, BackendModule.class);
            if (moduleAnnotation == null) {
                continue;
            }

            String className = AdminUtils.getModuleName(cls);
            ModuleVO moduleVO = new ModuleVO();
            moduleVO.setId(className);
            moduleVO.setPrivilegeList(Arrays.stream(moduleAnnotation.value()).map(item -> {
                PrivilegeVO privilegeVO = new PrivilegeVO();
                String[] pairs = item.split(":");
                Assert.isTrue(pairs.length == 2, cls.getName() + "privileges error");
                privilegeVO.setId(pairs[0]);
                privilegeVO.setDescription(pairs[1]);
                return privilegeVO;
            }).collect(Collectors.toList()));

            moduleVOList.add(moduleVO);
        }

        return moduleVOList;
    }

    @Override
    public Map<Integer, String> getNameMap(Set<Integer> adminIds) {
        if (CollectionUtils.isEmpty(adminIds)) {
            return Collections.emptyMap();
        }

        return adminMapper.listByUserIds(new ArrayList<>(adminIds))
                .stream()
                .collect(Collectors.toMap(Admin::getId, Admin::getName));
    }

    @Override
    public Page<AdminVO> list(KeywordQueryDTO queryDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        if (queryDTO == null) {
            queryDTO = new KeywordQueryDTO();
        } else {
            FormatUtils.trimFieldToNull(queryDTO);
        }

        int year;
        queryDTO.setKeyword(FormatUtils.makeFuzzySearchTerm(queryDTO.getKeyword()));
        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));
        Calendar cal = Calendar.getInstance();

        Integer total = adminMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), total);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }

        List<Admin> list = adminMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        Set<Integer> adminIds = list.stream().map(Admin::getUpdatedBy).collect(Collectors.toSet());
        adminIds.addAll(list.stream().map(Admin::getCreatedBy).collect(Collectors.toSet()));
        Map<Integer, String> nameMap = getNameMap(adminIds);

        List<AdminVO> voList = list.stream()
                .map(item -> AdminUtils.convertToBriefVO(item, nameMap))
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public AdminDTO getDetail(Integer id) {
        Assert.notNull(id, "id不能为空");
        Admin admin = adminMapper.selectByPrimaryKey(id);
        Assert.notNull(admin, "未找到管理员: " + id);
        List<AdminPriv> privList = adminPrivMapper.list(id);
        List<AdminLab> labList = adminLabMapper.selectByAdminId(id);

        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        adminDTO.setPassword(null);
        Map<String, List<String>> modMap = new HashMap<>();
        privList.forEach(item -> {
            List<String> list = modMap.computeIfAbsent(item.getModId(), k -> new ArrayList<>());
            list.add(item.getPriv());
        });

        List<AdminModDTO> modDTOList = new ArrayList<>();
        modMap.entrySet().forEach(entry -> {
            AdminModDTO modDTO = new AdminModDTO();
            modDTO.setId(entry.getKey());
            modDTO.setPrivList(entry.getValue());
            modDTOList.add(modDTO);
        });

        List<Integer> labIdList = new ArrayList<>();
        for (AdminLab adminLab : labList) {
            labIdList.add(adminLab.getLabId());
        }

        adminDTO.setModList(modDTOList);
        adminDTO.setLabIds(labIdList);
        return adminDTO;
    }

    @Override
    @Transactional
    public Integer update(AdminDTO adminDTO) {
        AdminUtils.validate(adminDTO);
        Assert.notNull(adminDTO.getId(), "管理员id不能为空");
        Admin admin = adminMapper.selectByPrimaryKey(adminDTO.getId());
        Assert.notNull(admin, "未找到管理员，id=" + adminDTO.getId());

        CurrentUser token = ThreadContextHolder.getCurrentUser();
        if ("root".equalsIgnoreCase(adminDTO.getUserCode())) {
            Assert.isTrue("root".equalsIgnoreCase(token.getUserCode()), "只有root用户可以修改root信息");
        }

        BeanUtils.copyProperties(adminDTO, admin);
        admin.setUpdatedBy(token.getAdminId());
        adminMapper.updateByPrimaryKey(admin);
        List<Integer> ids = new ArrayList<>();
        ids.add(admin.getId());
        adminPrivMapper.deleteByAdminIds(ids);
        adminLabMapper.deleteByAdminIds(ids);
        updateOtherInfo(adminDTO);
        return admin.getId();
    }

    @Override
    @Transactional
    public Integer add(AdminDTO adminDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        AdminUtils.validate(adminDTO);
        Assert.hasText(adminDTO.getPassword(), "密码不能为空");
        Admin admin = adminMapper.getByUserCode(adminDTO.getUserCode());
        Assert.isNull(admin, "该用户ID已经存在，请另外指定一个");
        admin = new Admin();
        BeanUtils.copyProperties(adminDTO, admin);
        admin.setUpdatedBy(token.getAdminId());
        admin.setCreatedBy(token.getAdminId());
        adminMapper.insert(admin);
        adminDTO.setId((admin.getId()));
        updateOtherInfo(adminDTO);
        return admin.getId();
    }

    @Override
    public Integer delete(List<Integer> ids) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.notEmpty(ids, "删除列表不能为空");
        int size = adminMapper.delete(ids);
        adminPrivMapper.deleteByAdminIds(ids);
        adminLabMapper.deleteByAdminIds(ids);
        return size;
    }

    private void updateOtherInfo(AdminDTO adminDTO) {
        if ("root".equalsIgnoreCase(adminDTO.getUserCode())) {
            return;
        }

        CurrentUser token = ThreadContextHolder.getCurrentUser();

        if (CollectionUtils.isEmpty(adminDTO.getModList())) {
            return;
        }

        for (AdminModDTO modDTO : adminDTO.getModList()) {
            for (String priv : modDTO.getPrivList()) {
                AdminPriv adminPriv = new AdminPriv();
                adminPriv.setAdminId(adminDTO.getId());
                adminPriv.setModId(modDTO.getId());
                adminPriv.setPriv(priv);
                adminPrivMapper.insert(adminPriv);
            }
        }

        if (CollectionUtils.isEmpty(adminDTO.getLabIds())) {
            return;
        }

        for (Integer labId : adminDTO.getLabIds()) {
            Lab lab = labMapper.selectByPrimaryKey(labId);
            Assert.notNull(lab, "实验室不存在，id=" + labId);
            AdminLab adminLab = new AdminLab();
            adminLab.setAdminId(adminDTO.getId());
            adminLab.setLabId(labId);
            adminLab.setCreatedBy(token.getAdminId());
            adminLabMapper.insert(adminLab);
        }
    }
}
