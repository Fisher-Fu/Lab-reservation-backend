package kreadcn.homework.config;

import kreadcn.homework.annotation.BackendModule;
import kreadcn.homework.annotation.NeedNoPrivilege;
import kreadcn.homework.annotation.Privilege;
import kreadcn.homework.model.CurrentUser;
import kreadcn.homework.service.utils.AdminUtils;
import kreadcn.homework.utils.ThreadContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 描述：对API接口的请求，除了login外，都做token检查，没有token则报错
 *
 * @author lihongwen
 * @date 2020/3/9
 */
@Component
@Aspect
@Slf4j
public class TokenCheckAspect {
    @Before("execution(* kreadcn.homework.controller.system..*Controller.*(..))")
    public void processLog(JoinPoint joinPoint) throws Exception {
        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();

        Method method = clazz.getMethod(methodName, parameterTypes);
        if (method.getAnnotation(NeedNoPrivilege.class) != null) {
            return;
        }

        if ("login".equals(joinPoint.getSignature().getName())) {
            return;
        }


        Privilege privilege = method.getAnnotation(Privilege.class);
        if (privilege != null && privilege.value().length == 0) {
            // @Privilege表示只要登录了就能访问，不需要特别权限
            ThreadContextHolder.getCurrentUser();
            return;
        }

        CurrentUser token = ThreadContextHolder.getCurrentUser();
        Assert.notNull(token, "您未登录，请重新登录");
        if ("ping".equals(joinPoint.getSignature().getName())) {
            return;
        }

        if ("root".equalsIgnoreCase(token.getUserCode())) {
            return;
        }

        BackendModule moduleAnnotation = (BackendModule) clazz.getAnnotation(BackendModule.class);
        Assert.notNull(moduleAnnotation, "访问的类没有Module注解");

        String className = AdminUtils.getModuleName(clazz);
        Assert.notNull(privilege, "方法没有privilege注解，不能访问：" + className + '.' + methodName);
        String[] privs = privilege.value();
        for (String s : privs) {
            if (token.getPrivSet().contains(className + '.' + s)) {
                return;
            }
        }

        throw new RuntimeException("您没有权限执行此操作");
    }
}
