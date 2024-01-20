package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.DeleteDepartmentUserDTO;
import kreadcn.homework.dto.DepartmentDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.DepartmentService;
import kreadcn.homework.vo.DepartmentDetailVO;
import kreadcn.homework.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 部门管理后端服务模块
 *
 * @author 李洪文
 * @description
 * @date 2019/12/3 11:07
 */

@RestController
@RequestMapping("/api/department")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("listDepartment")
    @Privilege("page")
    public Page<DepartmentVO> listDepartment(@RequestBody KeywordQueryDTO queryDTO) {
        return departmentService.listByPage(queryDTO);
    }

    @PostMapping("updateDepartment")
    @Privilege("update")
    public Integer updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.updateDepartment(departmentDTO);
    }

    @GetMapping("getDepartment")
    @Privilege("page")
    public DepartmentDetailVO getDepartment(Integer id) {
        return departmentService.getDetail(id);
    }

    @PostMapping("deleteDepartment")
    @Privilege("delete")
    public void deleteDepartment(@RequestBody List<Integer> ids) {
        departmentService.deleteByCodes(ids);
    }

    @PostMapping("importDepartment")
    @Privilege("add")
    public int importDepartment(
            @RequestParam("departmentName") String departmentName,
            @RequestParam("contact") String contact,
            @RequestParam("contactPhone") String contactPhone,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) throws Exception {
        return departmentService.importDepartment(departmentName, contact, contactPhone, description, file.getOriginalFilename(), file.getInputStream());
    }

    @PostMapping("deleteDepartmentUser")
    @Privilege("delete")
    public int deleteDepartmentUser(@RequestBody DeleteDepartmentUserDTO delDTO) {
        return departmentService.deleteUser(delDTO);
    }

    @GetMapping("addDepartmentUser")
    @Privilege("add")
    public int addDepartmentUser(Integer departmentId, String userCode, String name) {
        return departmentService.addUser(departmentId, userCode, name);
    }
}
