package com.me.modules.order.out.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.annotation.Elk;
import com.me.modules.order.out.entity.FlashOutOrder;
import com.me.modules.order.out.service.impl.FlashOutOrderService;
import com.me.modules.order.trans.TransService;
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
public class FlashOrderJob {



    private TransService transService;


    private FlashOutOrderService flashOutOrderService;

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void transOutOrderDtl() throws IOException {

        log.info("执行flash出库回传");
        QueryWrapper<FlashOutOrder> flashOrderQuery = new QueryWrapper<>();
        flashOrderQuery.eq("is_send",1);

        List<FlashOutOrder> flashOutOrders = flashOutOrderService.list(flashOrderQuery);

        for (FlashOutOrder flashOutOrder : flashOutOrders){

            transService.transOutOrderDtl(flashOutOrder);
        }
        log.info("flash出库回传执行完毕");
    }
}
