package com.me.modules.fl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.eccang.entity.EcReOrder;
import com.me.modules.eccang.service.EcReOrderService;
import com.me.modules.fl.service.FlReOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FlReOrderServiceImpl implements FlReOrderService {

    private EcReOrderService ecReOrderService;

    @Override
    public void sendOrder(String param) {

    }

    @Override
    public List<String> queryReOrder() {

        QueryWrapper<EcReOrder> ecReOrderQuery = new QueryWrapper<>();

        ecReOrderQuery.select("ro_code,count(1) as count")
                .eq("status","80")
                .groupBy("ro_code")
                .last("limit 100");

        List<Map<String,Object>> results = ecReOrderService.listMaps(ecReOrderQuery);

        log.info(results.toString());

        for(Map<String,Object> result :results){
            String roCode = result.get("ro_code").toString();
            String count = result.get("count").toString();

            ecReOrderQuery = new QueryWrapper<>();

            ecReOrderQuery.eq("ro_code",roCode).eq("status","80");
            ecReOrderService.list(ecReOrderQuery);
        }

        /*ecReOrderQuery.eq("status","80");
        ecReOrderQuery.last("limit 100");
        List<EcReOrder> ecReOrders= ecReOrderService.list(ecReOrderQuery);*/

        /*List<String> params = new ArrayList<>();

        for (EcReOrder ecReOrder : ecReOrders){

            JSONObject reqJsonObj = new JSONObject();
            reqJsonObj.put("token",ecReOrder.getRoCode());

            JSONArray products = new JSONArray();
            reqJsonObj.put("products",products);

            String param = reqJsonObj.toJSONString();
            params.add(param);
        }*/






        return null;
    }
}
