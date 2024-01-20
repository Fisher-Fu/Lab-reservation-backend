package kreadcn.homework.service.utils;

import kreadcn.homework.dto.AllocatedScheduleDTO;
import kreadcn.homework.dto.CourseScheduleDTO;
import kreadcn.homework.dto.ScheduleVerifyDTO;
import kreadcn.homework.enums.CourseScheduleStatus;
import kreadcn.homework.model.AllocSchedule;
import kreadcn.homework.model.CourseSchedule;
import kreadcn.homework.model.Lab;
import kreadcn.homework.utils.FormatUtils;
import kreadcn.homework.vo.CourseScheduleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CourseScheduleUtils {
    /**
     * 规范并校验CourseScheduleDTO
     *
     * @Param courseScheduleDTO
     */
    public static void validateCourseScheduleForTeacher(CourseScheduleDTO courseScheduleDTO) {
        Assert.notNull(courseScheduleDTO, "预约信息不能为空");
        FormatUtils.trimFieldToNull(courseScheduleDTO);
        Assert.hasText(courseScheduleDTO.getCourseName(), "实验课课程名称不能为空");
        Assert.notEmpty(courseScheduleDTO.getSection(), "上课课节不能为空");
        Assert.notEmpty(courseScheduleDTO.getWeekRange(), "上课周不能为空");
        Assert.notEmpty(courseScheduleDTO.getWeekDay(), "上课星期不能为空");
        Assert.notNull(courseScheduleDTO.getStudentCount(), "上课人数不能为空");
        Assert.notNull(courseScheduleDTO.getRoomCount(), "所需实验室数不能为空");
        Assert.isTrue(courseScheduleDTO.getStudentCount() > 0, "上课人数不合理");
        Assert.isTrue(courseScheduleDTO.getRoomCount() > 0, "所需实验室数不合理");
    }

    public static void validateCourseScheduleForTechnician(ScheduleVerifyDTO scheduleVerifyDTO) {
        Assert.hasText(scheduleVerifyDTO.getReason(), "请提供审核通过或未通过的原因");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param courseSchedule 实体对象
     * @param nameMap
     * @return VO对象
     */
    public static CourseScheduleVO convertToVO(CourseSchedule courseSchedule, Map<Integer, String> nameMap) {
        CourseScheduleVO courseScheduleVO = new CourseScheduleVO();
        BeanUtils.copyProperties(courseSchedule, courseScheduleVO);
        courseScheduleVO.setCreatedByDesc(nameMap.get(courseSchedule.getCreatedBy()));
        return courseScheduleVO;
    }

    /**
     * 校验"更新"操作输入合法性
     *
     * @param courseSchedule
     */
    public static void validateUpdateCourseSchedule(CourseSchedule courseSchedule) {
        Assert.isTrue(CourseScheduleStatus.REVISE.getValue().equals(courseSchedule.getStatus()),
                "当前状态无法修改, 请联系实验员或管理员");
    }

    /**
     * 校验"撤销"操作输入合法性
     *
     * @param courseSchedule
     * @date 2023/11/18
     */
    public static void validateCancelCourseSchedule(CourseSchedule courseSchedule) {
        Assert.isTrue(CourseScheduleStatus.PENDING.getValue().equals(courseSchedule.getStatus()) ||
                        CourseScheduleStatus.REVISE.getValue().equals(courseSchedule.getStatus()),
                "该申请不能撤销!");
    }

    /**
     * 判断申请是否处于可审核的状态
     *
     * @param courseSchedule
     */
    public static void validateVerifySchedule(CourseSchedule courseSchedule) {
        Assert.isTrue(CourseScheduleStatus.PENDING.getValue().equals(courseSchedule.getStatus()) ||
                        CourseScheduleStatus.REVISE.getValue().equals(courseSchedule.getStatus()),
                "该申请无法进行审核!");
    }

    /**
     * 将整数转为列表 位运算
     *
     * @param value 待转化的整数
     * @return List<Integer> 整数列表
     */
    public static List<Integer> integersToList(Integer value) {
        return IntStream.range(0, 32)
                .filter(i -> ((value >> i) & 1) == 1)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * 获取已通过审核的申请的明细
     *
     * @param allocList 包含预约申请信息的列表
     * @param allLabs   包含所有实验室信息的列表
     * @return 包含所有实验室分配信息的 Map, 其中包括以预约申请id为键的映射和以实验室id为键的映射
     */
    public static Map<Integer, AllocSchedule> getAllAllocScheduleMap(List<AllocatedScheduleDTO> allocList, List<Lab> allLabs) {
        //Map key: (schedule)id
        Map<Integer, AllocatedScheduleDTO> allocScheduleMap = new HashMap<>();
        //Map key: labId
        Map<Integer, AllocSchedule> map = new HashMap<>();
        //执行完下述代码, 获得一个Map<scheduleId, AllocatedScheduleDTO>, 一个Map<labId, AllocSchedule>
        for (AllocatedScheduleDTO alloc : allocList) {
            //获取预约申请的id
            int scheduleId = alloc.getId();
            // 如果allocScheduleMap中不包含该请求id, 将其放入allocScheduleMap
            if (!allocScheduleMap.containsKey(scheduleId)) {
                AllocatedScheduleDTO allocatedScheduleDTO = new AllocatedScheduleDTO();
                allocatedScheduleDTO.setId(alloc.getId());
                allocatedScheduleDTO.setLabId(alloc.getLabId());
                allocatedScheduleDTO.setLab(getLabById(alloc.getLabId(), allLabs));
                allocatedScheduleDTO.setSection(alloc.getSection());
                allocatedScheduleDTO.setWeekRange(alloc.getWeekRange());
                allocatedScheduleDTO.setWeekDay(alloc.getWeekDay());
                allocatedScheduleDTO.setCreatedBy(alloc.getCreatedBy());
                allocatedScheduleDTO.setCourseName(alloc.getCourseName());

                allocScheduleMap.put(scheduleId, allocatedScheduleDTO);
            }
            //获取当前 labId 对应的 AllocSchedule 对象(申请)
            AllocSchedule schedule = map.get(alloc.getLabId());
            //未获取到申请, 则创建新的 AllocSchedule 对象, 并为其设置一个空的 HashMap
            if (schedule == null) {
                schedule = new AllocSchedule();
                schedule.setLabId(alloc.getLabId());
                schedule.setLabName(getLabById(alloc.getLabId(), allLabs).getName());
                schedule.setAllocMap(new HashMap<>());
            }
            //三层嵌套循环获取选中申请明细
            for (int i = 0; i < integersToList(alloc.getWeekRange()).size(); i++) {
                for (int j = 0; j < integersToList(alloc.getWeekDay()).size(); j++) {
                    for (int k = 0; k < integersToList(alloc.getSection()).size(); k++) {
                        //key:"周.星期.节"
                        String key = integersToList(alloc.getWeekRange()).get(i) + "."
                                + integersToList(alloc.getWeekDay()).get(j) + "."
                                + integersToList(alloc.getSection()).get(k);
                        schedule.getAllocMap().put(key, alloc);
                    }
                }
            }

            for (Lab lab : allLabs) {
                // 当前 lab 不是当前 schedule 对象对应的实验室, 跳过当前循环
                if (lab.getId() != schedule.getLabId()) continue;
                if (!map.containsKey(lab.getId())) {
                    //如果当前lab是schedule分配的实验室, 若 map 不包含当前 labId 的条目, 直接给 schedule 赋予 labId 作为 key
                    map.put(lab.getId(), schedule);
                } else {
                    //如果labId已经有对应申请记录, 假如申请记录的内容不同则说明这是一个不同的申请, 因此添加
                    if (map.get(lab.getId()).getAllocMap() != schedule.getAllocMap()) {
                        map.put(lab.getId(), schedule);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 根据labId获取Lab对象
     */
    public static Lab getLabById(Integer labId, List<Lab> allLabs) {
        return allLabs.stream()
                .filter(lab -> lab.getId().equals(labId))
                .findFirst()
                .orElse(null);
    }

    /**
     * 教师新建申请时获取时间不冲突的实验室的数量
     *
     * @param map      <labId, AllocSchedule(labId, labName, Map<"周次.星期.课节", AllocatedScheduleDTO类>)>
     * @param schedule 新的申请
     * @return
     */
    public static Integer countAvailableLabForTeacher(Map<Integer, AllocSchedule> map, CourseScheduleDTO schedule, List<Lab> allLabs) {
        Integer count = 0;

        for (Lab lab : allLabs) {
            //如果遍历到的某一个实验室人数不满足需求, 跳过该实验室
            if (lab.getCapacity() < schedule.getStudentCount() / schedule.getRoomCount()) {
                continue;
            }

            boolean found = true;
            AllocSchedule alloc = map.get(lab.getId());
            if (alloc != null) {
                for (int i = 0; i < schedule.getWeekRange().size(); i++) {
                    for (int j = 0; j < schedule.getWeekDay().size(); j++) {
                        for (int k = 0; k < schedule.getSection().size(); k++) {
                            // 获取新申请的key
                            String key = schedule.getWeekRange().get(i) + "."
                                    + schedule.getWeekDay().get(j) + "."
                                    + schedule.getSection().get(k);
                            // 检查是否已经存在
                            if (alloc.getAllocMap().containsKey(key)) {
                                // key已存在, 说明申请时间有冲突
                                found = false;
                                break;
                            }
                        }
                        if (!found) break;
                    }
                    if (!found) break;
                }
            }
            if (found) count++;
            if (count >= schedule.getRoomCount()) break;
        }
        return count;
    }

    /**
     * 检验实验员分配的实验室是否符合要求
     *
     * @param map      <labId, AllocSchedule(labId, labName, Map<"周次.星期.课节", AllocatedScheduleDTO类>)>
     * @param schedule
     * @param labIds
     * @param allLabs
     */
    public static void checkLabs(Map<Integer, AllocSchedule> map, CourseSchedule schedule, List<Integer> labIds, List<Lab> allLabs) {
        for (Integer labId : labIds) {
            Assert.isTrue(getLabById(labId, allLabs).getCapacity() >= schedule.getStudentCount() / schedule.getRoomCount(),
                    "您分配的实验室人数不符合对方要求!");

            AllocSchedule alloc = map.get(labId);
            //如果alloc为null则一定不会冲突, 因此不做判断; 只在alloc非空时进行判断
            if (alloc != null) {
                for (int i = 0; i < integersToList(schedule.getWeekRange()).size(); i++) {
                    for (int j = 0; j < integersToList(schedule.getWeekDay()).size(); j++) {
                        for (int k = 0; k < integersToList(schedule.getSection()).size(); k++) {
                            // 获取新申请的key
                            String key = integersToList(schedule.getWeekRange()).get(i) + "."
                                    + integersToList(schedule.getWeekDay()).get(j) + "."
                                    + integersToList(schedule.getSection()).get(k);
                            // 检查是否已经存在
                            Assert.isTrue(alloc.getAllocMap().containsValue(key), "您提供的id为" + labId + "的实验室在当前时间已被占用");
                        }
                    }
                }
            }
        }
    }
}
