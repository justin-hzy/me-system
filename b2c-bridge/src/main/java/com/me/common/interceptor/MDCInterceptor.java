package com.me.common.interceptor;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.me.common.exception.BusinessException;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MDCInterceptor extends HandlerInterceptorAdapter {
    private static String MDC_TRACE_ID = "traceId";
    private static final Snowflake snowflake = IdUtil.createSnowflake(1, 2);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        MDC.put(MDC_TRACE_ID, snowflake.nextIdStr());
        try {
            return super.preHandle(request, response, handler);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        MDC.remove(MDC_TRACE_ID);
        try {
            super.afterCompletion(request, response, handler, ex);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            super.afterConcurrentHandlingStarted(request, response, handler);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
}
