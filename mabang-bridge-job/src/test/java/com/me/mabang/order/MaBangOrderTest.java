package com.me.mabang.order;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MaBangOrderTest {

    @Autowired
    MaBangTransService maBangTransService;

    @Test
    void transMaBangOrder(){

        String createDateStart = "2024-10-13 00:00:00";
        String createDateEnd = "2024-10-13 23:59:59";

        boolean isNext = true;

        String cursor = null;

        while (isNext){
            JSONObject returnJSON = maBangTransService.transMaBangOrder(createDateStart,createDateEnd,cursor);
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
