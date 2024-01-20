package kreadcn.homework.enums;

public enum CourseScheduleStatus {
    PENDING(0, "未审核"),
    /**
     * 已经结束
     */
    PASS(1, "通过"),
    REVISE(2, "驳回修改"),
    REJECT(3, "拒绝"),
    CANCEL(4, "已撤销");

    final private Integer value;
    final private String desc;

    CourseScheduleStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer type) {
        for (CourseScheduleStatus orderType : CourseScheduleStatus.values()) {
            if (orderType.value.equals(type)) {
                return orderType.desc;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
