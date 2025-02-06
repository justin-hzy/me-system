package com.me.common.aop;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author : PiaO
 * @version : 1.0
 * @date : 2020/05/08 10:17
 */
@Slf4j
@Aspect
@Component
@Order(1)
public class MdcAspect {

    private static final String UNIQUE_ID = "traceId";

    @Pointcut("@annotation(com.me.common.aop.Mdc)")
    public void logPointCut() {
        //ignore
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point){
        MDC.put(UNIQUE_ID, IdUtil.fastSimpleUUID());
        Object result = new Object();
        try {
            result = point.proceed();
        } catch (Throwable throwable) {
            log.info("异常message"+throwable.getMessage());
            //throw new BusinessException(throwable);
        }
        MDC.remove(UNIQUE_ID);
        return result;
    }
}