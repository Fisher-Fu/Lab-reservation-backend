package kreadcn.homework.service;

import kreadcn.homework.dto.AdminDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.AdminVO;
import kreadcn.homework.vo.ModuleVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AdminService {
    /**
     * 获取所有的模块列表
     *
     * @return 所有的模块列表
     */
    List<ModuleVO> listModules();

    Map<Integer, String> getNameMap(Set<Integer> adminIds);

    Page<AdminVO> list(KeywordQueryDTO queryDTO);

    AdminDTO getDetail(Integer id);

    Integer update(AdminDTO adminDTO);

    Integer add(AdminDTO adminDTO);

    Integer delete(List<Integer> ids);
}
