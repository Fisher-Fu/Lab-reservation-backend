package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.AdminDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.AdminService;
import kreadcn.homework.vo.AdminVO;
import kreadcn.homework.vo.ModuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("listModules")
    @Privilege("page")
    public List<ModuleVO> listModules() {

        return adminService.listModules();
    }

    @PostMapping("list")
    @Privilege("page")
    public Page<AdminVO> listAdmin(@RequestBody KeywordQueryDTO queryDTO) {
        return adminService.list(queryDTO);
    }

    @GetMapping("get")
    @Privilege("page")
    public AdminDTO getAdmin(Integer id) {

        return adminService.getDetail(id);
    }

    @PostMapping("add")
    @Privilege("add")
    public Integer addAdmin(@RequestBody AdminDTO adminDTO) {

        return adminService.add(adminDTO);
    }

    @PostMapping("update")
    @Privilege("update")
    public Integer updateAdmin(@RequestBody AdminDTO adminDTO) {

        return adminService.update(adminDTO);
    }

    @PostMapping("delete")
    @Privilege("delete")
    public Integer deleteAdmin(@RequestBody List<Integer> ids) {
        return adminService.delete(ids);
    }
}
