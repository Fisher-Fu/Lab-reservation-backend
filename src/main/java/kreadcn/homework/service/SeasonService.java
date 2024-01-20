package kreadcn.homework.service;

import kreadcn.homework.dto.SeasonDTO;
import kreadcn.homework.dto.query.SeasonQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.SeasonVO;

import java.util.List;

/**
 * 学期模块服务接口
 *
 * @author He.Y
 * @date 2023-10-24
 */
public interface SeasonService {
    Integer addSeason(SeasonDTO seasonDTO);

    void deleteByIds(List<Integer> seasonIds);

    /**
     * 更新学期数据
     *
     * @return season编码(id)
     * @Param seasonDTO season输入对象
     */
    Integer updateSeason(SeasonDTO seasonDTO);

    Page<SeasonVO> listByPage(SeasonQueryDTO queryDTO);
}
