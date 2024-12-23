package com.me.mabang.order;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
@Slf4j
public class MBOrderTest {

    @Autowired
    MaBangTransService maBangTransService;

    @Test
    void transMaBangOrder(){

        /*String expressTimeStart = "2024-12-23 00:00:00";
        String expressTimeEnd = "2024-12-23 23:59:59";*/

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
        //JSONObject returnJSON = maBangTransService.transMaBangOrder(createDateStart,createDateEnd,cursor);
    }
}
