package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.entity.QiMenOrder;
import com.me.nascent.modules.qimen.entity.QiMenReTrade;
import com.me.nascent.modules.qimen.service.QiMenOrderService;
import com.me.nascent.modules.qimen.service.QiMenReTradeService;
import com.me.nascent.modules.qimen.service.QiMenTransReFundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenTransReFundServiceImpl implements QiMenTransReFundService {

    private QiMenReTradeService qiMenReTradeService;

    private QiMenOrderService qiMenOrderService;

    @Override
    public String transReFund(QiMenDto dto) {
        String message = dto.getMessage();
        /*Map<String, String> messageMap = convertJsonToMap(message);*/
        JSONObject messageJsonObj = JSONObject.parseObject(message);
        JSONObject tradeJsonObj = messageJsonObj.getJSONObject("trade");

        // 校验数据合法性 todo

        QiMenReTrade qiMenReTrade = encReTrade(tradeJsonObj);

        JSONArray orderJsonArr = tradeJsonObj.getJSONArray("orders");

        List<QiMenOrder> qiMenOrders = new ArrayList<>();
        for(int i = 0;i<orderJsonArr.size();i++){
            JSONObject orderJsonObj = orderJsonArr.getJSONObject(i);
            QiMenOrder qiMenOrder = encOrder(orderJsonObj);
            qiMenOrders.add(qiMenOrder);
        }


        QueryWrapper<QiMenReTrade> qiMenReTradeQuery = new QueryWrapper<>();
        qiMenReTradeQuery.eq("tid",qiMenReTrade.getTid());

        QiMenReTrade existReTrade = qiMenReTradeService.getOne(qiMenReTradeQuery);

        if(existReTrade != null){
            UpdateWrapper<QiMenReTrade> qiMenReTradeUpdate = new UpdateWrapper<>();
            qiMenReTradeUpdate.eq("tid",qiMenReTrade.getTid());

            qiMenReTradeService.update(qiMenReTrade,qiMenReTradeUpdate);

            for (QiMenOrder qiMenOrder : qiMenOrders){
                UpdateWrapper<QiMenOrder> qiMenOrderUpdate = new UpdateWrapper<>();
                qiMenOrderUpdate.eq("tid",existReTrade.getTid()).eq("oid",qiMenOrder.getOid());
                qiMenOrderService.update(qiMenOrder,qiMenOrderUpdate);
            }



        }else {
            qiMenReTradeService.save(qiMenReTrade);
            qiMenOrderService.saveBatch(qiMenOrders);
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

    public static QiMenReTrade encReTrade(JSONObject tradeJsonObj){
        QiMenReTrade qiMenReTrade = new QiMenReTrade();

        String snapshotUrl = tradeJsonObj.getString("snapshotUrl");
        String tid = tradeJsonObj.getString("tid");
        String modified = tradeJsonObj.getString("modified");
        String sellerMemo = tradeJsonObj.getString("sellerMemo");
        String created = tradeJsonObj.getString("created");
        String payTime = tradeJsonObj.getString("payTime");
        String status = tradeJsonObj.getString("status");
        String receiverCity = tradeJsonObj.getString("receiverCity");
        String buyerMemo = tradeJsonObj.getString("buyerMemo");
        String isPartConsign = tradeJsonObj.getString("isPartConsign");
        String sellerNick = tradeJsonObj.getString("sellerNick");

        qiMenReTrade.setSnapshotUrl(snapshotUrl);
        qiMenReTrade.setTid(tid);
        qiMenReTrade.setModified(modified);
        qiMenReTrade.setSellerMemo(sellerMemo);
        qiMenReTrade.setCreated(created);
        qiMenReTrade.setPayTime(payTime);
        qiMenReTrade.setStatus(status);
        qiMenReTrade.setReceiverCity(receiverCity);
        qiMenReTrade.setBuyerMemo(buyerMemo);
        qiMenReTrade.setIsPartConsign(isPartConsign);
        qiMenReTrade.setSellerNick(sellerNick);

        return qiMenReTrade;
    }

    public static QiMenOrder encOrder(JSONObject orderJsonObj){
        String consignTime = orderJsonObj.getString("consignTime");
        String adjustFee = orderJsonObj.getString("adjustFee");
        String num = orderJsonObj.getString("num");
        String shippingType = orderJsonObj.getString("shippingType");
        String numIid = orderJsonObj.getString("numIid");
        String oid = orderJsonObj.getString("oid");
        String title = orderJsonObj.getString("title");
        String price = orderJsonObj.getString("price");
        String totalFee = orderJsonObj.getString("totalFee");
        String refundStatus = orderJsonObj.getString("refundStatus");
        String invoiceNo = orderJsonObj.getString("invoiceNo");
        String refundId = orderJsonObj.getString("refundId");
        String outerIid = orderJsonObj.getString("outerIid");
        String logisticsCompany = orderJsonObj.getString("logisticsCompany");
        String expandCardExpandPriceUsedSuborder = orderJsonObj.getString("expandCardExpandPriceUsedSuborder");
        String expandCardBasicPriceUsedSuborder = orderJsonObj.getString("expandCardBasicPriceUsedSuborder");
        String status = orderJsonObj.getString("status");

        QiMenOrder qiMenOrder = new QiMenOrder();
        qiMenOrder.setConsignTime(consignTime);
        qiMenOrder.setAdjustFee(adjustFee);
        qiMenOrder.setNum(num);
        qiMenOrder.setShippingType(shippingType);
        qiMenOrder.setNumIid(numIid);
        qiMenOrder.setOid(oid);
        qiMenOrder.setTitle(title);
        qiMenOrder.setPrice(price);
        qiMenOrder.setTotalFee(totalFee);
        qiMenOrder.setRefundStatus(refundStatus);
        qiMenOrder.setInvoiceNo(invoiceNo);
        qiMenOrder.setRefundId(refundId);
        qiMenOrder.setOuterIid(outerIid);
        qiMenOrder.setLogisticsCompany(logisticsCompany);
        qiMenOrder.setExpandCardExpandPriceUsedSuborder(expandCardExpandPriceUsedSuborder);
        qiMenOrder.setExpandCardBasicPriceUsedSuborder(expandCardBasicPriceUsedSuborder);
        qiMenOrder.setStatus(status);

        return  qiMenOrder;
    }
}
