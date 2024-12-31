package com.me.modules.sale.trans.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@AllArgsConstructor
public class OrderJob {

//    @Elk
//    @Scheduled(cron = "0 */2 * * * *")
    public void transOutOrderList(){
        log.info("执行transOutOrderList");
    }
}
