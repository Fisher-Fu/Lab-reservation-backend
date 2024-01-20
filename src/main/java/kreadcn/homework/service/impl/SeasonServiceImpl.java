package kreadcn.homework.service.impl;

import kreadcn.homework.dao.SeasonMapper;
import kreadcn.homework.dto.SeasonDTO;
import kreadcn.homework.dto.query.SeasonQueryDTO;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.model.Page;
import kreadcn.homework.model.Season;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.service.SeasonService;
import kreadcn.homework.service.utils.SeasonUtils;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.PageUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.SeasonVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author He.Y
 * @description
 * @date 2023/10/24 15:55
 */
@Service
public class SeasonServiceImpl implements SeasonService {
    @Autowired
    private SeasonMapper seasonMapper;

    @Autowired
    private AdminService adminService;

    @Override
    public Integer addSeason(SeasonDTO seasonDTO) {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        SeasonUtils.validateSeason(seasonDTO);
        Season season = new Season();
        // 将输入的字段全部复制到实体对象中
        season.setSeasonName(seasonDTO.getSeasonName());
        season.setEnabled(seasonDTO.getEnabled());
        season.setStartedAt(seasonDTO.getStartedAt());
        season.setCreatedAt(new Date());
        season.setCreatedBy(token.getAdminId());
        season.setUpdatedAt(new Date());
        season.setUpdatedBy(token.getAdminId());
        BeanUtils.copyProperties(seasonDTO, season);
        // 调用DAO方法保存到数据库表
        seasonMapper.insert(season);
        return season.getId();
    }

    @Override
    public void deleteByIds(List<Integer> seasonIds) {
        Assert.notEmpty(seasonIds, "未能选中学期");
        seasonMapper.deleteByIds(seasonIds);
    }

    @Override
    public Integer updateSeason(SeasonDTO seasonDTO) {
        // 校验输入数据正确性
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        SeasonUtils.validateSeason(seasonDTO);
        Assert.notNull(seasonDTO.getId(), "学期id不能为空");
        Season season = seasonMapper.selectByPrimaryKey(seasonDTO.getId());
        Assert.notNull(season, "没有Id为" + seasonDTO.getId() + "的学期");

        BeanUtils.copyProperties(seasonDTO, season);
        season.setUpdatedBy(token.getAdminId());
        season.setUpdatedAt(new Date());
        seasonMapper.updateByPrimaryKey(season);
        return season.getId();
    }

    /**
     * 分页获取学期信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的学期数据列表
     */
    @Override
    public Page<SeasonVO> listByPage(SeasonQueryDTO queryDTO) {
        Assert.notNull(queryDTO, "请输入查询参数");
        queryDTO.setSeasonName(FormatUtils.makeFuzzySearchTerm(queryDTO.getSeasonName()));
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        //命中的查询条件数
        Integer size = seasonMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);
        Page<SeasonVO> data = new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), new ArrayList<>());

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Season> list = seasonMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Season::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Season::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<SeasonVO> voList = list.stream()
                .map(item -> SeasonUtils.convertToVO(item, nameMap))
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }
}
