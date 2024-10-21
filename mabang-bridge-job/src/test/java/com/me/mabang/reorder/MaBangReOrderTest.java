package com.me.mabang.reorder;

import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MaBangReOrderTest {

    @Autowired
    MaBangTransService maBangTransService;

    @Test
    void transMaBangReOrder(){

        String createDateStart = "2024-10-13 00:00:00";
        String createDateEnd = "2024-10-13 23:59:59";

        boolean isNext = true;

        String cursor = null;

        maBangTransService.transMaBangReOrder(createDateStart,createDateEnd,cursor);
    }
}
