package com.me.mabang.reorder;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MBRefundTest {

    @Autowired
    MaBangTransService maBangTransService;

    @Test
    void transMaBangRefund(){

        String createDateStart = "2024-11-17 00:00:00";
        String createDateEnd = "2024-11-26 23:59:59";


        Integer page = 1;

        boolean isNext = true;

        while (isNext){
            JSONObject returnJSON  = maBangTransService.transMaBangRefund(createDateStart,createDateEnd,page);

            isNext = returnJSON.getBoolean("isNext");
            Integer pageCount = returnJSON.getInteger("pageCount");
            page = returnJSON.getInteger("page");

            if(page>pageCount){
                isNext = false;
            }

        }
        //JSONObject returnJSON  = maBangTransService.transMaBangRefund(createDateStart,createDateEnd,page);
    }
}
