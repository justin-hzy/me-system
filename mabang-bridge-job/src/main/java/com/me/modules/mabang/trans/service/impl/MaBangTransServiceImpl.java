package com.me.modules.mabang.trans.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.mabang.order.dto.InsertMBOrderDtl;
import com.me.modules.mabang.order.dto.InsertMBOrderDto;
import com.me.modules.mabang.order.entity.MBOrder;
import com.me.modules.mabang.order.service.MBOrderService;
import com.me.modules.mabang.refund.dto.InsertMBRefundDtl;
import com.me.modules.mabang.refund.dto.InsertMBRefundDto;
import com.me.modules.mabang.refund.entity.MBRefund;
import com.me.modules.mabang.refund.service.MBRefundService;
import com.me.modules.mabang.service.MaBangHttpService;
import com.me.modules.mabang.trans.service.MaBangTransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class MaBangTransServiceImpl implements MaBangTransService {


    private MaBangHttpService maBangHttpService;

    private MBOrderService mbOrderService;

    private MBRefundService mbRefundService;



    @Override
    public JSONObject transMaBangOrder(String expressTimeStart,String expressTimeEnd,String cursor) {
        JSONObject returnJSON = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("expressTimeStart",expressTimeStart);
        map.put("expressTimeEnd",expressTimeEnd);
        map.put("maxRows","1000");
        map.put("status","3");
        if(cursor != null){
            map.put("cursor",cursor);
        }
        String data = maBangHttpService.callGwApi("order-get-order-list-new", map);

        JSONObject respJson = JSONObject.parseObject(data);

        String code = respJson.getString("code");
        if("200".equals(code)){
            //log.info("data="+data);
            JSONObject dataJson = respJson.getJSONObject("data");
            String hasNext = dataJson.getString("hasNext");
            log.info("total="+dataJson.getString("total"));
            log.info("hasNext="+hasNext);
            JSONArray dataJsonArray = dataJson.getJSONArray("data");
            for (int i = 0;i<dataJsonArray.size();i++){
                JSONObject element = dataJsonArray.getJSONObject(i);
                String platformOrderId = element.getString("platformOrderId");
                String platformId = element.getString("platformId");

                if(!"其他".equals(platformId)){
                    QueryWrapper<MBOrder> maBangOrderQuery = new QueryWrapper<>();
                    maBangOrderQuery.lambda().eq(MBOrder::getPlatformOrderId,platformOrderId);
                    List<MBOrder> MBOrders = mbOrderService.list(maBangOrderQuery);
                    if(CollUtil.isEmpty(MBOrders)){
                        // 执行保存
                        String currencyId = element.getString("currencyId");
                        String expressTime = element.getString("expressTime");
                        String street1 = element.getString("street1");

                        String shopId = element.getString("shopId");
                        String shopName = element.getString("shopName");

                        //如果店铺名称等于VNLazadaPOP 去掉订单头部 2021894296 的内容
                        if("VNLazadaPOP".equals(shopName) || "VNlazadaPOP".equals(shopName)){
                            if (platformOrderId.contains("2021894296")){
                                platformOrderId = platformOrderId.replace("2021894296","");
                            }
                        }

                        String orderStatus = element.getString("orderStatus");
                        String voucherPriceOrigin = element.getString("voucherPriceOrigin");
                        String subsidyAmountOrigin = element.getString("subsidyAmountOrigin");
                        String shippingTotalOrigin = element.getString("shippingTotalOrigin");
                        String otherIncome = element.getString("otherIncome");
                        String itemTotalOrigin = element.getString("itemTotalOrigin");

                        InsertMBOrderDto dto = new InsertMBOrderDto();
                        dto.setPlatformOrderId(platformOrderId);
                        dto.setCurrencyId(currencyId);
                        dto.setExpressTime(expressTime);
                        dto.setStreet(street1);
                        dto.setShopId(shopId);
                        dto.setShopName(shopName);
                        dto.setOrderStatus(orderStatus);


                        List<InsertMBOrderDtl> dtl = dto.getInsertMBOrderDtls();

                        JSONArray orderItemJsonArray = element.getJSONArray("orderItem");
                        for (int j = 0;j<orderItemJsonArray.size();j++){
                            JSONObject orderItemJson = orderItemJsonArray.getJSONObject(j);
                            String stockSku = orderItemJson.getString("stockSku");
                            String quantity = orderItemJson.getString("quantity");
                            String sellPriceOrigin = orderItemJson.getString("sellPriceOrigin");

                            InsertMBOrderDtl insertMBOrderDtl = new InsertMBOrderDtl();
                            insertMBOrderDtl.setStockSku(stockSku);
                            insertMBOrderDtl.setQuantity(quantity);
                            insertMBOrderDtl.setSellPriceOrigin(sellPriceOrigin);

                            insertMBOrderDtl.setVoucherPriceOrigin(voucherPriceOrigin);
                            insertMBOrderDtl.setSubsidyAmountOrigin(subsidyAmountOrigin);
                            insertMBOrderDtl.setShippingTotalOrigin(shippingTotalOrigin);
                            insertMBOrderDtl.setOtherIncome(otherIncome);
                            insertMBOrderDtl.setItemTotalOrigin(itemTotalOrigin);

                            dtl.add(insertMBOrderDtl);
                        }

                        mbOrderService.insertMBOrder(dto);
                    }else {
                        log.info("订单号:"+platformOrderId);
                    }
                }
            }



            if("false".equals(hasNext)){
                String nextCursor = dataJson.getString("nextCursor");
                log.info("nextCursor="+nextCursor);
                returnJSON.put("nextCursor",nextCursor);
                returnJSON.put("hasNext",hasNext);
            }else {
                String nextCursor = dataJson.getString("nextCursor");
                log.info("nextCursor="+nextCursor);
                returnJSON.put("nextCursor",nextCursor);
                returnJSON.put("hasNext",hasNext);
            }
        }else {

        }

        return returnJSON;
    }

    @Override
    public JSONObject transMaBangRefund(String createDateStart, String createDateEnd, Integer page) {

        JSONObject returnJSON = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("createDateStart",createDateStart);
        map.put("createDateEnd",createDateEnd);
        map.put("rowsPerPage","10");
        //map.put("status","4");
        map.put("page",page);

        String data = maBangHttpService.callGwApi("order-get-return-order-list", map);

        log.info("data="+data);

        JSONObject respJson = JSONObject.parseObject(data);

        String code = respJson.getString("code");
        if("200".equals(code)){
            JSONObject dataJson = respJson.getJSONObject("data");

            Integer pageCount = dataJson.getInteger("pageCount");
            returnJSON.put("pageCount",pageCount);

            JSONArray dataJsonArray = dataJson.getJSONArray("data");

            if (CollUtil.isNotEmpty(dataJsonArray)){
                returnJSON.put("isNext",true);
                page = page + 1;
                returnJSON.put("page",page);

                for (int i = 0;i<dataJsonArray.size();i++){
                    JSONObject element = dataJsonArray.getJSONObject(i);
                    String platformOrderId = element.getString("platformOrderId");

                    QueryWrapper<MBRefund> mbRefundQuery = new QueryWrapper<>();
                    mbRefundQuery.lambda().eq(MBRefund::getPlatformOrderId,platformOrderId);
                    List<MBRefund> mbRefunds = mbRefundService.list(mbRefundQuery);
                    if (CollUtil.isEmpty(mbRefunds)){
                        //插入数据
                        String currencyId = element.getString("currencyId");
                        String refundTime = element.getString("refundTime");
                        String shopId = element.getString("shopId");
                        String status = element.getString("status");

                        InsertMBRefundDto dto = new InsertMBRefundDto();
                        dto.setPlatformOrderId(platformOrderId);
                        dto.setStatus(status);
                        dto.setShopId(shopId);
                        dto.setRefundTime(refundTime);
                        dto.setCurrencyId(currencyId);

                        List<InsertMBRefundDtl> insertMBRefundDtls = dto.getInsertMBRefundDtls();
                        JSONArray itemJsonArray = element.getJSONArray("item");
                        for (int j = 0;j<itemJsonArray.size();j++){
                            JSONObject itemJson = itemJsonArray.getJSONObject(j);
                            InsertMBRefundDtl dtl = new InsertMBRefundDtl();
                            dtl.setQuantity1(itemJson.getString("quantity1"));
                            dtl.setStockSku(itemJson.getString("stockSku"));
                            dtl.setSellPrice(itemJson.getString("sellPrice"));
                            insertMBRefundDtls.add(dtl);
                        }

                        mbRefundService.insertMBRefund(dto);
                    }
                }
            }else {
                returnJSON.put("isNext",false);
            }
        }else {

        }
        return returnJSON;
    }
}
