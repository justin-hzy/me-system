package com.me.common.aop;

import cn.hutool.core.util.IdUtil;
import com.me.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(1)
public class QuartzMdcAspect {
    private static final String UNIQUE_ID = "traceId";

    @Pointcut("@annotation(com.me.common.annotation.Elk)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) {
        MDC.put(UNIQUE_ID, IdUtil.fastSimpleUUID());
        // 执行方法
        Object result = new Object();
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BusinessException(throwable);
        }
        MDC.remove(UNIQUE_ID);
        return result;
    }
}
