package com.me.modules.sale.in.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.annotation.Elk;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.in.service.FlashInOrderService;
import com.me.modules.sale.trans.TransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class FlashInOrderJob {


    private TransService transService;


    private FlashInOrderService flashInOrderService;

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void tramsInOrderDtl() throws IOException {


        log.info("执行flash入库回传");
        QueryWrapper<FlashInOrder> inOrderQuery = new QueryWrapper<>();
        inOrderQuery.eq("is_receive","1");

        List<FlashInOrder> flashInOrders = flashInOrderService.list(inOrderQuery);

        for (FlashInOrder flashInOrder : flashInOrders){
            transService.transInOrderList(flashInOrder);
        }

        log.info("flash入库回传执行完毕");

    }
}
