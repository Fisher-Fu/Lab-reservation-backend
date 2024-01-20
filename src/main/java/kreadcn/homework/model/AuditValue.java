package kreadcn.homework.model;

import lombok.Data;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/5/11
 */
@Data
public class AuditValue {
    private int minute;
    private int errorTimes = 0;
}
