package com.me.bos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@Slf4j
public class BosTest2 {

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Test
    public void getFinance(){
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
        body.put("orderSearchContent","JY202312150032");
        body.put("orderSearchType",1);
        //JY202312010003

        signMap.put("body", JSON.toJSONString(body));
        String signBe = linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = sendPostRequest(baseUrl,header,body);
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
    }

    public static String linkParams(Map<String, String> map, String secret) {
        StringBuilder sb = new StringBuilder();
        sb.append(secret);
        for (Map.Entry<String, String> item : map.entrySet())
        {
            if (item.getKey().equals("sign"))
                continue;
            sb.append(item.getKey());
            sb.append(item.getValue());
        }
        sb.append(secret);
        return  sb.toString();
    }

    public static String sendPostRequest(String url, Map<String, String> header) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (executionCount > 3) {
                            return false;
                        }
                        return true;
                    })
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            // 设置header参数
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            // 封装body参数
            List<NameValuePair> params = new ArrayList<>();

            Map<String,Object> map = new HashMap<>();
//            map.put("tradeStatusCode",4);
//            map.put("pageNo",1);
//            map.put("pageSize",200);
//            map.put("createTimeBegin","2023-11-22 00:00:00");
//            map.put("createTimeEnd","2023-11-25 15:00:00");
            StringEntity entity = new StringEntity(JSON.toJSONString(map),"utf-8");
            httpPost.setEntity(entity);


            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sendPostRequest(String url, Map<String, String> header, Map<String,Object> body) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (executionCount > 3) {
                            return false;
                        }
                        return true;
                    })
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            // 设置header参数
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            log.info("json="+JSON.toJSONString(body));

            // 封装body参数
            StringEntity entity = new StringEntity(JSON.toJSONString(body),"utf-8");
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
