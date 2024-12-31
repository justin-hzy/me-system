package com.me.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.sale.out.entity.FlashOutOrder;
import com.me.modules.sale.out.service.impl.FlashOutOrderService;
import com.me.modules.sale.trans.TransService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class OutOrderTest {

    @Autowired
    TransService transService;

    @Autowired
    FlashOutOrderService flashOutOrderService;

    @Test
    public void transOutOrderDtl() throws IOException {


        QueryWrapper<FlashOutOrder> flashOrderQuery = new QueryWrapper<>();
        flashOrderQuery.eq("is_send",1);

        List<FlashOutOrder> flashOutOrders = flashOutOrderService.list(flashOrderQuery);

        for (FlashOutOrder flashOutOrder : flashOutOrders){

            transService.transOutOrderDtl(flashOutOrder);

        }


    }
}
