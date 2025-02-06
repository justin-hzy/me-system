package com.me.modules.refund.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.aop.Mdc;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.refund.service.ThRefundService;
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
public class RefundJob {

    private PurOrderService purOrderService;

    private ThRefundService thRefundService;

    @Mdc
    @Scheduled(cron = "0 0 9-21/1 * * ?")
    void queryRefundDtl() throws IOException {

        log.info("执行退货入库订单已入库明细查询定时任务");
        QueryWrapper<ThRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1");
        List<ThRefund> refunds = thRefundService.list(queryWrapper);

        for (ThRefund refund : refunds){
            purOrderService.transPurOrderDtl(refund);
        }
    }
}
