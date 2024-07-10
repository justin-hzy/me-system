package com.me.bos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.utils.DateUtils;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.stockout.service.TransAdStockOutOrderService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@Slf4j
public class BosTest0 {



    @Autowired
    private TransAdStockOutOrderService transAdStockOutOrderService;

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Test
    public void test1(){
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesTradeList";

        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);
        log.info("timestamp="+timestamp);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        JSONObject body = new JSONObject();
        body.put("pageNo",1);
        body.put("pageSize",200);
        body.put("tradeStatusCode",4);
        body.put("createTimeBegin","2023-11-22 00:00:00");
        body.put("createTimeEnd","2023-11-25 15:00:00");

        signMap.put("body",body.toJSONString());
        String signBe = linkParams(signMap,appKey);
        log.info("加密前字符串="+signBe);
        String sign = SecureUtil.md5(signBe);
        log.info("sign="+sign);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+urlencode(parameter);
        log.info("fullUrl="+fullUrl);
        log.info(body.toJSONString());
        String response = sendPostRequest(fullUrl,header,body);
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
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }







    @Test
    public void timeTest(){
        //2023-11-22 00:00:00
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        log.info(formattedDate);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date tomorrowDate = calendar.getTime();
        String tomorrowDateTime = sdf.format(tomorrowDate);
        log.info("明天时间：" + tomorrowDateTime);
    }

    @Test
    public void getWarehouseList(){
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
        List list = new ArrayList();
        list.add("精越-云仓");
        Map<String,Object> body = new HashMap<>();
        body.put("is_disabled",0);
        body.put("warehouse_name_list",list);
        //body.put("status",1);
//        body.put("start_time", "2020-12-18 00:00:00");
//        body.put("end_time","2023-12-20 00:00:00");

        signMap.put("body",JSON.toJSONString(body));
        String signBe = linkParams(signMap,appKey);
        //log.info("加密前字符串="+signBe);
        String sign = SecureUtil.md5(signBe);
        //log.info("sign="+sign);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+urlencode(parameter);
        String response = sendPostRequest(fullUrl,header,body);
        log.info("response="+response);
    }










    @Test
    public void getResponse(){
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesTradeList";
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
        Map<String,Object> body = new HashMap<>();
        body.put("pageNo",1);
        body.put("pageSize",200);
        body.put("tradeStatusCode",4);
        body.put("createTimeBegin", "2024-01-01 00:00:00");
        body.put("createTimeEnd","2024-01-31 23:59:59");
//        body.put("createTimeBegin", DateUtils.getAboardCreateDate());
//        body.put("createTimeEnd",DateUtils.getAboardEndDate());
//        log.info(DateUtils.getAboardCreateDate());
//        log.info(DateUtils.getAboardEndDate());

        signMap.put("body",JSON.toJSONString(body));
        String signBe = this.linkParams(signMap,appKey);
        //log.info("加密前字符串="+signBe);
        String sign = SecureUtil.md5(signBe);
        //log.info("sign="+sign);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+this.urlencode(parameter);
        String response = this.sendPostRequest(fullUrl,header,body);
        log.info("response="+response);


        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("data");
        JSONObject jsonObj = dataJsonObject.getJSONObject("data");
        Boolean empty = jsonObj.getBoolean("empty");
        Integer pageSize = jsonObj.getInteger("pageSize");
        Integer total = jsonObj.getInteger("total");
        List<WangDianAbroadStockOut> wangDianAbroadStockOutList = new ArrayList<>();
        log.info("total="+total);
    }



}
