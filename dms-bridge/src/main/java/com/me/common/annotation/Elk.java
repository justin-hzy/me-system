package com.me.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName Elk
 * @Description elk日志收集注解
 * @Author 梁臣
 * @Date 2020/5/10 11:43
 * @Version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Elk {
}
