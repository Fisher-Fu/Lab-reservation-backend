package kreadcn.homework.service;

import kreadcn.homework.vo.HomeworkVO;
import kreadcn.homework.vo.UserHomeworkDetailVO;

import java.io.File;
import java.util.List;

public interface UserHomeworkService {
    List<HomeworkVO> listUserHomework();

    UserHomeworkDetailVO getDetail(Integer homeworkId);

    int uploadFile(Integer homeworkId, String fileName, File file, String ipAddress) throws Exception;

    int deleteFiles(List<Integer> ids);

}
