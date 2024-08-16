package com.me.nascent.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.order.entity.*;
import com.me.nascent.modules.order.service.*;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.trade.OrderDetailVo;
import com.nascent.ecrp.opensdk.domain.trade.PromotionDetailVo;
import com.nascent.ecrp.opensdk.domain.trade.PromotionsVo;
import com.nascent.ecrp.opensdk.domain.trade.TradeDetailVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.OrdersVo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradeByModifySgFinishInfo;
import com.nascent.ecrp.opensdk.domain.trade.tradeByModifyTime.TradesVo;
import com.nascent.ecrp.opensdk.request.trade.TradeSaveRequest;
import com.nascent.ecrp.opensdk.request.trade.TradeSynRequest;
import com.nascent.ecrp.opensdk.response.trade.TradeSaveResponse;
import com.nascent.ecrp.opensdk.response.trade.TradeSynResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransOrderServiceImpl implements TransOrderService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private TradeService tradeService;

    private NickService nickService;

    private PromotionService promotionService;

    private OrderService orderService;

    private SgFinishInfoService sgFinishInfoService;

    @Override
    public Map<String,Object> transOrder(Long nextId,Date startDate,Date endDate) throws Exception {

        Map resMap = new HashMap();
        Date modifyTime = null;
        Boolean isNext = false;
        TradeSynRequest request = new TradeSynRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());


        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setNextId(nextId);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        TradeSynResponse response = client.execute(request);
        //log.info(response.getBody());
        List<TradesVo> tradesVos = response.getResult();
        List<Trade> insertTrades = new ArrayList<>();
        List<Trade> updateTrades = new ArrayList<>();


        if(tradesVos.size()>0){
            isNext = true;
            for(TradesVo tradesVo : tradesVos){
                Long id = tradesVo.getId();
                nextId = id;
                modifyTime = tradesVo.getModifyTime();
                Trade trade = new Trade();
                BeanUtils.copyProperties(tradesVo, trade);

                QueryWrapper<Trade> tradeQuery = new QueryWrapper<>();
                tradeQuery.eq("id",id);
                Trade tradeResult = tradeService.getOne(tradeQuery);
                if(tradeResult!=null){
                    //进入更新集合
                    updateTrades.add(trade);
                }else {
                    insertTrades.add(trade);
                }


                List<NickInfo> nickInfos = tradesVo.getNickInfoList();
                List<Nick> nicks = new ArrayList<>();

                if(nickInfos.size()>0 || nickInfos !=null){
                    //清空明细表，重新载入
                    QueryWrapper<Nick> nickUpdate = new QueryWrapper<>();
                    nickUpdate.eq("mainid",id);
                    nickService.remove(nickUpdate);


                    for (NickInfo nickInfo : nickInfos){
                        Nick nick = new Nick();
                        BeanUtils.copyProperties(nickInfo, nick);
                        nick.setMainid(id);

                        nicks.add(nick);
                    }
                    nickService.saveBatch(nicks);
                }

                List<PromotionsVo> promotionsVos = tradesVo.getPromotionVos();

                if(promotionsVos != null){
                    List<Promotion> promotions = new ArrayList<>();
                    //清空明细表，重新载入
                    QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                    promotionQuery.eq("mainid",id);
                    promotionService.remove(promotionQuery);

                    for (PromotionsVo promotionsVo : promotionsVos){
                        Promotion promotion = new Promotion();
                        BeanUtils.copyProperties(promotionsVo,promotion);
                        promotion.setMainid(id);
                        promotions.add(promotion);
                    }
                    promotionService.saveBatch(promotions);
                }


                List<OrdersVo> ordersVos = tradesVo.getOrders();


                if(ordersVos.size()>0 || ordersVos != null){
                    List<Order> orders = new ArrayList<>();
                    //清空明细表，重新载入
                    QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                    orderQuery.eq("mainid",id);
                    orderService.remove(orderQuery);


                    for (OrdersVo ordersVo : ordersVos){
                        Order order = new Order();
                        BeanUtils.copyProperties(ordersVo,order);
                        order.setMainid(id);
                        orders.add(order);

                        List<TradeByModifySgFinishInfo> tradeByModifySgFinishInfos = ordersVo.getSgFinishInfoList();

                        if (tradeByModifySgFinishInfos.size()>0){
                            List<SgFinishInfo> sgFinishInfos = new ArrayList<>();
                            //清空明细表，重新载入
                            QueryWrapper<SgFinishInfo> sgFinishInfoQuery = new QueryWrapper<>();
                            sgFinishInfoQuery.eq("mainid",id);
                            sgFinishInfoService.remove(sgFinishInfoQuery);

                            for (TradeByModifySgFinishInfo tradeByModifySgFinishInfo : tradeByModifySgFinishInfos){
                                SgFinishInfo sgFinishInfo = new SgFinishInfo();
                                BeanUtils.copyProperties(tradeByModifySgFinishInfo,sgFinishInfo);
                                sgFinishInfos.add(sgFinishInfo);
                            }
                            sgFinishInfoService.saveBatch(sgFinishInfos);
                        }
                    }
                    orderService.saveBatch(orders);
                }
            }

            if(insertTrades.size()>0){
                log.info(insertTrades.toString());

                tradeService.saveBatch(insertTrades);
            }

            if(updateTrades.size()>0){
                log.info(updateTrades.toString());
                tradeService.saveOrUpdateBatch(updateTrades);
            }
        }

        resMap.put("nextId",nextId);
        resMap.put("modifyTime",modifyTime);
        resMap.put("isNext",isNext);
        return resMap;

    }

    @Override
    public void transOrder(Date startDate, Date endDate) throws Exception {

        Long nextId = 0L;
        boolean flag = true;
        while(flag){
            Map resMap = saveOrder(nextId,startDate,endDate);
            startDate = (Date) resMap.get("updateTime");
            nextId = (Long) resMap.get("nextId");
            flag = (boolean) resMap.get("isNext");

            log.info("startDate="+startDate);
            log.info("nextId="+nextId);
            log.info("flag="+flag);
        }
    }

    @Override
    public void putOrder() throws Exception {
        List<Trade> trades = tradeService.list();

        int batchSize = 100; // 每次处理的数据量
        int totalSize = trades.size(); // 总数据量
        //int totalSize = 200;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize);

            List<Trade> batchList = trades.subList(start,end);
            log.info("batchList="+batchList.toString());

            List<TradeDetailVo> tradeDetailVoList = new ArrayList<>();
            for (Trade trade : batchList){

                Long id = trade.getId();


                TradeDetailVo tradeDetailVo = new TradeDetailVo();
                BeanUtils.copyProperties(trade,tradeDetailVo);

                List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                orderQuery.eq("mainid",id);
                List<Order> existOrders = orderService.list(orderQuery);
                if(CollUtil.isNotEmpty(existOrders)){
                    for (Order order : existOrders){
                        OrderDetailVo orderDetailVo  = new OrderDetailVo();
                        BeanUtils.copyProperties(order,orderDetailVo);
                        orderDetailVos.add(orderDetailVo);
                    }
                    tradeDetailVo.setOrderDetailVoList(orderDetailVos);

                }

                List<PromotionDetailVo> promotionDetailVos = tradeDetailVo.getPromotionDetailVoList();

                QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                orderQuery.eq("mainid",id);
                List<Promotion> existPromotions = promotionService.list(promotionQuery);

                if (CollUtil.isNotEmpty(existPromotions)){
                    for (Promotion promotion : existPromotions){
                        PromotionDetailVo promotionDetailVo = new PromotionDetailVo();
                        BeanUtils.copyProperties(promotion,promotionDetailVo);
                        promotionDetailVos.add(promotionDetailVo);
                    }
                }

            }

            TradeSaveRequest request = new TradeSaveRequest();
            request.setServerUrl(nascentConfig.getServerUrl());
            request.setAppKey(nascentConfig.getAppKey());
            request.setAppSecret(nascentConfig.getAppSerect());
            request.setGroupId(nascentConfig.getGroupID());
            request.setAccessToken(tokenService.getToken());
            request.setTradeDetailVoList(tradeDetailVoList);

            ApiClient client = new ApiClientImpl(request);

            TradeSaveResponse response = client.execute(request);
            log.info("code="+response.getCode());
        }
    }


    private Map<String, Object> saveOrder(Long nextId,Date startDate, Date endDate) throws Exception {

        Map resMap = new HashMap();

        Date updateTime = null;
        Boolean isNext = false;
        TradeSynRequest request = new TradeSynRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());


        List<Token> tokens = tokenService.list();
        request.setAccessToken(tokens.get(0).getToken());

        request.setTimeType(1);

        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setNextId(nextId);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        TradeSynResponse response = client.execute(request);
        log.info(response.getBody());
        List<TradesVo> tradesVos = response.getResult();
        List<Trade> insertTrades = new ArrayList<>();
        List<Trade> updateTrades = new ArrayList<>();



        if("60001".equals(response.getCode())){
            UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
            tokenService.update(updateWrapper);
            resMap.put("nextId",nextId);
            resMap.put("updateTime",startDate);
            resMap.put("isNext",true);
        }else if("200".equals(response.getCode())){
            if(tradesVos.size()>0){
                isNext = true;
                for(TradesVo tradesVo : tradesVos){
                    Long id = tradesVo.getId();
                    nextId = id;
                    updateTime = tradesVo.getUpdateTime();
                    Trade trade = new Trade();
                    BeanUtils.copyProperties(tradesVo, trade);

                    QueryWrapper<Trade> tradeQuery = new QueryWrapper<>();
                    tradeQuery.eq("id",id);
                    Trade tradeResult = tradeService.getOne(tradeQuery);
                    if(tradeResult!=null){
                        //进入更新集合
                        updateTrades.add(trade);
                    }else {
                        insertTrades.add(trade);

                        List<NickInfo> nickInfos = tradesVo.getNickInfoList();


                        if(CollUtil.isNotEmpty(nickInfos)){
                            List<Nick> nicks = new ArrayList<>();

                            for (NickInfo nickInfo : nickInfos){
                                Nick nick = new Nick();
                                BeanUtils.copyProperties(nickInfo, nick);
                                nick.setMainid(id);

                                nicks.add(nick);
                            }
                            nickService.saveBatch(nicks);
                        }

                        List<PromotionsVo> promotionsVos = tradesVo.getPromotionVos();

                        if(CollUtil.isNotEmpty(promotionsVos)){
                            List<Promotion> promotions = new ArrayList<>();

                            for (PromotionsVo promotionsVo : promotionsVos){
                                Promotion promotion = new Promotion();
                                BeanUtils.copyProperties(promotionsVo,promotion);
                                promotion.setMainid(id);
                                promotions.add(promotion);
                            }
                            promotionService.saveBatch(promotions);
                        }


                        List<OrdersVo> ordersVos = tradesVo.getOrders();


                        if(CollUtil.isNotEmpty(ordersVos)){
                            List<Order> orders = new ArrayList<>();

                            for (OrdersVo ordersVo : ordersVos){
                                Order order = new Order();
                                BeanUtils.copyProperties(ordersVo,order);
                                order.setMainid(id);
                                orders.add(order);

                                List<TradeByModifySgFinishInfo> tradeByModifySgFinishInfos = ordersVo.getSgFinishInfoList();

                                if (tradeByModifySgFinishInfos.size()>0){
                                    List<SgFinishInfo> sgFinishInfos = new ArrayList<>();

                                    for (TradeByModifySgFinishInfo tradeByModifySgFinishInfo : tradeByModifySgFinishInfos){
                                        SgFinishInfo sgFinishInfo = new SgFinishInfo();
                                        BeanUtils.copyProperties(tradeByModifySgFinishInfo,sgFinishInfo);
                                        sgFinishInfos.add(sgFinishInfo);
                                    }
                                    sgFinishInfoService.saveBatch(sgFinishInfos);
                                }
                            }
                            orderService.saveBatch(orders);
                        }
                    }
                }

                if(insertTrades.size()>0){
                    //log.info(insertTrades.toString());
                    tradeService.saveBatch(insertTrades);
                }

                if(updateTrades.size()>0){
                    //log.info(updateTrades.toString());
                    tradeService.saveOrUpdateBatch(updateTrades);
                }
            }
            resMap.put("nextId",nextId);
            resMap.put("updateTime",updateTime);
            resMap.put("isNext",isNext);
        }
        return resMap;
    }
}
