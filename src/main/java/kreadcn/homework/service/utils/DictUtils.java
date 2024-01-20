package kreadcn.homework.service.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DictUtils {
    public static final String SERVER_TYPE = "server_type";
    private static Map<String, Map<Integer, String>> dictMap = null;

    public static void init(Map<String, Map<Integer, String>> map) {
        dictMap = map;
    }

    public static Map<String, Map<Integer, String>> getDictMap() {
        return dictMap;
    }

    public static Map<Integer, String> getDict(String dictType) {
        Map<Integer, String> map = dictMap.get(dictType);
        if (map == null) {
            log.error("Cannot find dict type: " + dictType);
            return null;
        }
        return map;
    }

    public static String getCodeDesc(String dictType, Integer code) {
        Map<Integer, String> map = getDict(dictType);
        return map != null ? map.get(code) : null;
    }

    public static Integer getCodeByDesc(String dictType, String desc) {
        Map<Integer, String> map = getDict(dictType);
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(desc)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
