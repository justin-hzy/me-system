package com.me.modules.sale.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.aop.Mdc;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.service.SaleOrderService;
import com.me.modules.sale.service.ThSaleOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class SaleOrderJob {

    private SaleOrderService service;

    private ThSaleOrderService thSaleOrderService;

    @Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void querySaleOrderDtl() throws IOException {

        log.info("执行销售订单已出库明细查询定时任务");
        QueryWrapper<ThSaleOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_send","1");
        List<ThSaleOrder> thSaleOrders = thSaleOrderService.list(queryWrapper);

        for (ThSaleOrder thSaleOrder : thSaleOrders){
            service.transSaleOrderDtl(thSaleOrder);
        }
    }


}
