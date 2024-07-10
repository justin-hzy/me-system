package com.me;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.me.common.utils.TraceIdBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */

@EnableAsync
@EnableCaching
//@EnableScheduling
@SpringBootApplication
public class EntranceApplication
{
    public static void main(String[] args) {
        SpringApplication.run(EntranceApplication.class, args);
    }

}
