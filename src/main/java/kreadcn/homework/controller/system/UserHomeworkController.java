package kreadcn.homework.controller.system;

import jakarta.servlet.http.HttpServletRequest;
import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.service.UserHomeworkService;
import kreadcn.homework.vo.HomeworkVO;
import kreadcn.homework.vo.UserHomeworkDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 部门管理后端服务模块
 *
 * @author 李洪文
 * @description
 * @date 2019/12/3 11:07
 */

@RestController
@RequestMapping("/api/userHomework")
@BackendModule({"page:页面"})
public class UserHomeworkController {
    @Autowired
    private UserHomeworkService userHomeworkService;

    @GetMapping("listHomework")
    @Privilege
    public List<HomeworkVO> listUserHomework() {
        return userHomeworkService.listUserHomework();
    }

    @GetMapping("getHomeworkDetail")
    @Privilege
    public UserHomeworkDetailVO getHomeworkDetail(Integer id) {
        return userHomeworkService.getDetail(id);
    }

    @PostMapping("uploadFile")
    @Privilege
    public int uploadFile(
            @RequestParam("homeworkId") Integer homeworkId,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {
        File tempFile = File.createTempFile("upload", ".tmp");
        org.apache.commons.io.FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
        return userHomeworkService.uploadFile(homeworkId, file.getOriginalFilename(), tempFile, request.getRemoteAddr());
    }

    @PostMapping("deleteFiles")
    @Privilege
    public int deleteFiles(@RequestBody List<Integer> ids) {
        return userHomeworkService.deleteFiles(ids);
    }

}
