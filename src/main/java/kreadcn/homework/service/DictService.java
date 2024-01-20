package kreadcn.homework.service;

import java.util.Map;

/**
 * 描述：
 *
 * @author yangqiang
 * @date 2018-12-28
 */
public interface DictService {

    /**
     * 获取所有字典类型的map
     *
     * @return 字典map
     */
    Map<String, Map<Integer, String>> getAllMap();
}
