package com.me.order;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.service.OrderService;
import com.me.nascent.modules.order.entity.*;
import com.me.nascent.modules.order.service.NickService;
import com.me.nascent.modules.order.service.PromotionService;
import com.me.nascent.modules.order.service.SgFinishInfoService;
import com.me.nascent.modules.order.service.TradeService;
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

@SpringBootTest
@Slf4j
public class TradeTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private NickService nickService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SgFinishInfoService sgFinishInfoService;


    @Test
    public void getOrder() throws Exception {

        TradeSynRequest request = new TradeSynRequest();

        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse("2022-07-01 00:00:00", formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime endDateTime = LocalDateTime.parse("2022-07-06 00:00:00", formatter);
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setNextId(0L);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        TradeSynResponse response = client.execute(request);
        log.info(response.getBody());
        List<TradesVo> tradesVos = response.getResult();

        List<Trade> trades = new ArrayList<>();


        for(TradesVo tradesVo : tradesVos){
            Long id = tradesVo.getId();
            Trade trade = new Trade();
            BeanUtils.copyProperties(tradesVo, trade);
            trades.add(trade);


            List<NickInfo> nickInfos = tradesVo.getNickInfoList();
            List<Nick> nicks = new ArrayList<>();
            for (NickInfo nickInfo : nickInfos){
                Nick nick = new Nick();
                BeanUtils.copyProperties(nickInfo, nick);
                nick.setMainid(id);
                nicks.add(nick);
            }
            //nickService.saveBatch(nicks);

            List<PromotionsVo> promotionsVos = tradesVo.getPromotionVos();

            if(promotionsVos != null){
                List<Promotion> promotions = new ArrayList<>();

                for (PromotionsVo promotionsVo : promotionsVos){
                    Promotion promotion = new Promotion();
                    BeanUtils.copyProperties(promotionsVo,promotion);
                    promotions.add(promotion);
                }
                //promotionService.saveBatch(promotions);
            }

            List<OrdersVo> ordersVos = tradesVo.getOrders();
            List<Order> orders = new ArrayList<>();

            if(ordersVos != null){
                for (OrdersVo ordersVo : ordersVos){
                    Order order = new Order();
                    BeanUtils.copyProperties(ordersVo,order);
                    orders.add(order);


                    List<TradeByModifySgFinishInfo> tradeByModifySgFinishInfos = ordersVo.getSgFinishInfoList();
                    List<SgFinishInfo> sgFinishInfos = new ArrayList<>();
                    if (tradeByModifySgFinishInfos != null){
                        for (TradeByModifySgFinishInfo tradeByModifySgFinishInfo : tradeByModifySgFinishInfos){
                            SgFinishInfo sgFinishInfo = new SgFinishInfo();
                            BeanUtils.copyProperties(tradeByModifySgFinishInfo,sgFinishInfo);
                            sgFinishInfos.add(sgFinishInfo);
                        }
                        sgFinishInfoService.saveBatch(sgFinishInfos);
                    }
                }
                //orderService.saveBatch(orders);
            }
        }
        log.info(trades.toString());

        //orderService.saveBatch(orders);
    }
}
