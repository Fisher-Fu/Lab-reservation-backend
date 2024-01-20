package kreadcn.homework.controller.system;


import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.SeasonDTO;
import kreadcn.homework.dto.query.SeasonQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.SeasonService;
import kreadcn.homework.vo.SeasonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学期信息管理后端服务模块
 *
 * @author 何一凡
 * @description
 * @date 2023/10/24 15:53
 */
@RestController
@RequestMapping("/api/season")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class SeasonController {
    @Autowired
    private SeasonService seasonService;

    @PostMapping("addSeason")
    @Privilege("add")
    public Integer addSeason(@RequestBody SeasonDTO seasonDTO) {
        return seasonService.addSeason(seasonDTO);
    }

    @PostMapping("deleteSeason")
    @Privilege("delete")
    public void deleteSeason(@RequestBody List<Integer> seasonIds) {
        seasonService.deleteByIds(seasonIds);
    }

    @PostMapping("updateSeason")
    @Privilege("update")
    public Integer updateSeason(@RequestBody SeasonDTO seasonDTO) {
        return seasonService.updateSeason(seasonDTO);
    }

    @PostMapping("listSeason")
    @Privilege("page")
    public Page<SeasonVO> listSeason(@RequestBody SeasonQueryDTO queryDTO) {
        return seasonService.listByPage(queryDTO);
    }
}
