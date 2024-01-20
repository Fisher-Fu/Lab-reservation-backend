package kreadcn.homework.service.utils;

import kreadcn.homework.dto.SeasonDTO;
import kreadcn.homework.model.Season;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.vo.SeasonVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author He.Y
 * @description
 * @date 2023/10/25 20:55
 */
public class SeasonUtils {
    /**
     * 将实体对象转换为VO对象
     *
     * @param season  实体对象
     * @param nameMap
     * @return VO对象
     */
    public static SeasonVO convertToVO(Season season, Map<Integer, String> nameMap) {
        SeasonVO seasonVO = new SeasonVO();
        BeanUtils.copyProperties(season, seasonVO);

        seasonVO.setCreatedByDesc(nameMap.get(season.getCreatedBy()));
        return seasonVO;
    }

    /**
     * 规范并校验seasonDTO
     *
     * @param seasonDTO
     */
    public static void validateSeason(SeasonDTO seasonDTO) {
        FormatUtils.trimFieldToNull(seasonDTO);
        Assert.hasText(seasonDTO.getSeasonName(), "学期名称不能为空");
        Assert.notNull(seasonDTO.getEnabled(), "未指定是否可用！");
        Assert.notNull(seasonDTO.getStartedAt(), "学期开始日期不能为空");
    }
}
