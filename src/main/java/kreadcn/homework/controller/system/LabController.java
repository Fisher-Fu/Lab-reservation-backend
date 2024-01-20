package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.LabDTO;
import kreadcn.homework.dto.query.LabQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.LabService;
import kreadcn.homework.vo.LabVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 实验室信息管理后端服务模块
 *
 * @author 何一凡
 * @description
 * @date 2023/10/20 15:33
 */

@RestController
@RequestMapping("/api/lab")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class LabController {
    @Autowired
    private LabService labService;

    @PostMapping("listLab")
    @Privilege("page")
    public Page<LabVO> listLab(@RequestBody LabQueryDTO queryDTO) {
        return labService.listByPage(queryDTO);
    }

    @PostMapping("addLab")
    @Privilege("add")
    public Integer addLab(@RequestBody LabDTO labDTO) {
        return labService.addLab(labDTO);
    }

    @PostMapping("deleteLab")
    @Privilege("delete")
    public Integer deleteLab(@RequestBody List<Integer> labIds) {
        return labService.deleteByIds(labIds);
    }

    @PostMapping("updateLab")
    @Privilege("update")
    public Integer updateLab(@RequestBody LabDTO labDTO) {
        return labService.updateLab(labDTO);
    }
}
