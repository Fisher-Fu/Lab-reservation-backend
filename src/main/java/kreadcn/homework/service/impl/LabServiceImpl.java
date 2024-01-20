package kreadcn.homework.service.impl;

import kreadcn.homework.dao.AdminLabMapper;
import kreadcn.homework.dao.AdminMapper;
import kreadcn.homework.dao.LabMapper;
import kreadcn.homework.dto.LabDTO;
import kreadcn.homework.dto.query.LabQueryDTO;
import kreadcn.homework.model.*;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.LabService;
import kreadcn.homework.service.utils.LabUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.LabVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author He.Y
 * @description
 * @date 2023/10/20 15:26
 */
@Service
public class LabServiceImpl implements LabService {
    @Autowired
    private LabMapper labMapper;

    @Autowired
    private AdminLabMapper adminLabMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    /**
     * 分页获取实验室信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的实验室数据列表
     */
    @Override
    public Page<LabVO> listByPage(LabQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "请输入查询参数");
        queryDTO.setName(FormatUtils.makeFuzzySearchTerm(queryDTO.getName()));
        queryDTO.setAddress(FormatUtils.makeFuzzySearchTerm(queryDTO.getAddress()));
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //命中的查询条件数
        Integer size = labMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        Page<LabVO> data = new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), new ArrayList<>());

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Lab> list = labMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Lab::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Lab::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<LabVO> voList = list.stream()
                .map(item -> LabUtils.convertToVO(item, nameMap))
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override
    public Integer addLab(LabDTO labDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        LabUtils.validateLab(labDTO);
        Lab lab = new Lab();
        // 将输入的字段全部复制到实体对象中
        lab.setName(labDTO.getName());
        lab.setAddress(labDTO.getAddress());
        lab.setCapacity(labDTO.getCapacity());
        lab.setArea(labDTO.getArea());
        lab.setDescription(labDTO.getDescription());
        lab.setCreatedAt(new Date());
        lab.setUpdatedAt(new Date());
        lab.setCreatedBy(token.getAdminId());
        lab.setUpdatedBy(token.getAdminId());
        BeanUtils.copyProperties(labDTO, lab);
        // 调用DAO方法保存到数据库表
        labMapper.insert(lab);
        updateOtherInfo(labDTO);
        return lab.getId();
    }

    @Override
    public int deleteByIds(List<Integer> labIds) {
        Assert.notEmpty(labIds, "未能选中实验室");
        int result = labMapper.deleteByIds(labIds);
        //删除实验室的同时, 删除其所有关联信息
        adminLabMapper.deleteByLabIds(labIds);
        return result;
    }

    /**
     * 更新实验室数据
     *
     * @param labDTO 实验室输入对象
     * @return 实验室编码
     */
    @Override
    public Integer updateLab(LabDTO labDTO) {
        // 校验输入数据正确性
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        LabUtils.validateLab(labDTO);
        Assert.notNull(labDTO.getId(), "实验室id不能为空");
        Lab lab = labMapper.selectByPrimaryKey(labDTO.getId());
        Assert.notNull(lab, "没有Id为" + labDTO.getId() + "的实验室");

        BeanUtils.copyProperties(labDTO, lab);
        lab.setUpdatedBy(token.getAdminId());
        lab.setUpdatedAt(new Date());
        labMapper.updateByPrimaryKey(lab);
        List<Integer> ids = new ArrayList<>();
        ids.add(lab.getId());
        adminLabMapper.deleteByLabIds(ids);
        updateOtherInfo(labDTO);
        return lab.getId();
    }

    /**
     * 添加Lab-Admin关联信息
     *
     * @param labDTO
     */
    private void updateOtherInfo(LabDTO labDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        if (CollectionUtils.isEmpty(labDTO.getAdminIds())) {
            return;
        }
        for (Integer id : labDTO.getAdminIds()) {
            Admin admin = adminMapper.selectByPrimaryKey(id);
            Assert.notNull(admin, "不存在id为" + id + "的用户");
            AdminLab adminLab = new AdminLab();
            adminLab.setLabId(labDTO.getId());
            adminLab.setAdminId(id);
            adminLab.setCreatedBy(token.getAdminId());
            adminLabMapper.insert(adminLab);
        }
    }
}
