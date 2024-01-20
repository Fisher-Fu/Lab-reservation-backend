package kreadcn.homework.service.impl;

import jakarta.annotation.PostConstruct;
import kreadcn.homework.service.DictService;
import kreadcn.homework.service.utils.DictUtils;
import kreadcn.homework.dao.DictMapper;
import kreadcn.homework.model.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;

    @PostConstruct
    public void init() {
        DictUtils.init(getAllMap());
    }

    @Override
    public Map<String, Map<Integer, String>> getAllMap() {
        List<Dict> dictTypeList = dictMapper.listAll();
        HashMap<String, Map<Integer, String>> returnMap = new HashMap<>();

        dictTypeList.forEach(dictType -> {
            Map<Integer, String> map = returnMap.computeIfAbsent(dictType.getDictType(), k -> new HashMap<>());
            map.put(dictType.getDictCode(), dictType.getDictValue());
        });

        return returnMap;
    }
}
