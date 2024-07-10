package com.me.modules.order.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.sys.service.HttpService;
import com.me.modules.order.service.AbroadWareHouseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class AbroadWareHouseServiceImpl implements AbroadWareHouseService {

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    private HttpService httpService;

    @Override
    public String getWarehouseNo(List<String> warehouseName) {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/warehouse/v1/getWarehouseList";
        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        //JSONObject body = new JSONObject();
//        List list = new ArrayList();
//        list.add("精越-云仓");
        Map<String,Object> body = new HashMap<>();
        body.put("is_disabled",0);
        body.put("warehouse_name_list",warehouseName);

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+httpService.urlencode(parameter);
        String response = httpService.sendPostRequest(fullUrl,header,body);
        //log.info("response="+response);
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONArray("data");
        return dataResultArray.getJSONObject(0).getString("warehouseNo");
    }
}
