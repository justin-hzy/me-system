package com.me.modules.json.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.json.JsonService;
import com.me.modules.sale.in.dto.PutInOrderDto;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.in.pojo.InGood;
import com.me.modules.sale.out.dto.PutOutOrderDto;
import com.me.modules.sale.out.pojo.OutGood;
import com.me.modules.refund.dto.PutRefundDto;
import com.me.modules.refund.pojo.RefundGood;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JsonServiceImpl implements JsonService {

    @Override
    public JSONObject createPutOutOrderJson(PutOutOrderDto dto) {
        JSONObject param = new JSONObject();
        param.put("status",dto.getStatus());
        param.put("warehouseId",dto.getWarehouseId());
        param.put("type",dto.getType());
        param.put("channelSource",dto.getChannelSource());
        param.put("nodeSn",dto.getNodeSn());
        param.put("consigneeName",dto.getConsigneeName());
        param.put("consigneePhone",dto.getConsigneePhone());
        param.put("province",dto.getProvince());
        param.put("city",dto.getCity());
        param.put("district",dto.getDistrict());
        param.put("postalCode",dto.getPostalCode());
        param.put("consigneeAddress",dto.getConsigneeAddress());
        param.put("deliveryWay",dto.getDeliveryWay());
        param.put("outTime",dto.getOutTime());
        param.put("goodsStatus",dto.getGoodsStatus());
        param.put("remark",dto.getGoodsStatus());
        param.put("orderSn",dto.getOrderSn());

        List<OutGood> outGoods = dto.getGoods();

        JSONArray goodList = new JSONArray();

        for (int j = 0;j<outGoods.size();j++){
            OutGood outGood = outGoods.get(j);
            int i = j+1;
            JSONObject goodMap = new JSONObject();
            goodMap.put("i",i);
            goodMap.put("barCode",outGood.getBarCode());
            goodMap.put("goodsName",outGood.getGoodsName());
            goodMap.put("specification",outGood.getSpecification());
            goodMap.put("num",outGood.getNum());
            goodMap.put("price",outGood.getPrice());
            goodMap.put("remark",outGood.getRemark());

            goodList.add(goodMap);
        }

        param.put("goods",goodList.toJSONString());

        return param;
    }

    @Override
    public JSONObject createTransOutOrderDtlJson(String  orderSn) {
        JSONObject param = new JSONObject();
        param.put("orderSn",orderSn);
        return param;
    }

    @Override
    public JSONObject createTransInOrderDtlJson(FlashInOrder flashInOrder) {
        JSONObject param = new JSONObject();
        param.put("orderSn",flashInOrder.getOrderSn());
        return param;
    }

    @Override
    public JSONObject createPutRefundJson(PutRefundDto dto) {
        JSONObject param = new JSONObject();
        param.put("externalOrderSn",dto.getExternalOrderSn());
        param.put("backType",dto.getBackType());
        param.put("senderAddress",dto.getSenderAddress());
        param.put("province",dto.getProvince());
        param.put("city",dto.getCity());
        param.put("district",dto.getDistrict());
        param.put("postalCode",dto.getPostalCode());
        param.put("senderName",dto.getSenderName());
        param.put("senderPhone",dto.getSenderPhone());
        param.put("backPayMode",dto.getBackPayMode());
        param.put("warehouseId",dto.getWarehouseId());

        JSONArray goodsJsonArr = new JSONArray();
        List<RefundGood> refundGoods = dto.getGoods();

        for (RefundGood refundGood : refundGoods){
            JSONObject goodsJson = new JSONObject();
            goodsJson.put("barCode",refundGood.getBarCode());
            goodsJson.put("num",refundGood.getNum());
            goodsJson.put("price",refundGood.getPrice());

            goodsJsonArr.add(goodsJson);
        }

        param.put("goods",goodsJsonArr.toJSONString());
        return param;
    }

    @Override
    public JSONObject createPutInOrderJson(PutInOrderDto dto) {
        JSONObject param = new JSONObject();
        param.put("warehouseId",dto.getWarehouseId());
        param.put("type",dto.getType());
        param.put("qualityStatus",dto.getQualityStatus());
        param.put("orderSn",dto.getOrderSn());
        param.put("channelSource",dto.getChannelSource());
        param.put("deliveryWay",dto.getDeliveryWay());
        param.put("supplyCode",dto.getSupplyCode());
        param.put("carrier",dto.getCarrier());
        param.put("deliveryMan",dto.getDeliveryMan());
        param.put("plateNumber",dto.getPlateNumber());
        param.put("deliveryNumber",dto.getDeliveryNumber());
        param.put("deliveryContact",dto.getDeliveryContact());
        param.put("remark",dto.getRemark());
        param.put("arrivalStart",dto.getArrivalStart());
        param.put("arrivalEnd",dto.getArrivalEnd());
        param.put("check",dto.getCheck());
        param.put("ignorePrice",dto.getIgnorePrice());
        param.put("isBatchArrival",dto.getIsBatchArrival());

        JSONArray goodsJsonArr = new JSONArray();
        List<InGood> inGoods = dto.getGoods();

        for (InGood inGood : inGoods){
            JSONObject goodsJson = new JSONObject();
            goodsJson.put("i",inGood.getI());
            goodsJson.put("barCode",inGood.getBarCode());
            goodsJson.put("num",inGood.getNum());
            goodsJson.put("price",inGood.getPrice());

            goodsJsonArr.add(goodsJson);
        }

        param.put("goods",goodsJsonArr.toJSONString());

        return param;
    }
}
