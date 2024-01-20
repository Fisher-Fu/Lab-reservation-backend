package kreadcn.homework.service.utils;

import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.vo.OnlineUserVO;
import org.springframework.beans.BeanUtils;

public class TokenUtils {
    public static OnlineUserVO convertToVO(CurrentUser token) {
        OnlineUserVO vo = new OnlineUserVO();
        BeanUtils.copyProperties(token, vo);
        return vo;
    }
}
