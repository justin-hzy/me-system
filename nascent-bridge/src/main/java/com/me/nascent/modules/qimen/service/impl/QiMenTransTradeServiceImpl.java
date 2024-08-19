package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.entity.QiMenTrade;
import com.me.nascent.modules.qimen.service.QiMenTradeService;
import com.me.nascent.modules.qimen.service.QiMenTransTradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenTransTradeServiceImpl implements QiMenTransTradeService {

    private QiMenTradeService qiMenTradeService;

    @Override
    public String transTrade(QiMenDto dto) throws Exception {

        String message = dto.getMessage();

        Map<String, String> messageMap = convertJsonToMap(message);

        // 校验数据合法性 todo

        //
        QiMenTrade qiMenTrade = encTrade(messageMap);

        QueryWrapper<QiMenTrade> qiMenTradeQuery = new QueryWrapper<>();
        qiMenTradeQuery.eq("tid",qiMenTrade.getTid())
                .eq("sellerNick",qiMenTrade.getSellerNick());

        QiMenTrade existTrade = qiMenTradeService.getOne(qiMenTradeQuery);

        if(existTrade != null){
            String modified = existTrade.getModified();
            String newModified = qiMenTrade.getModified();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date existDate = sdf.parse(modified);

            Date newDate = sdf.parse(newModified);

            if(existDate.before(newDate)){
                UpdateWrapper<QiMenTrade> qiMenTradeUpdate = new UpdateWrapper<>();
                qiMenTradeUpdate.eq("tid",qiMenTrade.getTid())
                        .eq("sellerNick",qiMenTrade.getSellerNick());
            }
        }else {
            qiMenTradeService.save(existTrade);
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code","SUCCESS");
        jsonObject.put("return_msg","数据处理成功");
        return jsonObject.toJSONString();
    }

    public static Map<String, String> convertJsonToMap(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            Map<String, String> map = mapper.readValue(jsonString, typeFactory.constructMapType(Map.class, String.class, String.class));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QiMenTrade encTrade(Map<String,String> messageMap){
        String consignTime = messageMap.get("consignTime");
        String adjustFee = messageMap.get("adjustFee");
        String num = messageMap.get("num");
        String rxAuditStatus = messageMap.get("rxAuditStatus");
        String availableConfirmFee = messageMap.get("availableConfirmFee");
        String snapshotUrl = messageMap.get("snapshotUrl");
        String type = messageMap.get("type");
        String receivedPayment = messageMap.get("receivedPayment");
        String tid = messageMap.get("tid");
        String stepPaidFee = messageMap.get("stepPaidFee");
        String price = messageMap.get("price");
        String expandCardBasicPriceUsed = messageMap.get("expandCardBasicPriceUsed");
        String totalFee = messageMap.get("totalFee");
        String modified = messageMap.get("modified");
        String payment = messageMap.get("payment");
        String canRate = messageMap.get("canRate");
        String buyerMessage = messageMap.get("buyerMessage");
        String sellerMemo = messageMap.get("sellerMemo");
        String created = messageMap.get("created");
        String payTime = messageMap.get("payTime");
        String hasPostFee = messageMap.get("hasPostFee");
        String couponFee = messageMap.get("couponFee");
        /*String[] orders = (String[]) messageMap.get("orders");
        String[] promotionDetails = (String[]) messageMap.get("promotionDetails");*/
        String sellerCanRate = messageMap.get("sellerCanRate");
        String status = messageMap.get("status");
        String sellerRate = messageMap.get("sellerRate");
        String postFee = messageMap.get("postFee");
        String timeoutActionTime = messageMap.get("timeoutActionTime");
        String expandCardExpandPriceUsed = messageMap.get("expandCardExpandPriceUsed");
        String receiverCity = messageMap.get("receiverCity");
        String shippingType = messageMap.get("shippingType");
        String numIid = messageMap.get("numIid");
        String title = messageMap.get("title");
        String buyerRate = messageMap.get("buyerRate");
        String expandCardExpandPrice = messageMap.get("expandCardExpandPrice");
        String discountFee = messageMap.get("discountFee");
        String receiverState = messageMap.get("receiverState");
        String stepTradeStatus = messageMap.get("stepTradeStatus");
        String buyerMemo = messageMap.get("buyerMemo");
        /*ExpandcardInfo expandcardInfo = (ExpandcardInfo) messageMap.get("expandcardInfo");*/
        String buyerOpenUid = messageMap.get("buyerOpenUid");
        String tradeFrom = messageMap.get("tradeFrom");
        String endTime = messageMap.get("endTime");
        String picPath = messageMap.get("picPath");
        String expandCardBasicPrice = messageMap.get("expandCardBasicPrice");
        String sellerFlag = messageMap.get("sellerFlag");
        String isPartConsign = messageMap.get("isPartConsign");
        String sellerNick = messageMap.get("sellerNick");



        QiMenTrade qiMenTrade = new QiMenTrade();
        qiMenTrade.setConsignTime(consignTime);
        qiMenTrade.setAdjustFee(adjustFee);
        qiMenTrade.setNum(num);
        qiMenTrade.setRxAuditStatus(rxAuditStatus);
        qiMenTrade.setAvailableConfirmFee(availableConfirmFee);
        qiMenTrade.setSnapshotUrl(snapshotUrl);
        qiMenTrade.setType(type);
        qiMenTrade.setReceivedPayment(receivedPayment);
        qiMenTrade.setTid(tid);
        qiMenTrade.setStepPaidFee(stepPaidFee);
        qiMenTrade.setPrice(price);
        qiMenTrade.setExpandCardBasicPriceUsed(expandCardBasicPriceUsed);
        qiMenTrade.setTotalFee(totalFee);
        qiMenTrade.setModified(modified);
        qiMenTrade.setPayment(payment);
        qiMenTrade.setCanRate(canRate);
        qiMenTrade.setBuyerMessage(buyerMessage);
        qiMenTrade.setSellerMemo(sellerMemo);
        qiMenTrade.setCreated(created);
        qiMenTrade.setPayTime(payTime);
        qiMenTrade.setHasPostFee(hasPostFee);
        qiMenTrade.setCouponFee(couponFee);
        qiMenTrade.setSellerCanRate(sellerCanRate);
        qiMenTrade.setStatus(status);
        qiMenTrade.setSellerRate(sellerRate);
        qiMenTrade.setPostFee(postFee);
        qiMenTrade.setTimeoutActionTime(timeoutActionTime);
        qiMenTrade.setExpandCardExpandPriceUsed(expandCardExpandPriceUsed);
        qiMenTrade.setReceiverCity(receiverCity);
        qiMenTrade.setShippingType(shippingType);
        qiMenTrade.setNumIid(numIid);
        qiMenTrade.setTitle(title);
        qiMenTrade.setBuyerRate(buyerRate);
        qiMenTrade.setExpandCardExpandPrice(expandCardExpandPrice);
        qiMenTrade.setDiscountFee(discountFee);
        qiMenTrade.setReceiverState(receiverState);
        qiMenTrade.setStepTradeStatus(stepTradeStatus);
        qiMenTrade.setBuyerMemo(buyerMemo);
        qiMenTrade.setTradeFrom(tradeFrom);
        qiMenTrade.setEndTime(endTime);
        qiMenTrade.setPicPath(picPath);
        qiMenTrade.setExpandCardBasicPrice(expandCardBasicPrice);
        qiMenTrade.setSellerFlag(sellerFlag);
        qiMenTrade.setIsPartConsign(isPartConsign);
        qiMenTrade.setSellerNick(sellerNick);

        return qiMenTrade;
    }
}
