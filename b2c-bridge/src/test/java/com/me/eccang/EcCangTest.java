package com.me.eccang;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.utils.EcHttpUtil;

import com.me.modules.eccang.entity.EcOrder;
import com.me.modules.eccang.entity.EcReOrder;
import com.me.modules.eccang.entity.ReOrderCollArr;
import com.me.modules.eccang.service.EcReOrderService;
import com.me.modules.eccang.service.ReOrderCollArrService;
import com.me.modules.eccang.service.TranService;
import com.me.modules.fl.service.FlOrderService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@SpringBootTest
@Slf4j
public class EcCangTest {

    @Autowired
    FlOrderService flOrderService;

    @Autowired
    TranService tranService;


    @Test
    public void test(){
        transOrderList();
    }

    /**
     * 查询订单列表示例
     */
    public void transOrderList() {

        String service = "getOrderList";
        String params = "{\"page\":1,\"pageSize\":2}";
        JSONObject result = EcHttpUtil.soapRequest(service, "eb", params);
        System.out.println(result);

        /*String code = result.getString("code");

        String message = result.getString("message");
        if("200".equals(code) && "Success".equals(message)){

            //获取data返回字段
            String platform = result.getString("platform");
            String referenceNo = result.getString("referenceNo");
            String orderCode = result.getString("orderCode");
            String smCode = result.getString("smCode");
            String addTime = result.getString("addTime");
            String orderPaydate = result.getString("orderPaydate");
            String warehouseCode = result.getString("warehouseCode");
            String warehouseName = result.getString("warehouseName");
            String serviceProviderCode = result.getString("serviceProviderCode");
            String serviceProviderName = result.getString("serviceProviderName");
            String channelCode = result.getString("channelCode");
            String channelName = result.getString("channelName");
            String sellerId = result.getString("sellerId");
            String ebayTotal = result.getString("ebay_total");
            String currencyCode = result.getString("currency_code");
            String taxNumber = result.getString("tax_number");
            String ioss = result.getString("ioss");
            String eori = result.getString("eori");
            String cpf = result.getString("cpf");
            String orderStatus = result.getString("orderStatus");








        }else {
            //收集错误信息
        }*/
    }


    @Test
    public void transReOrderList(){

        Integer page = 1;
        Integer pageSize = 100;

        JSONObject resp = tranService.tranEcReOrder(page,pageSize);

        Integer total = resp.getInteger("total");

        for (;total>page*pageSize;page++){
            tranService.tranEcReOrder(page,pageSize);
        }


    }
}
