package com.me.bos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
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

import java.util.*;

@SpringBootTest
@Slf4j
public class BosTest6 {

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Test
    public void getSalesRefundList(){
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesRefundList";

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
//        body.put("order_status",80);
//        body.put("status",0);
        body.put("createTimeBegin","2023-12-01 00:00:00");
        body.put("createTimeEnd","2023-12-31 23:59:59");
//        body.put("start_time","2023-11-19 00:00:00");
//        body.put("end_time","2023-11-30 23:59:59");
        //JY202312010003

        signMap.put("body", JSON.toJSONString(body));
        String signBe = linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = sendPostRequest(baseUrl,header,body);
        log.info("response="+response);
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

    public static String urlencode(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return encodedParams.toString();
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
            /*for (Map.Entry<String, Integer> entry : body.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }*/
            /*for(String key : body.keySet()){
                params.add(new BasicNameValuePair(key,body.getString(key)));

            }

            HttpEntity entity = new UrlEncodedFormEntity(params);
            httpPost.setEntity(entity);*/

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

            // 封装body参数
            StringEntity entity = new StringEntity(JSON.toJSONString(body),"utf-8");
            log.info(JSON.toJSONString(body));
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
