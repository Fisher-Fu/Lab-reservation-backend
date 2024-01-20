package kreadcn.homework.service;


import kreadcn.homework.dto.HomeworkDTO;
import kreadcn.homework.dto.query.KeywordQueryDTO;
import kreadcn.homework.model.Page;
import kreadcn.homework.vo.HomeworkDetailVO;
import kreadcn.homework.vo.HomeworkVO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 部门模块服务接口
 *
 * @author 李洪文
 * @date 2019-12-3
 */
public interface HomeworkService {
    Page<HomeworkVO> listByPage(KeywordQueryDTO queryDTO);

    /**
     * 更新部门数据
     *
     * @return 部门编码
     */

    Integer add(HomeworkDTO homeworkDTO);

    Integer update(HomeworkDTO homeworkDTO);

    int deleteByIds(List<Integer> ids);

    HomeworkDetailVO getDetail(Integer id);

    void downloadZip(Integer homeworkId, OutputStream outputStream) throws IOException;

    void finishHomework(int id);
}
