package kreadcn.homework.service.utils;

import kreadcn.homework.dto.LabDTO;
import kreadcn.homework.model.Lab;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.vo.LabVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author He.Y
 * @description
 * @date 2023/10/20 15:47
 */
public class LabUtils {
    /**
     * 规范并校验labDTO
     *
     * @param labDTO
     */
    public static void validateLab(LabDTO labDTO) {
        FormatUtils.trimFieldToNull(labDTO);
        Assert.notNull(labDTO, "实验室信息不能为空");
        Assert.hasText(labDTO.getName(), "实验室名称不能为空");
        Assert.hasText(labDTO.getAddress(), "实验室地址不能为空");
        Assert.notNull(labDTO.getCapacity(), "实验室最大容纳人数不能为空");
        Assert.notNull(labDTO.getArea(), "实验室面积不能为空");
        Assert.isTrue(labDTO.getCapacity() > 0, "最大容纳人数不合理");
        Assert.isTrue(labDTO.getArea() > 0, "面积不合理");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param lab     实体对象
     * @param nameMap
     * @return VO对象
     */
    public static LabVO convertToVO(Lab lab, Map<Integer, String> nameMap) {
        LabVO labVO = new LabVO();
        BeanUtils.copyProperties(lab, labVO);
        labVO.setCreatedByDesc(nameMap.get(lab.getCreatedBy()));
        return labVO;
    }
}
