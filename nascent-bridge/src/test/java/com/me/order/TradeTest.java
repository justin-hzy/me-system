package com.me.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.order.service.*;
import com.me.nascent.modules.order.entity.*;
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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Slf4j
public class TradeTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;


    @Autowired
    TransOrderService transOrderService;

    @Autowired
    TransBtnTradeFailService transBtnTradeFailService;

    @Autowired
    PromotionService promotionService;

    @Autowired
    TradeService tradeService;

    @Autowired
    OrderService orderService;


    @Test
    public void getOrderByDay() throws Exception {

        String startDateStr  = "2024-08-28 13:25:16";
        String endDateStr = "2024-08-31 23:59:59";


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = sdf.parse(startDateStr);

        Date endDate = sdf.parse(endDateStr);

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() + 30 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr1 = sdf.format(startDate);
            String endStr2 = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr1 + " 到 " + endStr2);

            transOrderService.transOrder(startDate, endDateOfWeek);

            startDate = endDateOfWeek; // 修改为1分钟
            //System.out.println("下一个开启时间:" + startDate);
        }
    }

    @Test
    public void getOrderByYear() throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String year = "2024";

        Calendar cal = Calendar.getInstance();

        for (int month = 10; month <= 10; month++) {
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startDate = cal.getTime();

            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endDate = cal.getTime();

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDate);
            System.out.println("同步会员数据: " + startStr + " 到 " + endStr);

            while (startDate.before(endDate)) {
                Date endDateOfWeek = new Date(startDate.getTime() + 30 * 60 * 1000); // 修改为30分钟
                if (endDateOfWeek.after(endDate)) {
                    endDateOfWeek = endDate;
                }

                String startStr1 = sdf.format(startDate);
                String endStr2 = sdf.format(endDateOfWeek);
                System.out.println("同步订单数据: " + startStr1 + " 到 " + endStr2);

                transOrderService.transOrder(startDate, endDateOfWeek);

                startDate = endDateOfWeek; // 修改为1分钟
                //System.out.println("下一个开启时间:" + startDate);
            }

        }
    }


    @Test
    public void putTradeByOne() throws Exception {
        transOrderService.putTradeByOne();

    }


    @Test
    public void putTradeByRange() throws Exception{
        transOrderService.putTradeByRange();
    }

    @Test
    public void putFailTrade() throws Exception {
        transOrderService.putFailTrade();
    }

    @Test
    public void putTradeByRange_2021_01() throws Exception {
        Map<Long,Long> storeIdMap = storeIdMap();

        QueryWrapper<Trade> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.likeRight("created","2021-01")
                //.in("shopId","100149660")
                .in("shopId","100149660","100150165","100149661","100150083","100149662","100150166","100149663","100156928")
                .last("AND length(outNick) > 0");
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

                    if("TRADE_FINISHED".equals(trade.getTradeStatus())){
                        tradeDetailVo.setConsignTime(tradeDetailVo.getPayTime());
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
                    tradeDetailVo.setAvailableConfirmFee(null);




                    List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                    QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                    orderQuery.eq("mainid",id);
                    List<Order> existOrders = orderService.list(orderQuery);
                    if(CollUtil.isNotEmpty(existOrders)){
                        for (Order order : existOrders){
                            OrderDetailVo orderDetailVo  = new OrderDetailVo();
                            BeanUtils.copyProperties(order,orderDetailVo);
                            orderDetailVo.setOrderDiscountFee(orderDetailVo.getOrderDiscountFee().abs());
                            if(StrUtil.isEmpty(order.getTitle())){
                                orderDetailVo.setTitle("-");
                            }
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
                    transBtnTradeFail.setMessage(response.getRequestId());
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
                    transBtnTradeFail.setMessage(response.getRequestId());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
            }
        }
    }

    @Test
    public void putTradeByRange_2024_06() throws Exception {
        Map<Long,Long> storeIdMap = storeIdMap();

        QueryWrapper<Trade> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.likeRight("created","2024-06")
                //.in("shopId","100149660")
                .in("shopId","100149660","100150165","100149661","100150083","100149662","100150166","100149663","100156928");
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

                    if("TRADE_FINISHED".equals(trade.getTradeStatus())){
                        if(tradeDetailVo.getConsignTime() == null && tradeDetailVo.getPayTime() != null){
                            tradeDetailVo.setConsignTime(tradeDetailVo.getPayTime());
                        }

                        if(tradeDetailVo.getConsignTime() == null && tradeDetailVo.getPayTime() == null){
                            tradeDetailVo.setConsignTime(trade.getCreated());
                        }
                    }

                    if("TRADE_CLOSED".equals(trade.getTradeStatus()) || "TRADE_FINISHED".equals(trade.getTradeStatus())){
                        if(tradeDetailVo.getPayTime() == null){
                            tradeDetailVo.setPayTime(trade.getCreated());
                        }
                    }


                    tradeDetailVo.setIsCalIntegral(true);
                    tradeDetailVo.setRealPointFee(null);
                    tradeDetailVo.setSysCustomerId(null);
                    tradeDetailVo.setNasOuid(trade.getOutNick());
                    tradeDetailVo.setPlatform(null);
                    tradeDetailVo.setReceiverPhone("");
                    tradeDetailVo.setReceiverMobile("");
                    tradeDetailVo.setDiscountFee(trade.getDiscountFee().abs());
                    tradeDetailVo.setAvailableConfirmFee(null);




                    List<OrderDetailVo> orderDetailVos = new ArrayList<>();

                    QueryWrapper<Order> orderQuery = new QueryWrapper<>();
                    orderQuery.eq("mainid",id);
                    List<Order> existOrders = orderService.list(orderQuery);
                    if(CollUtil.isNotEmpty(existOrders)){
                        for (Order order : existOrders){
                            OrderDetailVo orderDetailVo  = new OrderDetailVo();
                            BeanUtils.copyProperties(order,orderDetailVo);
                            orderDetailVo.setOrderDiscountFee(orderDetailVo.getOrderDiscountFee().abs());
                            if(StrUtil.isEmpty(order.getTitle())){
                                orderDetailVo.setTitle("-");
                            }
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
                    transBtnTradeFail.setMessage(response.getRequestId());
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
                    transBtnTradeFail.setMessage(response.getRequestId());
                    transBtnTradeFailService.save(transBtnTradeFail);
                }
            }
        }
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
