package kreadcn.homework.service;


import kreadcn.homework.dto.DeleteDepartmentUserDTO;
import kreadcn.homework.dto.DepartmentDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.DepartmentDetailVO;
import kreadcn.homework.vo.DepartmentVO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门模块服务接口
 *
 * @author 李洪文
 * @date 2019-12-3
 */
public interface DepartmentService {
    Page<DepartmentVO> listByPage(KeywordQueryDTO queryDTO);

    /**
     * 更新部门数据
     *
     * @param departmentDTO 部门输入对象
     * @return 部门编码
     */
    Integer updateDepartment(DepartmentDTO departmentDTO);

    /**
     * 根据编码列表，批量删除部门
     *
     * @param ids 编码列表
     */
    void deleteByCodes(List<Integer> ids);

    Map<Integer, String> getNameMap(Set<Integer> adminIds);

    int importDepartment(String departmentName, String contact, String contactPhone, String description, String fileName, InputStream inputStream) throws Exception;

    /**
     * @param id 班级id
     * @return 该班级学生列表
     */
    DepartmentDetailVO getDetail(Integer id);

    /**
     * 删除班级学生关联
     *
     * @param ids
     * @return
     */
    int deleteUser(DeleteDepartmentUserDTO delDTO);

    int addUser(Integer departmentId, String userCode, String name);
}
