package kreadcn.homework.service;

import kreadcn.homework.dto.LabDTO;
import kreadcn.homework.dto.query.LabQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.LabVO;

import java.util.List;

/**
 * 实验室模块服务接口
 *
 * @author He.Y
 * @date 2023-10-20
 */
public interface LabService {
    Page<LabVO> listByPage(LabQueryDTO queryDTO);

    Integer addLab(LabDTO labDTO);

    int deleteByIds(List<Integer> labIds);

    /**
     * 更新实验室数据
     *
     * @param labDTO lab输入对象
     * @return lab编码(id)
     */
    Integer updateLab(LabDTO labDTO);
}
