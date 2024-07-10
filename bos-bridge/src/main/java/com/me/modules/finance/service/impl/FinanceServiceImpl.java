package com.me.modules.finance.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.finance.service.FinanceService;
import com.me.modules.sys.service.HttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceServiceImpl implements FinanceService {

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    private HttpService httpService;

    @Override
    public String getFinanceReport(String orderNo,String sku) {
        String baseUrl = "https://coss.qizhishangke.com/api/openservices/V1/profitReport/finance/sku/list";
        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);

        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        //JSONObject body = new JSONObject();
        Map<String,Object> body = new HashMap<>();
        body.put("pageNo",1);
        body.put("pageSize",200);
        //body.put("startTime", "2023-12-01 00:00:00");
        //body.put("timeType",2);
        //body.put("endTime","2023-12-01 16:00:00");
        body.put("orderSearchContent",orderNo);
        body.put("orderSearchType",1);
        body.put("skuSearchType",1);
        body.put("skuSearchContent",sku);
        //JY202312010003

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = httpService.sendPostRequest(baseUrl,header,body);
        //log.info("response="+response);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONObject("data").getJSONArray("returnList");
        BigDecimal totalRevenue = new BigDecimal("0");
        for(int i=0;i<dataResultArray.size();i++){
            JSONObject dataElement = dataResultArray.getJSONObject(i);

            BigDecimal currentTotalRevenue = dataElement.getBigDecimal("totalRevenue");
            //log.info(currentTotalRevenue.toString());

            int result = currentTotalRevenue.compareTo(BigDecimal.ZERO);
            if (result > 0) {
                BigDecimal rateToCny = dataElement.getBigDecimal("rateToCny");
                currentTotalRevenue = currentTotalRevenue.multiply(rateToCny);
            }
            totalRevenue = totalRevenue.add(currentTotalRevenue);
            log.info("currentTotalRevenue="+currentTotalRevenue.toString());
//            totalRevenue.add(currentTotalRevenue);
//            log.info("接口计算="+totalRevenue.multiply(rateToCny).toString());

//            BigDecimal usaRate = new BigDecimal(0.0000410000);
//            BigDecimal chinaRate = new BigDecimal(7.1023000000);
//            log.info("金蝶计算="+totalRevenue.multiply(usaRate).multiply(chinaRate).toString());
        }
        log.info("totalRevenue="+totalRevenue.toString());
        totalRevenue = totalRevenue.abs();
        return totalRevenue.toString();
    }
}
