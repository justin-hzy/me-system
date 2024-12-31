package com.me.pur;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.in.service.FlashInOrderService;
import com.me.modules.sale.trans.TransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class InOrderTest {

    @Autowired
    TransService transService;

    @Autowired
    FlashInOrderService flashInOrderService;

    @Test
    public void tramsInOrderDtlTest() throws IOException {

        QueryWrapper<FlashInOrder> inOrderQuery = new QueryWrapper<>();
        inOrderQuery.eq("is_receive","1");

        List<FlashInOrder> flashInOrders = flashInOrderService.list(inOrderQuery);

        for (FlashInOrder flashInOrder : flashInOrders){
            transService.transInOrderList(flashInOrder);
        }



    }
}
