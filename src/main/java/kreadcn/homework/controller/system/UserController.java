package kreadcn.homework.controller.system;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.dto.UserDTO;
import kreadcn.homework.dto.query.UserQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.service.UserService;
import kreadcn.homework.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/3/17
 */
@RestController
@RequestMapping("/api/user")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("list")
    @Privilege("page")
    public Page<UserVO> listUser(@RequestBody UserQueryDTO queryDTO) {
        return userService.listByPage(queryDTO);
    }

    @GetMapping("get")
    @Privilege("page")
    public UserDTO getUser(Integer id) {
        return userService.getDetail(id);
    }

    @PostMapping("add")
    @Privilege("add")
    public Integer addUser(@RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @PostMapping("update")
    @Privilege("update")
    public Integer updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }


    @PostMapping("delete")
    @Privilege("delete")
    public Integer deleteUser(@RequestBody List<Integer> ids) {
        return userService.delete(ids);
    }

    @PostMapping("resetPassword")
    @Privilege("update")
    public Boolean resetPassword(Integer id) {
        userService.resetPassword(id);
        return true;
    }
}
