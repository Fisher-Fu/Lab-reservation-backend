package kreadcn.homework.controller.system;

import jakarta.servlet.http.HttpServletResponse;
import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.HomeworkDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.HomeworkService;
import kreadcn.homework.vo.HomeworkDetailVO;
import kreadcn.homework.vo.HomeworkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 部门管理后端服务模块
 *
 * @author 李洪文
 * @description
 * @date 2019/12/3 11:07
 */

@RestController
@RequestMapping("/api/homework")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class HomeworkController {
    @Autowired
    private HomeworkService homeworkService;

    @PostMapping("listHomework")
    @Privilege("page")
    public Page<HomeworkVO> listHomework(@RequestBody KeywordQueryDTO queryDTO) {
        return homeworkService.listByPage(queryDTO);
    }

    @PostMapping("addHomework")
    @Privilege("add")
    public Integer addHomework(@RequestBody HomeworkDTO homeworkDTO) {
        return homeworkService.add(homeworkDTO);
    }

    @PostMapping("updateHomework")
    @Privilege("update")
    public Integer updateHomework(@RequestBody HomeworkDTO homeworkDTO) {
        return homeworkService.update(homeworkDTO);
    }

    @PostMapping("deleteHomework")
    @Privilege("delete")
    public int deleteHomework(@RequestBody List<Integer> ids) {
        return homeworkService.deleteByIds(ids);
    }

    @GetMapping("getAdminHomeworkDetail")
    @Privilege("page")
    public HomeworkDetailVO getAdminHomeworkDetail(Integer id) {
        return homeworkService.getDetail(id);
    }

    @GetMapping("downloadZip")
    @Privilege("page")
    public void downloadZip(Integer id, HttpServletResponse response) throws IOException {
        response.setContentType("application/x-msdownload");
        //暂时设定压缩下载后的文件名字为test.zip
        response.setHeader("Content-Disposition", "attachment;filename=files.zip");
        homeworkService.downloadZip(id, response.getOutputStream());
    }

    @GetMapping("finishHomework")
    @Privilege("update")
    public void finishHomework(Integer id) {
        homeworkService.finishHomework(id);
    }
}
