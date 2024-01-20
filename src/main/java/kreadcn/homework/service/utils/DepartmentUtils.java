package kreadcn.homework.service.utils;

import kreadcn.homework.dto.DepartmentDTO;
import kreadcn.homework.model.Department;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.vo.DepartmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/3 9:35
 */
public class DepartmentUtils {
    /**
     * 规范并校验departmentDTO
     *
     * @param departmentDTO
     */
    public static void validateDepartment(DepartmentDTO departmentDTO) {
        FormatUtils.trimFieldToNull(departmentDTO);
        Assert.notNull(departmentDTO, "部门输入数据不能为空");
        Assert.hasText(departmentDTO.getDepartmentName(), "部门名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param department 实体对象
     * @param nameMap
     * @return VO对象
     */
    public static DepartmentVO convertToVO(Department department, Map<Integer, String> nameMap) {
        DepartmentVO departmentVO = new DepartmentVO();
        BeanUtils.copyProperties(department, departmentVO);

        departmentVO.setCreatedByDesc(nameMap.get(department.getCreatedBy()));
        return departmentVO;
    }
}
