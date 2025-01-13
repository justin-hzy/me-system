package com.me.trf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.DmsConfig;
import com.me.common.config.SfConfig;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.service.SaleOrderService;
import com.me.modules.trf.entity.ThTrfOrder;
import com.me.modules.trf.service.ThTrfOrderService;
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
    ThTrfOrderService thTrfOrderService;

    @Autowired
    PurOrderService purOrderService;

    @Autowired
    private SaleOrderService service;

    @Autowired
    private DmsConfig dmsConfig;

    @Test
    void queryTrfOutDtl() throws IOException {
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1").isNotNull("in_company_code");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            ThRefund thRefund = new ThRefund();
            thRefund.setErpOrder(thTrfOrder.getErpOrder());
            thRefund.setIsReceive(thTrfOrder.getIsReceive());
            thRefund.setCompanyCode(thTrfOrder.getInCompanyCode());
            thRefund.setRequestId(thTrfOrder.getRequestId());

            purOrderService.transPurOrderDtl(thRefund,dmsConfig.getTrfInDt3());
        }
    }

    @Test
    void queryTrfInDtl() throws IOException {
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_send","1").isNotNull("out_company_code");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            ThSaleOrder thSaleOrder = new ThSaleOrder();
            thSaleOrder.setErpOrder(thTrfOrder.getErpOrder());
            thSaleOrder.setIsSend(thTrfOrder.getIsSend());
            thSaleOrder.setCompanyCode(thTrfOrder.getOutCompanyCode());
            thSaleOrder.setRequestId(thTrfOrder.getRequestId());
            service.transSaleOrderDtl(thSaleOrder,dmsConfig.getTrfOutDt2());
        }
    }
}
