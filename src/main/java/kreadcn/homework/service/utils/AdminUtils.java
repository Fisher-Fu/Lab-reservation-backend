package kreadcn.homework.service.utils;

import kreadcn.homework.dto.AdminDTO;
import kreadcn.homework.model.Admin;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import kreadcn.homework.vo.AdminVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/4/8
 */
public class AdminUtils {
    public static AdminVO convertToBriefVO(Admin item, Map<Integer, String> nameMap) {
        AdminVO vo = new AdminVO();
        BeanUtils.copyProperties(item, vo);
        vo.setCreatedByDesc(nameMap.get(item.getCreatedBy()));
        vo.setUpdatedByDesc(nameMap.get(item.getUpdatedBy()));
        return vo;
    }

    public static void validate(AdminDTO adminDTO) {
        Assert.notNull(adminDTO, "管理员信息不能为空");
        String password = adminDTO.getPassword();
        FormatUtils.trimFieldToNull(adminDTO);
        if (FormatUtils.isEmpty(password)) {
            password = null;
        } else {
            password = FormatUtils.password(password);
        }

        adminDTO.setPassword(password);
        Assert.hasText(adminDTO.getUserCode(), "登录名不能为空");
        Assert.hasText(adminDTO.getName(), "姓名不能为空");
    }

    public static String getModuleName(Class<?> clazz) {
        String rawName = clazz.getSimpleName();
        int pos = rawName.indexOf("Controller");
        if (pos <= 0) {
            return rawName;
        }
        return rawName.substring(0, 1).toLowerCase() + rawName.substring(1, pos);
    }

    public static Integer getCurrentAdminId() {
        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Integer adminId = token.getAdminId();
        if ("root".equalsIgnoreCase(token.getUserCode())) {
            adminId = null;
        }
        return adminId;
    }
}
