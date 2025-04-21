package com.me.common.utils;

import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TraceIdBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 生成唯一的traceId
        String traceId = UUID.randomUUID().toString();

        // 将traceId设置到MDC中
        MDC.put("traceId", traceId);

        return bean;
    }
}
