package com.me.trf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.DmsConfig;
import com.me.modules.order.in.entity.FlashInOrder;
import com.me.modules.order.out.entity.FlashOutOrder;
import com.me.modules.order.trans.TransService;
import com.me.modules.trf.entity.TrfInOrder;
import com.me.modules.trf.entity.TrfOutOrder;
import com.me.modules.trf.service.TrfInOrderService;
import com.me.modules.trf.service.TrfOutOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class TrfOrderTest {

    @Autowired
    TrfInOrderService trfInOrderService;

    @Autowired
    TrfOutOrderService trfOutOrderService;

    @Autowired
    TransService transService;

    @Autowired
    DmsConfig dmsConfig;

    @Test
    public void transInTrfOrderDtl() throws IOException {

        String inTrfOderDt3 = dmsConfig.getInTrfOderDt3();


        QueryWrapper<TrfInOrder> trfInOrderQuery = new QueryWrapper<>();
        trfInOrderQuery.eq("is_receive",1);
        List<TrfInOrder> trfInOrders = trfInOrderService.list(trfInOrderQuery);
        for (TrfInOrder trfInOrder : trfInOrders){

            FlashInOrder flashInOrder = new FlashInOrder();

            flashInOrder.setOrderSn(trfInOrder.getOrderSn());
            flashInOrder.setIsReceive(trfInOrder.getIsReceive());
            flashInOrder.setRequestId(trfInOrder.getRequestId());
            flashInOrder.setStoreCode(trfInOrder.getInStoreCode());

            transService.transInTrfOrderList(flashInOrder,inTrfOderDt3);
        }
    }

    @Test
    public void transOutTrfOrderDtl() throws IOException {

        String outTrfOrderDtl2 = dmsConfig.getOutTrfOrderDtl2();


        QueryWrapper<TrfOutOrder> trfInOrderQuery = new QueryWrapper<>();
        trfInOrderQuery.eq("is_send",1);
        List<TrfOutOrder> trfInOrders = trfOutOrderService.list(trfInOrderQuery);

        for (TrfOutOrder trfOutOrder : trfInOrders){

            FlashOutOrder flashOutOrder = new FlashOutOrder();

            flashOutOrder.setOrderSn(trfOutOrder.getOrderSn());
            flashOutOrder.setIsSend(trfOutOrder.getIsSend());
            flashOutOrder.setRequestId(trfOutOrder.getRequestId());
            flashOutOrder.setStoreCode(trfOutOrder.getOutStoreCode());

            transService.transOutTrfOrderList(flashOutOrder,outTrfOrderDtl2);
        }
    }
}
