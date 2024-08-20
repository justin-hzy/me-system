package com.me.nascent.modules.qimen.service.impl;

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

        Map<String, String> messageMap = convertJsonToMap(message);

        // 校验数据合法性 todo

        //
        QiMenTrade qiMenTrade = encTrade(messageMap);

        String orders = messageMap.get("orders");
        JSONArray orderJsonArr = JSONArray.parseArray(orders);

        String promotionDetails = messageMap.get("promotionDetails");
        JSONArray promotionDetailJsonArr = JSONArray.parseArray(promotionDetails);

        String expandCardInfo = messageMap.get("expandcardInfo");
        JSONObject expandCardInfoJsonObj = JSONObject.parseObject(expandCardInfo);
        QIMenExpandCardInfo qiMenExpandCardInfo = encExpandCardInfo(expandCardInfoJsonObj);

        List<QiMenOrder> qiMenOrders = new ArrayList<>();
        for (int i = 0;i<orderJsonArr.size();i++){
            JSONObject orderJsonObj = orderJsonArr.getJSONObject(i);
            QiMenOrder qiMenOrder = encOrder(orderJsonObj);

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

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date existDate = sdf.parse(modified);

            Date newDate = sdf.parse(newModified);

            if(existDate.before(newDate)){
                qiMenTradeService.saveOrUpdate(existTrade);
                qiMenOrderService.saveOrUpdateBatch(qiMenOrders);
                qiMenPromotionDetailService.saveOrUpdateBatch(qiMenPromotionDetails);
                qiMenExpandCardInfoService.saveOrUpdate(qiMenExpandCardInfo);
            }
        }else {
            qiMenTradeService.save(existTrade);
            qiMenOrderService.saveBatch(qiMenOrders);
            qiMenPromotionDetailService.saveBatch(qiMenPromotionDetails);
            qiMenExpandCardInfoService.save(qiMenExpandCardInfo);
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
