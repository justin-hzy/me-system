package com.me.modules.mabang.job;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@Slf4j
@AllArgsConstructor
public class MaBangJob {

    MaBangTransService maBangTransService;


    @Scheduled(cron = "0 9/3 * * * *")
    void transMaBangOrder(){

        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayStr = today.format(formatter);

        String expressTimeStart = todayStr+" 00:00:00";
        String expressTimeEnd = todayStr+" 23:59:59";

        boolean isNext = true;

        String cursor = null;

        while (isNext){
            JSONObject returnJSON = maBangTransService.transMaBangOrder(expressTimeStart,expressTimeEnd,cursor);
            String hasNext = returnJSON.getString("hasNext");
            if("true".equals(hasNext)){
                isNext = true;
                cursor = returnJSON.getString("nextCursor");
            }else if("false".equals(hasNext)){
                isNext = false;
            }
        }
    }

    @Scheduled(cron = "0 0 * * ?")
    void transYdayMaBangOrder(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yesterdayStr = yesterday.format(formatter);

        String expressTimeStart = yesterdayStr+" 00:00:00";
        String expressTimeEnd = yesterdayStr+" 23:59:59";

        boolean isNext = true;

        String cursor = null;

        while (isNext){
            JSONObject returnJSON = maBangTransService.transMaBangOrder(expressTimeStart,expressTimeEnd,cursor);
            String hasNext = returnJSON.getString("hasNext");
            if("true".equals(hasNext)){
                isNext = true;
                cursor = returnJSON.getString("nextCursor");
            }else if("false".equals(hasNext)){
                isNext = false;
            }
        }
    }


}
