package kreadcn.homework.dao;

import kreadcn.homework.dto.AllocatedScheduleDTO;
import kreadcn.homework.dto.query.CourseScheduleQueryDTO;
import kreadcn.homework.model.CourseSchedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseSchedule record);

    int insertSelective(CourseSchedule record);

    CourseSchedule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseSchedule record);

    int updateByPrimaryKey(CourseSchedule record);

    /**
     * 根据id列表批量删除预约申请
     *
     * @param courseScheduleIds id列表
     */
    int deleteByIds(@Param("courseScheduleIds") List<Integer> courseScheduleIds);

    /**
     * 根据查询条件获取命中个数
     * 不使用 @Param 注解
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(CourseScheduleQueryDTO queryDTO);

    /**
     * 根据查询条件获取实验室列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 实验室列表
     */
    List<CourseSchedule> list(
            @Param("queryDTO") CourseScheduleQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

//    /**
//     * 获取所有已通过审核的课程时间信息
//     *
//     * @param status 课程审核状态
//     * @return 包含三个字段(section, weekRange, weekDay)的列表
//     */
//    List<Map<String, Integer>> selectAllTimes(Integer status, Integer seasonId);

    /**
     * 获取某个学期已经通过审核的课程的详细信息
     *
     * @param status
     * @param seasonId
     * @return List<AllocatedScheduleDTO>
     */
    List<AllocatedScheduleDTO> getSchedules(Integer status, Integer seasonId);
}