package kreadcn.homework.controller.common;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.NeedNoPrivilege;
import kreadcn.homework.service.utils.DictUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dict")
@BackendModule
public class DictController {
    @GetMapping("getAllDict")
    @NeedNoPrivilege
    public Map<String, Map<Integer, String>> getAllDict() {
        return DictUtils.getDictMap();
    }
}
