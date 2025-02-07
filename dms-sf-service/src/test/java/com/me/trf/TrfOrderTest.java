package com.me.trf;

import cn.hutool.core.util.StrUtil;
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
    private SaleOrderService saleOrderService;

    @Autowired
    private DmsConfig dmsConfig;

    @Test
    void queryConsOutDtl() throws IOException {
        /*dbxzpd : 调拨类型 寄售出*/
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_send","1").eq("dbxzpd","1");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            if (StrUtil.isNotEmpty(thTrfOrder.getErpOrder())){
                ThSaleOrder thSaleOrder = new ThSaleOrder();
                thSaleOrder.setErpOrder(thTrfOrder.getErpOrder());
                thSaleOrder.setIsSend(thTrfOrder.getIsSend());
                thSaleOrder.setCompanyCode(thTrfOrder.getOutCompanyCode());
                thSaleOrder.setRequestId(thTrfOrder.getRequestId());
                saleOrderService.transSaleOrderDtl(thSaleOrder,dmsConfig.getTrfOutDt2());
            }
        }
    }

    @Test
    void queryConsInDtl() throws IOException {
        /*dbxzpd : 调拨类型 寄售退*/
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1").eq("dbxzpd","2");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            if (StrUtil.isNotEmpty(thTrfOrder.getErpOrder())){
                ThRefund thRefund = new ThRefund();
                thRefund.setErpOrder(thTrfOrder.getErpOrder());
                thRefund.setIsReceive(thTrfOrder.getIsReceive());
                thRefund.setCompanyCode(thTrfOrder.getInCompanyCode());
                thRefund.setRequestId(thTrfOrder.getRequestId());
                purOrderService.transPurOrderDtl(thRefund,dmsConfig.getTrfInDt3());
            }
        }
    }

    /*@Test
    void queryTrfOutDtl() throws IOException{
        *//*dbxzpd : 调拨类型*//*
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_send","1").eq("dbxzpd","3");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            if (StrUtil.isNotEmpty(thTrfOrder.getErpOrder())){
                ThSaleOrder thSaleOrder = new ThSaleOrder();
                thSaleOrder.setErpOrder(thTrfOrder.getErpOrder());
                thSaleOrder.setIsSend(thSaleOrder.getIsSend());
                thSaleOrder.setCompanyCode(thTrfOrder.getOutCompanyCode());
                thSaleOrder.setRequestId(thTrfOrder.getRequestId());
                saleOrderService.transSaleOrderDtl(thSaleOrder,dmsConfig.getTrfOutDt2());
            }
        }
    }*/

    @Test
    void queryTrfInDtl() throws IOException{
        /*dbxzpd : 调拨类型 仓间调拨*/
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1").eq("dbxzpd","3");
        List<ThTrfOrder> thTrfOutOrders = thTrfOrderService.list(queryWrapper);

        for (ThTrfOrder thTrfOrder : thTrfOutOrders){
            if (StrUtil.isNotEmpty(thTrfOrder.getErpOrder())){
                ThRefund thRefund = new ThRefund();
                thRefund.setErpOrder(thTrfOrder.getErpOrder());
                thRefund.setIsReceive(thRefund.getIsReceive());
                thRefund.setCompanyCode(thTrfOrder.getInCompanyCode());
                thRefund.setRequestId(thTrfOrder.getRequestId());
                purOrderService.transPurOrderDtl(thRefund,dmsConfig.getTrfInDt3());
            }
        }
    }
}
