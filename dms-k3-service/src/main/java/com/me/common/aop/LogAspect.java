package com.me.common.aop;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.me.common.core.JsonResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogAspect {

    private static String MDC_TRACE_ID = "traceId";
    private static final Snowflake snowflake = IdUtil.createSnowflake(1, 2);

    /**
     * 切点
     */
    @Pointcut("execution(public * com.me.modules..controller.*.*(..))")
    public void requestLog() {
        //point cut
    }

    /**
     * 切点方法执行前执行此方法
     *
     * @param joinPoint joinPoint
     */
    @Before(value = "requestLog()")
    public void beforeRun(JoinPoint joinPoint) {
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取执行方法名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        //获取方法参数值
        final Object[] argValues = joinPoint.getArgs();
        //获取方法说明
        String description = "";
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }
        String traceid = MDC.get(MDC_TRACE_ID);
        if (StrUtil.isBlank(traceid)) {
            String traceId = UUID.randomUUID().toString();
            traceId = traceId.replace("-","");
            MDC.put(MDC_TRACE_ID,traceId);
        }
        //获取执行方法名
        // update by PiaO 20210820 判断是定时任务 不打印日志
        if(!StrUtil.equals(methodName,"quartzMonitor")) {
            log.info("START->[{}]: Class[{}] Method[{}] Params[{}]", description, className, methodName, JSONUtil.toJsonStr(argValues));
        }
        //打印日志
    }


    /**
     * 切点方法执行后执行此方法
     *
     * @param joinPoint joinPoint
     * @param ret       ret
     */
    @AfterReturning(returning = "ret", pointcut = "requestLog()")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        //获取方法说明
        String description = "";
        ApiOperation annotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class);
        if (Objects.nonNull(annotation)) {
            description = annotation.value();
        }
        //获取请求结果
        JsonResult<Object> jsonResult = Convert.convert(new TypeReference<JsonResult<Object>>() {
        }, ret);
        //打印日志
        if (Objects.isNull(jsonResult)) {
            jsonResult = JsonResult.ok();
        }
        //获取执行方法名
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        // update by PiaO 20210820 判断是首页图片或者是验证码时 不打印日志
        if(!(StrUtil.equals(methodName,"showall") ||StrUtil.equals(methodName,"getGraphImg") || StrUtil.equals(methodName,"quartzMonitor"))) {
            log.info("ENDED->[{}]: ReturnCode[{}] ReturnMsg[{}] Data[{}]", description, jsonResult.getCode(), jsonResult.getMessage(), jsonResult.getData());
        }
    }
}
