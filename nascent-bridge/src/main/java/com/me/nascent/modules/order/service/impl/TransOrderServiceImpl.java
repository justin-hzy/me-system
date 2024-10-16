package com.me.nascent.modules.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.order.entity.*;
import com.me.nascent.modules.order.service.*;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.me.nascent.modules.trans.entity.TransBtnTradeFail;
import com.me.nascent.modules.trans.service.TransBtnTradeFailService;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private TransBtnTradeFailService transBtnTradeFailService;

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
    public void putTradeByOne() throws Exception {

        Map<Long,Long> storeIdMap = storeIdMap();

        QueryWrapper<Trade> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper
                //.likeRight("created","2021-06")
                //.in("shopId","100149660")
                .in("shopId","100149660","100150165","100149661","100150083","100149662","100150166","100149663","100156928")
                .in("outTradeId","1318866636481989787");

        List<Trade> trades = tradeService.list(shopQueryWrapper);

        HashMap<Long,List<Trade>> tradeHashMap = new LinkedHashMap<>();
        for (Trade trade : trades){
            Long shopId = trade.getShopId();
            if(tradeHashMap.containsKey(shopId)){
                List<Trade> tradeList = tradeHashMap.get(shopId);
                tradeList.add(trade);
            }else {
                List<Trade> tradeList = new ArrayList<>();
                tradeList.add(trade);
                tradeHashMap.put(shopId,tradeList);
            }
        }


        Set<Long> keys = tradeHashMap.keySet();
        for (long key : keys){
            List<Trade> list = tradeHashMap.get(key);
            int batchSize = 100; // 每次处理的数据量
            int totalSize = list.size(); // 总数据量
            //int totalSize = 1;
            int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

            for (int i = 0; i < loopCount; i++) {
                int start = i * batchSize; // 开始索引
                int end = Math.min((i + 1) * batchSize, totalSize);

                List<Trade> batchList = list.subList(start, end);
                //log.info("batchList=" + batchList.toString());

                List<TradeDetailVo> tradeDetailVoList = new ArrayList<>();
                for (Trade trade : batchList) {
                    Long id = trade.getId();
                    log.info("id="+id);

                    TradeDetailVo tradeDetailVo = new TradeDetailVo();
                    BeanUtils.copyProperties(trade,tradeDetailVo);

                    if(trade.getShippingType() == null){
                        tradeDetailVo.setShippingType("free");
                    }else {
                        tradeDetailVo.setShippingType(trade.getShippingType());
                    }
                    tradeDetailVo.setRefundStatus(0);
                    if(!"TRADE_CLOSED_BY_TAOBAO".equals(trade.getTradeStatus())){
                        tradeDetailVo.setPayTime(trade.getPayTime());
                    }

                    if("TRADE_CLOSED".equals(trade.getTradeStatus())){
                        tradeDetailVo.setPayTime(trade.getCreated());
                    }

                    tradeDetailVo.setIsCalIntegral(true);
                    tradeDetailVo.setRealPointFee(null);
                    tradeDetailVo.setSysCustomerId(null);
                    tradeDetailVo.setNasOuid(trade.getOutNick());
                    tradeDetailVo.setPlatform(null);
                    tradeDetailVo.setReceiverPhone("");
                    tradeDetailVo.setReceiverMobile("");
                    tradeDetailVo.setDiscountFee(trade.getDiscountFee().abs());

                    /*tradeDetailVo.setTotalFee(trade.getTotalFee());
                    tradeDetailVo.setPayment(trade.getPayment());
                    tradeDetailVo.setShippingType(trade.getShippingType());
                    tradeDetailVo.setOutTradeId(trade.getOutTradeId());
                    tradeDetailVo.setNasOuid(trade.getOutNick());
                    tradeDetailVo.setTradeStatus(trade.getTradeStatus());
                    tradeDetailVo.setTradeType(trade.getTradeType());
                    tradeDetailVo.setCreated(trade.getCreated());
                    tradeDetailVo.setNum(trade.getNum());
                    tradeDetailVo.setConsignTime(trade.getConsignTime());
                    tradeDetailVo.setPayType(trade.getPayType());
                    tradeDetailVo.setTradeFrom(trade.getTradeFrom());
                    tradeDetailVo.setEndTime(trade.getEndTime());*/


                    List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                    QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                    orderQuery.eq("mainid",id);
                    List<Order> existOrders = orderService.list(orderQuery);
                    if(CollUtil.isNotEmpty(existOrders)){
                        for (Order order : existOrders){
                            OrderDetailVo orderDetailVo  = new OrderDetailVo();
                            BeanUtils.copyProperties(order,orderDetailVo);
                            orderDetailVo.setOrderDiscountFee(orderDetailVo.getOrderDiscountFee().abs());
                            /*orderDetailVo.setOutOrderId(order.getOutOrderId());
                            orderDetailVo.setOrderStatus(order.getOrderStatus());
                            orderDetailVo.setTitle(order.getTitle());
                            orderDetailVo.setOutItemId(order.getOutItemId());
                            orderDetailVo.setOrderNum(order.getOrderNum());
                            orderDetailVo.setOrderTotalFee(order.getOrderTotalFee());
                            orderDetailVo.setOrderPayment(order.getOrderPayment());
                            BigDecimal bigDecimal_1 = new BigDecimal(order.getOrderPrice());
                            orderDetailVo.setOrderPrice(bigDecimal_1);*/
                            orderDetailVos.add(orderDetailVo);
                        }
                        tradeDetailVo.setOrderDetailVoList(orderDetailVos);
                    }

                    List<PromotionDetailVo> promotionDetailVos = new ArrayList<>();

                    QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                    promotionQuery.eq("mainid",id);
                    List<Promotion> existPromotions = promotionService.list(promotionQuery);

                    if (CollUtil.isNotEmpty(existPromotions)){
                        for (Promotion promotion : existPromotions){
                            PromotionDetailVo promotionDetailVo = new PromotionDetailVo();
                            BeanUtils.copyProperties(promotion,promotionDetailVo);
                            /*if(StrUtil.isEmpty(promotionDetailVo.getPromotionToolId())){
                                promotionDetailVo.setPromotionToolId("优惠卷");
                            }*/

                            promotionDetailVo.setPromotionToolId(null);

                            promotionDetailVos.add(promotionDetailVo);
                        }
                        tradeDetailVo.setPromotionDetailVoList(promotionDetailVos);
                    }

                    tradeDetailVoList.add(tradeDetailVo);
                }

                log.info(tradeDetailVoList.size()+"");
                TradeSaveRequest request = new TradeSaveRequest();
                request.setServerUrl(nascentConfig.getBtnServerUrl());
                request.setAppKey(nascentConfig.getBtnAppKey());
                request.setAppSecret(nascentConfig.getBtnAppSerect());
                request.setGroupId(nascentConfig.getBtnGroupID());

                QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
                tokenQuery.eq("name","btn");
                List<Token> tokens = tokenService.list(tokenQuery);

                request.setAccessToken(tokens.get(0).getToken());

                log.info("悦江shopid="+key);
                log.info("贝泰妮shopid="+storeIdMap.get(key));

                request.setShopId(storeIdMap.get(key));
                request.setTradeDetailVoList(tradeDetailVoList);

                ApiClient client = new ApiClientImpl(request);

                TradeSaveResponse response = client.execute(request);
                log.info(response.getCode());
                log.info(response.getMsg());
                if("200".equals(response.getCode())){
                    log.info(response.getBody());
                }else if("60001".equals(response.getCode()) || "103".equals(response.getCode())){
                    UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("name","btn").set("token",tokenService.getBtnToken());
                    tokenService.update(updateWrapper);

                    String ids = "";
                    for (int j = 0 ; j<batchList.size();j++){
                        Trade trade = batchList.get(j);
                        Long id = trade.getId();

                        if(j == (batchList.size()-1)){
                            ids = ids+String.valueOf(id);
                        }else {
                            ids = ids+String.valueOf(id)+",";
                        }
                    }

                    TransBtnTradeFail transBtnTradeFail = new TransBtnTradeFail();
                    transBtnTradeFail.setIds(ids);
                    transBtnTradeFail.setMessage(response.getBody());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
                else {
                    log.info(response.getBody());
                    String ids = "";
                    for (int j = 0 ; j<batchList.size();j++){
                        Trade trade = batchList.get(j);
                        Long id = trade.getId();

                        if(j == (batchList.size()-1)){
                            ids = ids+String.valueOf(id);
                        }else {
                            ids = ids+String.valueOf(id)+",";
                        }
                    }

                    TransBtnTradeFail transBtnTradeFail = new TransBtnTradeFail();
                    transBtnTradeFail.setIds(ids);
                    transBtnTradeFail.setMessage(response.getBody());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
            }
        }

    }

    @Override
    public void putTradeByRange() throws Exception {
        Map<Long,Long> storeIdMap = storeIdMap();

        QueryWrapper<Trade> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper
                .likeRight("created","2024-05")
                //.in("shopId","100149660")
                .in("shopId","100149660","100150165","100149661","100150083","100149662","100150166","100149663","100156928")
                .isNotNull("consignTime");
        List<Trade> trades = tradeService.list(shopQueryWrapper);

        HashMap<Long,List<Trade>> tradeHashMap = new LinkedHashMap<>();
        for (Trade trade : trades){
            Long shopId = trade.getShopId();
            if(tradeHashMap.containsKey(shopId)){
                List<Trade> tradeList = tradeHashMap.get(shopId);
                tradeList.add(trade);
            }else {
                List<Trade> tradeList = new ArrayList<>();
                tradeList.add(trade);
                tradeHashMap.put(shopId,tradeList);
            }
        }


        Set<Long> keys = tradeHashMap.keySet();
        for (long key : keys){
            List<Trade> list = tradeHashMap.get(key);
            int batchSize = 1; // 每次处理的数据量
            int totalSize = list.size(); // 总数据量
            //int totalSize = 1;
            int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

            for (int i = 0; i < loopCount; i++) {
                int start = i * batchSize; // 开始索引
                int end = Math.min((i + 1) * batchSize, totalSize);

                List<Trade> batchList = list.subList(start, end);
                //log.info("batchList=" + batchList.toString());

                List<TradeDetailVo> tradeDetailVoList = new ArrayList<>();
                for (Trade trade : batchList) {
                    Long id = trade.getId();
                    log.info("id="+id);

                    TradeDetailVo tradeDetailVo = new TradeDetailVo();
                    BeanUtils.copyProperties(trade,tradeDetailVo);

                    if(trade.getShippingType() == null){
                        tradeDetailVo.setShippingType("free");
                    }else {
                        tradeDetailVo.setShippingType(trade.getShippingType());
                    }
                    tradeDetailVo.setRefundStatus(0);
                    if(!"TRADE_CLOSED_BY_TAOBAO".equals(trade.getTradeStatus())){
                        tradeDetailVo.setPayTime(trade.getPayTime());
                    }
                    tradeDetailVo.setIsCalIntegral(true);
                    tradeDetailVo.setRealPointFee(null);
                    tradeDetailVo.setSysCustomerId(null);
                    tradeDetailVo.setNasOuid(trade.getOutNick());
                    tradeDetailVo.setPlatform(null);
                    tradeDetailVo.setReceiverPhone("");
                    tradeDetailVo.setReceiverMobile("");
                    tradeDetailVo.setDiscountFee(trade.getDiscountFee().abs());

                    /*tradeDetailVo.setTotalFee(trade.getTotalFee());
                    tradeDetailVo.setPayment(trade.getPayment());
                    tradeDetailVo.setShippingType(trade.getShippingType());
                    tradeDetailVo.setOutTradeId(trade.getOutTradeId());
                    tradeDetailVo.setNasOuid(trade.getOutNick());
                    tradeDetailVo.setTradeStatus(trade.getTradeStatus());
                    tradeDetailVo.setTradeType(trade.getTradeType());
                    tradeDetailVo.setCreated(trade.getCreated());
                    tradeDetailVo.setNum(trade.getNum());
                    tradeDetailVo.setConsignTime(trade.getConsignTime());
                    tradeDetailVo.setPayType(trade.getPayType());
                    tradeDetailVo.setTradeFrom(trade.getTradeFrom());
                    tradeDetailVo.setEndTime(trade.getEndTime());*/


                    List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                    QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                    orderQuery.eq("mainid",id);
                    List<Order> existOrders = orderService.list(orderQuery);
                    if(CollUtil.isNotEmpty(existOrders)){
                        for (Order order : existOrders){
                            OrderDetailVo orderDetailVo  = new OrderDetailVo();
                            BeanUtils.copyProperties(order,orderDetailVo);
                            orderDetailVo.setOrderDiscountFee(orderDetailVo.getOrderDiscountFee().abs());
                            /*orderDetailVo.setOutOrderId(order.getOutOrderId());
                            orderDetailVo.setOrderStatus(order.getOrderStatus());
                            orderDetailVo.setTitle(order.getTitle());
                            orderDetailVo.setOutItemId(order.getOutItemId());
                            orderDetailVo.setOrderNum(order.getOrderNum());
                            orderDetailVo.setOrderTotalFee(order.getOrderTotalFee());
                            orderDetailVo.setOrderPayment(order.getOrderPayment());
                            BigDecimal bigDecimal_1 = new BigDecimal(order.getOrderPrice());
                            orderDetailVo.setOrderPrice(bigDecimal_1);*/
                            orderDetailVos.add(orderDetailVo);
                        }
                        tradeDetailVo.setOrderDetailVoList(orderDetailVos);
                    }

                    List<PromotionDetailVo> promotionDetailVos = new ArrayList<>();

                    QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                    promotionQuery.eq("mainid",id);
                    List<Promotion> existPromotions = promotionService.list(promotionQuery);

                    if (CollUtil.isNotEmpty(existPromotions)){
                        for (Promotion promotion : existPromotions){
                            PromotionDetailVo promotionDetailVo = new PromotionDetailVo();
                            BeanUtils.copyProperties(promotion,promotionDetailVo);
                            /*if(StrUtil.isEmpty(promotionDetailVo.getPromotionToolId())){
                                promotionDetailVo.setPromotionToolId("优惠卷");
                            }*/

                            promotionDetailVo.setPromotionToolId(null);

                            promotionDetailVos.add(promotionDetailVo);
                        }
                        tradeDetailVo.setPromotionDetailVoList(promotionDetailVos);
                    }

                    tradeDetailVoList.add(tradeDetailVo);
                }

                log.info(tradeDetailVoList.size()+"");
                TradeSaveRequest request = new TradeSaveRequest();
                request.setServerUrl(nascentConfig.getBtnServerUrl());
                request.setAppKey(nascentConfig.getBtnAppKey());
                request.setAppSecret(nascentConfig.getBtnAppSerect());
                request.setGroupId(nascentConfig.getBtnGroupID());

                QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
                tokenQuery.eq("name","btn");
                List<Token> tokens = tokenService.list(tokenQuery);

                request.setAccessToken(tokens.get(0).getToken());

                log.info("悦江shopid="+key);
                log.info("贝泰妮shopid="+storeIdMap.get(key));

                request.setShopId(storeIdMap.get(key));
                request.setTradeDetailVoList(tradeDetailVoList);

                ApiClient client = new ApiClientImpl(request);

                TradeSaveResponse response = client.execute(request);
                log.info(response.getCode());
                log.info(response.getMsg());
                if("200".equals(response.getCode())){
                    log.info(response.getBody());
                    log.info("进入下次循环");
                }else if("60001".equals(response.getCode()) || "103".equals(response.getCode())){
                    UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("name","btn").set("token",tokenService.getBtnToken());
                    tokenService.update(updateWrapper);

                    String ids = "";
                    for (int j = 0 ; j<batchList.size();j++){
                        Trade trade = batchList.get(j);
                        Long id = trade.getId();

                        if(j == (batchList.size()-1)){
                            ids = ids+String.valueOf(id);
                        }else {
                            ids = ids+String.valueOf(id)+",";
                        }
                    }

                    TransBtnTradeFail transBtnTradeFail = new TransBtnTradeFail();
                    transBtnTradeFail.setIds(ids);
                    transBtnTradeFail.setMessage(response.getMsg());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
                else {
                    log.info(response.getBody());
                    String ids = "";
                    for (int j = 0 ; j<batchList.size();j++){
                        Trade trade = batchList.get(j);
                        Long id = trade.getId();

                        if(j == (batchList.size()-1)){
                            ids = ids+String.valueOf(id);
                        }else {
                            ids = ids+String.valueOf(id)+",";
                        }
                    }

                    TransBtnTradeFail transBtnTradeFail = new TransBtnTradeFail();
                    transBtnTradeFail.setIds(ids);
                    transBtnTradeFail.setMessage(response.getMsg());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
            }
        }



    }

    @Override
    public void putFailTrade() throws Exception {

        Map<Long,Long> storeIdMap = storeIdMap();
        QueryWrapper<TransBtnTradeFail> transBtnTradeFailQuery = new QueryWrapper<>();
        transBtnTradeFailQuery.isNull("is_finish").eq("ids","2932036,2932034,2932050,2932060,2932046,2932058,2932064,2932044,2932039,2932072,2932051,2932073,2932041,2932070,2932065,2932042,2932049,2932063,2932043,2932040,2932038,2932069,2932074,2932068,2932052,2932057,2932067,2932080,2932066,2932071,2932077,2932079,2932076,2932075,2932035,2932047,2932033,2932053,2932078,2932054,2932061,2932062,2932055,2932152,2932138,2932160,2932151,2932180,2932169,2932132,2932161,2932175,2932133,2932136,2932168,2932155,2932167,2932150,2932166,2932173,2932142,2932179,2932178,2932140,2932165,2932147,2932131,2932163,2932135,2932159,2932164,2932157,2932174,2932141,2932148,2932172,2932154,2932145,2932156,2932177,2932144,2932171,2932146,2932149,2932137,2932153,2932139,2932170,2932143,2932176,2932162,2932158,2932134,2932257,2932247,2932280,2932260,2932242,2932258,2932234");
        List<TransBtnTradeFail> transBtnTradeFails = transBtnTradeFailService.list();
        for (TransBtnTradeFail transBtnTradeFail : transBtnTradeFails){
            boolean flag = false;
            String ids = transBtnTradeFail.getIds();

            QueryWrapper<Trade> tradeQuery = new QueryWrapper<>();
            tradeQuery.in("id",ids);
            List<Trade> trades = tradeService.list(tradeQuery);

            log.info(trades.toString());

            List<TradeDetailVo> tradeDetailVoList = new ArrayList<>();
            for (Trade trade : trades){
                Long id = trade.getId();
                log.info("id="+id);

                TradeDetailVo tradeDetailVo = new TradeDetailVo();
                BeanUtils.copyProperties(trade,tradeDetailVo);

                if(trade.getShippingType() == null){
                    tradeDetailVo.setShippingType("free");
                }else {
                    tradeDetailVo.setShippingType(trade.getShippingType());
                }
                tradeDetailVo.setRefundStatus(0);
                if(!"TRADE_CLOSED_BY_TAOBAO".equals(trade.getTradeStatus())){
                    tradeDetailVo.setPayTime(trade.getPayTime());
                }
                tradeDetailVo.setIsCalIntegral(true);
                tradeDetailVo.setRealPointFee(null);
                tradeDetailVo.setSysCustomerId(null);
                tradeDetailVo.setNasOuid(trade.getOutNick());
                tradeDetailVo.setPlatform(null);
                tradeDetailVo.setReceiverPhone("");
                tradeDetailVo.setReceiverMobile("");
                tradeDetailVo.setDiscountFee(trade.getDiscountFee().abs());

                List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                orderQuery.eq("mainid",id);
                List<Order> existOrders = orderService.list(orderQuery);
                if(CollUtil.isNotEmpty(existOrders)){
                    for (Order order : existOrders){
                        OrderDetailVo orderDetailVo  = new OrderDetailVo();
                        BeanUtils.copyProperties(order,orderDetailVo);
                        orderDetailVo.setOrderDiscountFee(orderDetailVo.getOrderDiscountFee().abs());
                        orderDetailVos.add(orderDetailVo);
                    }
                    tradeDetailVo.setOrderDetailVoList(orderDetailVos);
                }


                List<PromotionDetailVo> promotionDetailVos = new ArrayList<>();

                QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                promotionQuery.eq("mainid",id);
                List<Promotion> existPromotions = promotionService.list(promotionQuery);

                if (CollUtil.isNotEmpty(existPromotions)){
                    for (Promotion promotion : existPromotions){
                        PromotionDetailVo promotionDetailVo = new PromotionDetailVo();
                        BeanUtils.copyProperties(promotion,promotionDetailVo);
                            /*if(StrUtil.isEmpty(promotionDetailVo.getPromotionToolId())){
                                promotionDetailVo.setPromotionToolId("优惠卷");
                            }*/

                        promotionDetailVo.setPromotionToolId(null);

                        promotionDetailVos.add(promotionDetailVo);
                    }
                    tradeDetailVo.setPromotionDetailVoList(promotionDetailVos);
                }
                tradeDetailVoList.add(tradeDetailVo);

                log.info(tradeDetailVoList.size()+"");
                TradeSaveRequest request = new TradeSaveRequest();
                request.setServerUrl(nascentConfig.getBtnServerUrl());
                request.setAppKey(nascentConfig.getBtnAppKey());
                request.setAppSecret(nascentConfig.getBtnAppSerect());
                request.setGroupId(nascentConfig.getBtnGroupID());

                QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
                tokenQuery.eq("name","btn");
                List<Token> tokens = tokenService.list(tokenQuery);

                request.setAccessToken(tokens.get(0).getToken());

                request.setShopId(storeIdMap.get(trade.getShopId()));
                request.setTradeDetailVoList(tradeDetailVoList);

                ApiClient client = new ApiClientImpl(request);

                TradeSaveResponse response = client.execute(request);

                if ("200".equals(response.getCode())){
                    log.info(response.getBody());
                    flag = true;
                }else {
                    log.info(response.getBody());
                    break;
                }

                if(flag == true){
                    UpdateWrapper<TransBtnTradeFail> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("ids",ids).set("is_finish","1");
                    transBtnTradeFailService.update(updateWrapper);
                }
                log.info("同步下一个数据");
            }





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

        QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
        tokenQuery.eq("name","nascent");
        List<Token> tokens = tokenService.list(tokenQuery);
        request.setAccessToken(tokens.get(0).getToken());

        request.setTimeType(1);

        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setNextId(nextId);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        TradeSynResponse response = client.execute(request);
        //log.info(response.getBody());
        List<TradesVo> tradesVos = response.getResult();
        List<Trade> insertTrades = new ArrayList<>();




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
                    /*trade.setRequestId(response.getRequestId());
                    Date date = new Date();
                    trade.setTransTime(date);*/

                    QueryWrapper<Trade> tradeQuery = new QueryWrapper<>();
                    tradeQuery.eq("id",id);
                    Trade tradeResult = tradeService.getOne(tradeQuery);
                    if(tradeResult!=null){
                        //进入更新集合
                        //updateTrades.add(trade);
                        /*UpdateWrapper<Trade> tradeUpdate = new UpdateWrapper<>();
                        tradeUpdate.eq("id",id);
                        tradeService.update(trade,tradeUpdate);

                        List<NickInfo> nickInfos = tradesVo.getNickInfoList();*/


                        /*if(CollUtil.isNotEmpty(nickInfos)){

                            QueryWrapper<Nick> nickQuery = new QueryWrapper<>();
                            nickQuery.eq("mainid",id);
                            nickService.remove(nickQuery);

                            List<Nick> nicks = new ArrayList<>();
                            for (NickInfo nickInfo : nickInfos){
                                Nick nick = new Nick();
                                BeanUtils.copyProperties(nickInfo, nick);
                                nick.setMainid(id);

                                nicks.add(nick);
                            }
                            nickService.saveBatch(nicks);
                        }*/

                        /*List<PromotionsVo> promotionsVos = tradesVo.getPromotionVos();

                        if(CollUtil.isNotEmpty(promotionsVos)){

                            QueryWrapper<Promotion> promotionQuery = new QueryWrapper<>();
                            promotionQuery.eq("mainid",id);
                            promotionService.remove(promotionQuery);


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

                            QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                            orderQuery.eq("mainid",id);
                            orderService.remove(orderQuery);

                            for (OrdersVo ordersVo : ordersVos){
                                Order order = new Order();
                                BeanUtils.copyProperties(ordersVo,order);
                                order.setMainid(id);
                                orders.add(order);

                            }
                            orderService.saveBatch(orders);
                        }*/



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
                    tradeService.saveBatch(insertTrades);
                }

            }
            resMap.put("nextId",nextId);
            resMap.put("updateTime",updateTime);
            resMap.put("isNext",isNext);
        }
        return resMap;
    }


    public static Map<Long,Long> storeIdMap(){

        Map<Long, Long> storeIdMap = new HashMap<>();

        // 数据初始化
        storeIdMap.put(100150234L, 101130609L);
        storeIdMap.put(100157262L, 101130549L);
        storeIdMap.put(100150235L, 101130610L);
        storeIdMap.put(100234651L, 101130540L);
        storeIdMap.put(100150179L, 101155857L);
        storeIdMap.put(100186879L, 101130542L);
        storeIdMap.put(100150210L, 101130589L);
        storeIdMap.put(100150228L, 101155862L);
        storeIdMap.put(100150244L, 101155867L);
        storeIdMap.put(100150229L, 101130605L);
        storeIdMap.put(100150230L, 101130606L);
        storeIdMap.put(100150211L, 101130590L);
        storeIdMap.put(100150233L, 101155863L);
        storeIdMap.put(100150166L, 101130620L);
        storeIdMap.put(100156928L, 101092686L);
        storeIdMap.put(100149662L, 101130619L);
        storeIdMap.put(100150205L, 101130584L);
        storeIdMap.put(100150214L, 101130593L);
        storeIdMap.put(100150199L, 101130578L);
        storeIdMap.put(100150197L, 101130576L);
        storeIdMap.put(100150169L, 101130552L);
        storeIdMap.put(100186877L, 101130547L);
        storeIdMap.put(100150226L, 101130603L);
        storeIdMap.put(100150225L, 101155861L);
        storeIdMap.put(100150212L, 101130591L);
        storeIdMap.put(100150237L, 101130612L);
        storeIdMap.put(100150180L, 101130561L);
        storeIdMap.put(100150219L, 101130598L);
        storeIdMap.put(100186881L, 101130544L);
        storeIdMap.put(100150177L, 101130559L);
        storeIdMap.put(100150221L, 101130599L);
        storeIdMap.put(100150168L, 101130551L);
        storeIdMap.put(100150172L, 101130555L);
        storeIdMap.put(100150175L, 101130557L);
        storeIdMap.put(100150182L, 101130563L);
        storeIdMap.put(100150184L, 101130565L);
        storeIdMap.put(100150183L, 101130564L);
        storeIdMap.put(100156915L, 101155854L);
        storeIdMap.put(100150232L, 101130608L);
        storeIdMap.put(100150173L, 101130556L);
        storeIdMap.put(100150245L, 101155868L);
        storeIdMap.put(100150227L, 101130604L);
        storeIdMap.put(100186878L, 101130548L);
        storeIdMap.put(100150213L, 101130592L);
        storeIdMap.put(100149663L, 101130621L);
        storeIdMap.put(100149661L, 101130618L);
        storeIdMap.put(100150241L, 101130615L);
        storeIdMap.put(100150238L, 101130613L);
        storeIdMap.put(100150243L, 101155866L);
        storeIdMap.put(100150242L, 101155865L);
        storeIdMap.put(100150198L, 101130577L);
        storeIdMap.put(100150239L, 101155864L);
        storeIdMap.put(100150174L, 101155856L);
        storeIdMap.put(100150208L, 101130587L);
        storeIdMap.put(100150209L, 101130588L);
        storeIdMap.put(100150218L, 101130597L);
        storeIdMap.put(100150240L, 101130614L);
        storeIdMap.put(100150178L, 101130560L);
        storeIdMap.put(100150181L, 101130562L);
        storeIdMap.put(100150176L, 101130558L);
        storeIdMap.put(100150171L, 101130554L);
        storeIdMap.put(100186883L, 101130546L);
        storeIdMap.put(100150223L, 101130601L);
        storeIdMap.put(100150167L, 101155855L);
        storeIdMap.put(100186882L, 101130545L);
        storeIdMap.put(100150222L, 101130600L);
        storeIdMap.put(100150236L, 101130611L);
        storeIdMap.put(100186884L, 101130541L);
        storeIdMap.put(100150224L, 101130602L);
        storeIdMap.put(100150553L, 101130550L);
        storeIdMap.put(100149660L, 101130616L);
        storeIdMap.put(100150083L, 101092685L);
        storeIdMap.put(100150165L,101130617L);
        return storeIdMap;
    }


}
