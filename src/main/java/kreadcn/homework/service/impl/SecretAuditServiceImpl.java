package kreadcn.homework.service.impl;

import kreadcn.homework.model.AuditValue;
import kreadcn.homework.service.SecretAuditService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述：
 *
 * @author lihongwen
 * @date 2020/5/11
 */
@Service
public class SecretAuditServiceImpl implements SecretAuditService {
    @Value("${maxErrorLogin:5}")
    private int maxErrorLogin = 5;

    @Value("${auditInterval:2}")
    private int auditInterval = 2;

    /**
     * 用户审计Map，Key为userCode
     */
    private Map<String, List<AuditValue>> userAuditMap = new ConcurrentHashMap<>();

    /**
     * IP审计Map， Key为IP
     */
    private Map<String, List<AuditValue>> ipAuditMap = new ConcurrentHashMap<>();

    /**
     * 每当用户登陆失败的时候调用
     *
     * @param userCode  用户ID
     * @param ipAddress IP地址
     */
    @Override
    public void addErrorLogin(String userCode, String ipAddress) {
        if (userCode != null) {
            addToMap(userAuditMap, userCode);
        }
        addToMap(ipAuditMap, ipAddress);
    }


    /**
     * 判断用户ID或者ipaddress是否需要显示验证码
     *
     * @param userCode  用户ID
     * @param ipAddress IP地址
     * @return
     */
    @Override
    public void checkAbnormal(String userCode, String ipAddress) {
        List<AuditValue> history;
        if (userCode != null) {
            history = userAuditMap.get(userCode);
            if (getRecentRetryTimes(history) >= maxErrorLogin) {
                throw new RuntimeException("您的账户或IP发生了多次错误密码尝试，请" + auditInterval + "分钟后再试");
            }
        }


        history = ipAuditMap.get(ipAddress);
        if (getRecentRetryTimes(history) >= maxErrorLogin) {
            throw new RuntimeException("您的账户或IP发生了多次错误密码尝试，请" + auditInterval + "分钟后再试");
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void checkExpired() {
        checkMapExpired(userAuditMap, true);
        checkMapExpired(ipAuditMap, false);
    }

    private int getRecentRetryTimes(List<AuditValue> history) {
        if (history == null) {
            return 0;
        }

        int total = 0;
        for (AuditValue val : history) {
            if (getInterval(val) < auditInterval) {
                total += val.getErrorTimes();
            }
        }

        return total;
    }

    private void addToMap(Map<String, List<AuditValue>> map, String key) {
        AuditValue auditValue = null;
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        List<AuditValue> userHistory = map.computeIfAbsent(key, k -> new ArrayList<>());

        if (CollectionUtils.isEmpty(userHistory)) {
            auditValue = new AuditValue();
            auditValue.setMinute(minute);
            userHistory.add(auditValue);
        } else {
            auditValue = userHistory.get(userHistory.size() - 1);
            if (auditValue.getMinute() != minute) {
                auditValue = new AuditValue();
                auditValue.setMinute(minute);
                userHistory.add(auditValue);
            }
        }

        auditValue.setErrorTimes(auditValue.getErrorTimes() + 1);
    }

    private void checkMapExpired(Map<String, List<AuditValue>> map, boolean isUserCheck) {
        Iterator<Map.Entry<String, List<AuditValue>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<AuditValue>> entry = iterator.next();

            for (int i = entry.getValue().size() - 1; i >= 0; i--) {
                AuditValue val = entry.getValue().get(i);
                if (getInterval(val) >= auditInterval) {
                    entry.getValue().remove(i);
                    Calendar calendar = Calendar.getInstance();
                    Date now = new Date();
                    calendar.setTime(now);
                }
            }
            if (entry.getValue().size() == 0) {
                iterator.remove();
            }
        }
    }

    private int getInterval(AuditValue val) {
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        if (minute < val.getMinute()) {
            return minute + 60 - val.getMinute();
        } else {
            return minute - val.getMinute();
        }
    }
}
