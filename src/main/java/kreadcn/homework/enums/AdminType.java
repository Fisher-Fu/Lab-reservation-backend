package kreadcn.homework.enums;

public enum AdminType {
    TEACHER(1, "教师"),
    TECHNICIAN(2, "实验员"),
    ADMINISTRATOR(3, "系统管理员");

    final private Integer value;
    final private String desc;

    AdminType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer type) {
        for (AdminType orderType : AdminType.values()) {
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
