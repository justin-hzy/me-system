package com.me.modules.trf.job;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.aop.Mdc;
import com.me.common.config.DmsConfig;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.service.SaleOrderService;
import com.me.modules.trf.entity.ThTrfOrder;
import com.me.modules.trf.service.ThTrfOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class ThaTrfJob {

    ThTrfOrderService thTrfOrderService;

    PurOrderService purOrderService;

    private SaleOrderService saleOrderService;

    private DmsConfig dmsConfig;

    @Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void queryConsOutDtl() throws IOException {
        log.info("执行寄售出货明细查询定时器");
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
        log.info("寄售出货明细查询定时器执行完毕");
    }

    @Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void queryConsInDtl() throws IOException {
        log.info("执行寄售退货明细查询定时器");
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
        log.info("寄售退货明细查询定时器执行完毕");
    }

    /*@Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void queryTrfOutDtl() throws IOException{
        //dbxzpd : 调拨类型
        QueryWrapper<ThTrfOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1").eq("dbxzpd","3");
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

    @Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void queryTrfInDtl() throws IOException{
        log.info("执行调拨入库明细查询定时器");
        //dbxzpd : 调拨类型 仓间调拨
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
        log.info("调拨入库明细查询定时器执行完毕");
    }
}
