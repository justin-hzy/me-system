package com.me.modules.order.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.me.modules.order.service.OrderDetailService;
import com.me.modules.sys.service.HttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    final private HttpService httpService;

    final private String appName = "wmt";

    final private String appKey = "d29851b508704fda872360e3760e3b1e";

    final private String sid = "wmt";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getOrderDetail() {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesTradeOrderList";

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

        Map<String,Object> body = new HashMap<>();
        List<String> tradeIds = new ArrayList<>();
        tradeIds.add("47936");
        //tradeIds.add("47938");
        body.put("tradeIds",tradeIds);

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        //log.info("加密前字符串="+signBe);
        String sign = SecureUtil.md5(signBe);
        //log.info("sign="+sign);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+httpService.urlencode(parameter);
        log.info("fullUrl="+fullUrl);
        log.info(JSON.toJSONString(body));
        String response = httpService.sendPostRequest(fullUrl,header,body);
        log.info("response="+response);
    }
}
