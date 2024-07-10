package com.me.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /*@ExceptionHandler(ArithmeticException.class)
    public void handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        //bot.send(e,request);
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
    }*/

}
