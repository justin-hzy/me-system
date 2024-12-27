package com.me.pur;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.refund.service.ThRefundService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@Slf4j
public class PurOrderTest {

    @Autowired
    private PurOrderService purOrderService;

    @Autowired
    private ThRefundService thRefundService;

    @Test
    void putPurOrder(PutRefundPurDto dto) throws IOException {
        purOrderService.putPurOrder(dto);
    }

    @Test
    void queryPurOrderDtl() throws IOException{

        QueryWrapper<ThRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_receive","1");
        List<ThRefund> refunds = thRefundService.list(queryWrapper);

        for (ThRefund refund : refunds){
            purOrderService.transPurOrderDtl(refund);
        }
    }
}
