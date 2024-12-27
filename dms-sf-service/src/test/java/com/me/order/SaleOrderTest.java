package com.me.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.service.SaleOrderService;
import com.me.modules.sale.service.ThSaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class SaleOrderTest {

    @Autowired
    private SaleOrderService service;

    @Autowired
    private ThSaleOrderService thSaleOrderService;

    @Test
    void putSaleOrder() throws IOException {
        PutSaleOrderDto putSaleOrderDto = new PutSaleOrderDto();
        service.putSaleOrder(putSaleOrderDto);
    }

    @Test
    void querySaleOrderDtl() throws IOException {

        QueryWrapper<ThSaleOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_send","1");
        List<ThSaleOrder> thSaleOrders = thSaleOrderService.list(queryWrapper);

        for (ThSaleOrder thSaleOrder : thSaleOrders){
            service.transSaleOrderDtl(thSaleOrder);
        }
    }
}
