package com.me.modules.json.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.SfConfig;
import com.me.modules.json.service.JsonService;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.pur.pojo.RefundGood;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.pojo.SaleGood;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class JsonServiceImpl implements JsonService {

    private SfConfig sfConfig;

    @Override
    public JSONObject createSaleOrderJson(PutSaleOrderDto dto) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("CompanyCode",dto.getCompanyCode());
        paramJson.put("AccessCode",sfConfig.getAccessCode());
        paramJson.put("Checkword",sfConfig.getCheckWord());
        paramJson.put("ServiceCode",sfConfig.getSaleOrderServiceCode());

        JSONArray saleOrdersJsonArr = new JSONArray();

        JSONObject saleOrderJson = new JSONObject();
        //默认为 10  销售订单
        saleOrderJson.put("SfOrderType",dto.getSfOrderType());
        saleOrderJson.put("ErpOrder",dto.getErpOrder());
        saleOrderJson.put("DestinationFacilityAliasId",dto.getDestinationFacilityAliasId());
        saleOrderJson.put("OrderNote",dto.getOrderNote());
        saleOrderJson.put("TradeOrderDateTime",dto.getTradeOrderDateTime());
        /*saleOrderJson.put("SfOrderType","10");
        saleOrderJson.put("ErpOrder","DMS-20241205001");
        saleOrderJson.put("DestinationFacilityAliasId","");
        saleOrderJson.put("OrderNote","测试");
        saleOrderJson.put("TradeOrderDateTime","2024-12-05 00:00:05");*/

        JSONObject orderReceiverInfoJson = new JSONObject();
        orderReceiverInfoJson.put("ReceiverCompany",dto.getReceiverCompany());
        orderReceiverInfoJson.put("ReceiverName",dto.getReceiverName());
        orderReceiverInfoJson.put("ReceiverMobile",dto.getReceiverMobile());
        orderReceiverInfoJson.put("ReceiverCountry",dto.getReceiverCountry());
        orderReceiverInfoJson.put("ReceiverAddress",dto.getReceiverAddress());
        /*orderReceiverInfoJson.put("ReceiverCompany","顺丰");
        orderReceiverInfoJson.put("ReceiverName","周宇");
        orderReceiverInfoJson.put("ReceiverMobile","12288767414");
        orderReceiverInfoJson.put("ReceiverCountry","泰国");
        orderReceiverInfoJson.put("ReceiverAddress","หอเมธาลดา ซอยวุ่นวาย ต.ท่าขอนยาง");*/
        saleOrderJson.put("OrderReceiverInfo",orderReceiverInfoJson);

        JSONObject orderSenderInfoJson = new JSONObject();
        orderSenderInfoJson.put("SenderCompany",dto.getSenderCompany());
        orderSenderInfoJson.put("SenderName",dto.getSenderName());
        /*orderSenderInfoJson.put("SenderCompany","顺丰");
        orderSenderInfoJson.put("SenderName","周宇");*/
        saleOrderJson.put("OrderSenderInfo",orderSenderInfoJson);

        //设置承运商
        JSONObject OrderCarrierJson = new JSONObject();
        OrderCarrierJson.put("Carrier","ZT");
        saleOrderJson.put("OrderCarrier",OrderCarrierJson);


        JSONArray orderItemsJsonArr = new JSONArray();

        List<SaleGood> saleGoods = dto.getSaleGoods();
        for (SaleGood good : saleGoods){
            JSONObject orderItemJson = new JSONObject();

            orderItemJson.put("SkuNo",good.getSkuNo());
            orderItemJson.put("ItemQuantity",good.getItemQuantity());
            orderItemJson.put("InventoryStatus",good.getInventoryStatus());
            orderItemsJsonArr.add(orderItemJson);
            /*orderItemJson.put("SkuNo","TEST2");
            orderItemJson.put("ItemQuantity","2");
            orderItemJson.put("InventoryStatus","正品");*/
        }


        saleOrderJson.put("OrderItems",orderItemsJsonArr);

        saleOrdersJsonArr.add(saleOrderJson);

        paramJson.put("SaleOrders",saleOrdersJsonArr);

        return paramJson;
    }

    @Override
    public JSONObject createSaleOrderDtlJson(ThSaleOrder thSaleOrder) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("CompanyCode",sfConfig.getCompanyCode());
        paramJson.put("AccessCode",sfConfig.getAccessCode());
        paramJson.put("Checkword",sfConfig.getCheckWord());
        paramJson.put("ServiceCode",sfConfig.getSaleOrderDtlServiceCode());


        List<String> erpOrderCodes = new ArrayList<>();
        erpOrderCodes.add(thSaleOrder.getErpOrder());

        JSONArray saleOrderJsonArr = new JSONArray();

        if (CollUtil.isNotEmpty(erpOrderCodes)){
            for (String erpOrderCode : erpOrderCodes){
                JSONObject saleOrderJson = new JSONObject();
                saleOrderJson.put("ErpOrder",erpOrderCode);
                saleOrderJsonArr.add(saleOrderJson);
            }
        }

        paramJson.put("SaleOrders",saleOrderJsonArr);



        return paramJson;
    }

    @Override
    public JSONObject createPurOrderJson(PutRefundPurDto dto) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("CompanyCode",sfConfig.getCompanyCode());
        paramJson.put("AccessCode",sfConfig.getAccessCode());
        paramJson.put("Checkword",sfConfig.getCheckWord());
        paramJson.put("ServiceCode",sfConfig.getPurOrderServiceCode());

        JSONArray purOrdersJsonArr = new JSONArray();

        JSONObject purOrderJson = new JSONObject();

        //purOrderJson.put("WarehouseCode","ZATH");
        purOrderJson.put("WarehouseCode",dto.getWarehouseCode());
        //purOrderJson.put("ErpOrder","DMS-20241218002");
        purOrderJson.put("ErpOrder",dto.getErpOrder());



        //默认为 20 退货入库
        //purOrderJson.put("ErpOrderType","20");
        purOrderJson.put("ErpOrderType",dto.getErpOrderType());
        purOrderJson.put("SFOrderType",dto.getSfOrderType());
        purOrderJson.put("TransferWarehouseCode","");

        JSONArray itemsJsonArr = new JSONArray();

        List<RefundGood> goods = dto.getGoods();
        for (RefundGood good : goods){

            JSONObject itemJson = new JSONObject();
            itemJson.put("SkuNo",good.getSkuNo());
            itemJson.put("Qty",good.getQty());
            itemsJsonArr.add(itemJson);
        }

        //itemJson.put("SkuNo","TEST2");
        //itemJson.put("Qty","30");
        //itemsJsonArr.add(itemJson);
        purOrderJson.put("Items",itemsJsonArr);


        purOrdersJsonArr.add(purOrderJson);

        paramJson.put("PurchaseOrders",purOrdersJsonArr);

        return paramJson;
    }

    @Override
    public JSONObject createPurOrderDtlJson(ThRefund thRefund) {
        JSONObject paramJson = new JSONObject();
        paramJson.put("CompanyCode",sfConfig.getCompanyCode());
        paramJson.put("AccessCode",sfConfig.getAccessCode());
        paramJson.put("Checkword",sfConfig.getCheckWord());
        paramJson.put("ServiceCode",sfConfig.getPurOrderDtlServiceCode());


        List<String> erpOrderCodes = new ArrayList<>();
        erpOrderCodes.add(thRefund.getErpOrder());

        JSONArray purOrderJsonArr = new JSONArray();
        if (CollUtil.isNotEmpty(erpOrderCodes)){
            for (String erpOrderCode : erpOrderCodes){
                JSONObject purOrderJson = new JSONObject();
                purOrderJson.put("ErpOrder",erpOrderCode);
                purOrderJsonArr.add(purOrderJson);
            }
        }

        paramJson.put("PurchaseOrders",purOrderJsonArr);

        return paramJson;
    }
}
