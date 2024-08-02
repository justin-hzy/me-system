package com.me.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.order.service.*;
import com.me.nascent.modules.order.entity.*;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;

import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.trade.PromotionsVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.OrdersVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradeByModifySgFinishInfo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradesVo;
import com.nascent.ecrp.opensdk.request.trade.TradeSynRequest;
import com.nascent.ecrp.opensdk.response.trade.TradeSynResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TradeTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;


    @Autowired
    TransOrderService transOrderService;


    @Test
    public void getOrder() throws Exception {


        String startDateStr  = "2022-07-01 00:00:00";

        //定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(startDateStr, formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime sevenDaysLater = startDateTime.plusDays(7);

        // 将 LocalDateTime 转换为 Date
        Date sevenDaysLaterDate = Date.from(sevenDaysLater.atZone(ZoneId.systemDefault()).toInstant());
        Long nextId = 0L;

        /*while (true){

        }*/

        Boolean flag = true;
        while (flag){
            /*log.info("nextId="+nextId);
            log.info("modifyTime="+modifyTime);
            log.info("isNext="+isNext);*/

            Map<String,Object> resMap  = transOrderService.transOrder(nextId,startDate,sevenDaysLaterDate);
            nextId = (Long) resMap.get("nextId");
            startDate = (Date) resMap.get("modifyTime");
            Boolean isNext = (Boolean) resMap.get("isNext");

            log.info("nextId="+nextId);
            log.info("modifyTime="+startDate);
            log.info("isNext="+isNext);

            if(isNext == false){
                startDateTime = startDateTime.plusDays(7);
                startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());

                sevenDaysLater = startDateTime.plusDays(7);
                sevenDaysLaterDate = Date.from(sevenDaysLater.atZone(ZoneId.systemDefault()).toInstant());
                log.info(startDateTime.toString());
                log.info(sevenDaysLaterDate.toString());
            }

            Thread.sleep(12000);
        }
    }


    @Test
    public void putOrder(){
        transOrderService.putOrder();
    }

}
