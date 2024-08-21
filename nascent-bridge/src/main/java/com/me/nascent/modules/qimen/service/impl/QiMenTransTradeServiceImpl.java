package com.me.nascent.modules.qimen.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.entity.QIMenExpandCardInfo;
import com.me.nascent.modules.qimen.entity.QiMenOrder;
import com.me.nascent.modules.qimen.entity.QiMenPromotionDetail;
import com.me.nascent.modules.qimen.entity.QiMenTrade;
import com.me.nascent.modules.qimen.service.*;
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

    private QiMenOrderService qiMenOrderService;

    private QiMenPromotionDetailService qiMenPromotionDetailService;

    private QIMenExpandCardInfoService qiMenExpandCardInfoService;

    @Override
    public String transTrade(QiMenDto dto) throws Exception {

        String message = dto.getMessage();

        JSONObject messageJsonObj = JSONObject.parseObject(message);
        // 校验数据合法性 todo


        JSONObject tradeJsonObj = messageJsonObj.getJSONObject("trade");


        //
        QiMenTrade qiMenTrade = encTrade(tradeJsonObj);
        log.info(qiMenTrade.toString());

        JSONArray orderJsonArr = tradeJsonObj.getJSONArray("orders");

        JSONArray promotionDetailJsonArr = tradeJsonObj.getJSONArray("promotionDetails");

        JSONObject expandCardInfoJsonObj = tradeJsonObj.getJSONObject("expandcardInfo");


        QIMenExpandCardInfo qiMenExpandCardInfo = null;

        if (expandCardInfoJsonObj != null){
            qiMenExpandCardInfo = encExpandCardInfo(expandCardInfoJsonObj);
        }

        List<QiMenOrder> qiMenOrders = new ArrayList<>();
        for (int i = 0;i<orderJsonArr.size();i++){
            JSONObject orderJsonObj = orderJsonArr.getJSONObject(i);
            QiMenOrder qiMenOrder = encOrder(orderJsonObj);
            qiMenOrder.setTid(qiMenTrade.getTid());

            qiMenOrders.add(qiMenOrder);
        }


        List<QiMenPromotionDetail> qiMenPromotionDetails = new ArrayList<>();
        for (int i = 0 ; i<promotionDetailJsonArr.size();i++){
            JSONObject promotionDetailJsonObj = promotionDetailJsonArr.getJSONObject(i);
            QiMenPromotionDetail qiMenPromotionDetail = encPromotion(promotionDetailJsonObj);

            qiMenPromotionDetails.add(qiMenPromotionDetail);
        }

        QueryWrapper<QiMenTrade> qiMenTradeQuery = new QueryWrapper<>();
        qiMenTradeQuery.eq("tid",qiMenTrade.getTid())
                .eq("sellerNick",qiMenTrade.getSellerNick());

        QiMenTrade existTrade = qiMenTradeService.getOne(qiMenTradeQuery);

        if(existTrade != null){
            String modified = existTrade.getModified();
            String newModified = qiMenTrade.getModified();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

            Date existDate = sdf.parse(modified);

            Date newDate = sdf.parse(newModified);

            if(existDate.before(newDate)){
                UpdateWrapper<QiMenTrade> qiMenTradeUpdate = new UpdateWrapper();
                qiMenTradeUpdate.eq("tid",qiMenTrade.getTid()).eq("sellerNick",qiMenTrade.getSellerNick());
                qiMenTradeService.update(qiMenTrade,qiMenTradeUpdate);

                for (QiMenOrder qiMenOrder : qiMenOrders){
                    UpdateWrapper<QiMenOrder> qiMenOrderUpdate = new UpdateWrapper();
                    qiMenOrderUpdate.eq("oid",qiMenOrder.getOid()).eq("tid",qiMenTrade.getTid());
                    qiMenOrderService.update(qiMenOrder,qiMenOrderUpdate);
                }

                if (CollUtil.isNotEmpty(qiMenPromotionDetails)){
                    for (QiMenPromotionDetail qiMenPromotionDetail : qiMenPromotionDetails){
                        UpdateWrapper<QiMenPromotionDetail> qiMenPromotionDetailUpdate = new UpdateWrapper();
                        qiMenPromotionDetailUpdate.eq("id",qiMenPromotionDetail.getId()).eq("tid",qiMenTrade.getTid());
                    }
                }

                if(qiMenExpandCardInfo != null){
                    UpdateWrapper<QIMenExpandCardInfo> qiMenExpandCardInfoUpdate = new UpdateWrapper<>();
                    qiMenExpandCardInfoUpdate.eq("tid",qiMenExpandCardInfo.getTid());
                    qiMenExpandCardInfoService.update(qiMenExpandCardInfo,qiMenExpandCardInfoUpdate);
                }
            }
        }else {
            qiMenTradeService.save(qiMenTrade);
            qiMenOrderService.saveBatch(qiMenOrders);
            qiMenPromotionDetailService.saveBatch(qiMenPromotionDetails);
            if(qiMenExpandCardInfo != null){
                qiMenExpandCardInfoService.save(qiMenExpandCardInfo);
            }
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

    public static QiMenTrade encTrade(JSONObject tradeJsonObj){
        String consignTime = tradeJsonObj.getString("consignTime");
        String adjustFee = tradeJsonObj.getString("adjustFee");
        String num = tradeJsonObj.getString("num");
        String rxAuditStatus = tradeJsonObj.getString("rxAuditStatus");
        String availableConfirmFee = tradeJsonObj.getString("availableConfirmFee");
        String snapshotUrl = tradeJsonObj.getString("snapshotUrl");
        String type = tradeJsonObj.getString("type");
        String receivedPayment = tradeJsonObj.getString("receivedPayment");
        String tid = tradeJsonObj.getString("tid");
        String stepPaidFee = tradeJsonObj.getString("stepPaidFee");
        String price = tradeJsonObj.getString("price");
        String expandCardBasicPriceUsed = tradeJsonObj.getString("expandCardBasicPriceUsed");
        String totalFee = tradeJsonObj.getString("totalFee");
        String modified = tradeJsonObj.getString("modified");
        String payment = tradeJsonObj.getString("payment");
        String canRate = tradeJsonObj.getString("canRate");
        String buyerMessage = tradeJsonObj.getString("buyerMessage");
        String sellerMemo = tradeJsonObj.getString("sellerMemo");
        String created = tradeJsonObj.getString("created");
        String payTime = tradeJsonObj.getString("payTime");
        String hasPostFee = tradeJsonObj.getString("hasPostFee");
        String couponFee = tradeJsonObj.getString("couponFee");
        String sellerCanRate = tradeJsonObj.getString("sellerCanRate");
        String status = tradeJsonObj.getString("status");
        String sellerRate = tradeJsonObj.getString("sellerRate");
        String postFee = tradeJsonObj.getString("postFee");
        String timeoutActionTime = tradeJsonObj.getString("timeoutActionTime");
        String expandCardExpandPriceUsed = tradeJsonObj.getString("expandCardExpandPriceUsed");
        String receiverCity = tradeJsonObj.getString("receiverCity");
        String shippingType = tradeJsonObj.getString("shippingType");
        String numIid = tradeJsonObj.getString("numIid");
        String title = tradeJsonObj.getString("title");
        String buyerRate = tradeJsonObj.getString("buyerRate");
        String expandCardExpandPrice = tradeJsonObj.getString("expandCardExpandPrice");
        String discountFee = tradeJsonObj.getString("discountFee");
        String receiverState = tradeJsonObj.getString("receiverState");
        String stepTradeStatus = tradeJsonObj.getString("stepTradeStatus");
        String buyerMemo = tradeJsonObj.getString("buyerMemo");
        String tradeFrom = tradeJsonObj.getString("tradeFrom");
        String endTime = tradeJsonObj.getString("endTime");
        String picPath = tradeJsonObj.getString("picPath");
        String expandCardBasicPrice = tradeJsonObj.getString("expandCardBasicPrice");
        String sellerFlag = tradeJsonObj.getString("sellerFlag");
        String isPartConsign = tradeJsonObj.getString("isPartConsign");
        String sellerNick = tradeJsonObj.getString("sellerNick");



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

    public static QiMenOrder encOrder(JSONObject orderJsonObj){
        String sellerRate = orderJsonObj.getString("sellerRate");
        String consignTime = orderJsonObj.getString("consignTime");
        String timeoutActionTime = orderJsonObj.getString("timeoutActionTime");
        String customization = orderJsonObj.getString("customization");
        String adjustFee = orderJsonObj.getString("adjustFee");
        String num = orderJsonObj.getString("num");
        String shippingType = orderJsonObj.getString("shippingType");
        String numIid = orderJsonObj.getString("numIid");
        String oid = orderJsonObj.getString("oid");
        String snapshotUrl = orderJsonObj.getString("snapshotUrl");
        String title = orderJsonObj.getString("title");
        String buyerRate = orderJsonObj.getString("buyerRate");
        String discountFee = orderJsonObj.getString("discountFee");
        String price = orderJsonObj.getString("price");
        String totalFee = orderJsonObj.getString("totalFee");
        String payment = orderJsonObj.getString("payment");
        String isOversold = orderJsonObj.getString("isOversold");
        String outerSkuId = orderJsonObj.getString("outerSkuId");
        String partMjzDiscount = orderJsonObj.getString("partMjzDiscount");
        String refundStatus = orderJsonObj.getString("refundStatus");
        String bindOids = orderJsonObj.getString("bindOids");
        String invoiceNo = orderJsonObj.getString("invoiceNo");
        String endTime = orderJsonObj.getString("endTime");
        String picPath = orderJsonObj.getString("picPath");
        String skuId = orderJsonObj.getString("skuId");
        String skuPropertiesName = orderJsonObj.getString("skuPropertiesName");
        String refundId = orderJsonObj.getString("refundId");
        String divideOrderFee = orderJsonObj.getString("divideOrderFee");
        String orderFrom = orderJsonObj.getString("orderFrom");
        String outerIid = orderJsonObj.getString("outerIid");
        String logisticsCompany = orderJsonObj.getString("logisticsCompany");
        String expandCardExpandPriceUsedSuborder = orderJsonObj.getString("expandCardExpandPriceUsedSuborder");
        String expandCardBasicPriceUsedSuborder = orderJsonObj.getString("expandCardBasicPriceUsedSuborder");
        String status = orderJsonObj.getString("status");
        String cid = orderJsonObj.getString("cid");
        String itemMealId = orderJsonObj.getString("itemMealId");

        QiMenOrder qiMenOrder = new QiMenOrder();
        qiMenOrder.setSellerRate(sellerRate);
        qiMenOrder.setConsignTime(consignTime);
        qiMenOrder.setTimeoutActionTime(timeoutActionTime);
        qiMenOrder.setCustomization(customization);
        qiMenOrder.setAdjustFee(adjustFee);
        qiMenOrder.setNum(num);
        qiMenOrder.setShippingType(shippingType);
        qiMenOrder.setNumIid(numIid);
        qiMenOrder.setOid(oid);
        qiMenOrder.setSnapshotUrl(snapshotUrl);
        qiMenOrder.setTitle(title);
        qiMenOrder.setBuyerRate(buyerRate);
        qiMenOrder.setDiscountFee(discountFee);
        qiMenOrder.setPrice(price);
        qiMenOrder.setTotalFee(totalFee);
        qiMenOrder.setPayment(payment);
        qiMenOrder.setIsOversold(isOversold);
        qiMenOrder.setOuterSkuId(outerSkuId);
        qiMenOrder.setPartMjzDiscount(partMjzDiscount);
        qiMenOrder.setRefundStatus(refundStatus);
        qiMenOrder.setBindOids(bindOids);
        qiMenOrder.setInvoiceNo(invoiceNo);
        qiMenOrder.setEndTime(endTime);
        qiMenOrder.setPicPath(picPath);
        qiMenOrder.setSkuId(skuId);
        qiMenOrder.setSkuPropertiesName(skuPropertiesName);
        qiMenOrder.setRefundId(refundId);
        qiMenOrder.setDivideOrderFee(divideOrderFee);
        qiMenOrder.setOrderFrom(orderFrom);
        qiMenOrder.setOuterIid(outerIid);
        qiMenOrder.setLogisticsCompany(logisticsCompany);
        qiMenOrder.setExpandCardExpandPriceUsedSuborder(expandCardExpandPriceUsedSuborder);
        qiMenOrder.setExpandCardBasicPriceUsedSuborder(expandCardBasicPriceUsedSuborder);
        qiMenOrder.setStatus(status);
        qiMenOrder.setCid(cid);
        qiMenOrder.setItemMealId(itemMealId);

        return qiMenOrder;
    }

    public static QiMenPromotionDetail encPromotion(JSONObject promotionJsonObj){
        String promotionName = promotionJsonObj.getString("promotionName");
        String giftItemId = promotionJsonObj.getString("giftItemId");
        String giftItemName = promotionJsonObj.getString("giftItemName");
        String promotionId = promotionJsonObj.getString("promotionId");
        String id = promotionJsonObj.getString("id");
        String giftItemNum = promotionJsonObj.getString("giftItemNum");
        String promotionDesc = promotionJsonObj.getString("promotionDesc");
        String discountFee = promotionJsonObj.getString("discountFee");

        QiMenPromotionDetail qiMenPromotionDetail = new QiMenPromotionDetail();
        qiMenPromotionDetail.setPromotionName(promotionName);
        qiMenPromotionDetail.setGiftItemId(giftItemId);
        qiMenPromotionDetail.setGiftItemName(giftItemName);
        qiMenPromotionDetail.setPromotionId(promotionId);
        qiMenPromotionDetail.setId(id);
        qiMenPromotionDetail.setGiftItemNum(giftItemNum);
        qiMenPromotionDetail.setPromotionDesc(promotionDesc);
        qiMenPromotionDetail.setDiscountFee(discountFee);
        return qiMenPromotionDetail;
    }


    public static QIMenExpandCardInfo encExpandCardInfo(JSONObject expandCardInfoJsonObj){
        String basicPriceUsed = expandCardInfoJsonObj.getString("basicPriceUsed");
        String expandPrice = expandCardInfoJsonObj.getString("expandPrice");
        String basicPrice = expandCardInfoJsonObj.getString("basicPrice");
        String expandPriceUsed = expandCardInfoJsonObj.getString("expandPriceUsed");

        QIMenExpandCardInfo qiMenExpandCardInfo = new QIMenExpandCardInfo();
        qiMenExpandCardInfo.setBasicPriceUsed(basicPriceUsed);
        qiMenExpandCardInfo.setExpandPrice(expandPrice);
        qiMenExpandCardInfo.setBasicPrice(basicPrice);
        qiMenExpandCardInfo.setExpandPriceUsed(expandPriceUsed);

        return qiMenExpandCardInfo;
    }
}
