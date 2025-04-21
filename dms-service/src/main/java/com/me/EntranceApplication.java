package com.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

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
