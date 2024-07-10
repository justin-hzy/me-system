package com.me.bos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.order.shop.service.ShopRelService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInListService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInService;
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

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@Slf4j
public class OrderProfitTest {

    @Autowired
    private WangDianAbroadStockInService wangDianAbroadStockInService;

    @Autowired
    private WangDianAbroadStockInListService wangDianAbroadStockInListService;

    @Autowired
    private ShopRelService shopRelService;

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Test
    public void test(){
        BigDecimal number = new BigDecimal("0.000");
        boolean isZero = number.compareTo(BigDecimal.ZERO) == 0;
        System.out.println(isZero);
    }

    @Test
    public void getOrderProfit(){
        String baseUrl = "https://coss.qizhishangke.com/api/openservices/V1/finances/pageReportByOrderItems";
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

        Map<String,Object> body = new HashMap<>();
        body.put("pageNum",1);
        body.put("pageSize",100);
        body.put("skuSearchType",1);
        body.put("profitCalculateType",1);
        body.put("skuSearchVal","74384037802");
        body.put("numberType",1);
        List<String> numberList = new ArrayList();
        numberList.add("JY202312310100");
        body.put("numberVal",numberList);

        signMap.put("body", JSON.toJSONString(body));
        String signBe = linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = sendPostRequest(baseUrl,header,body);
        log.info("response="+response);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("list");
        JSONObject element = dataResultArray.getJSONObject(0);
        String tradeNo = element.getString("tradeNo");
        String tid = element.getString("tid");
        String srcOid = element.getString("srcOid");
        BigDecimal exchangeRate = element.getBigDecimal("exchangeRate");

        BigDecimal totalIncome = element.getBigDecimal("totalIncome");
        if(!(totalIncome.compareTo(BigDecimal.ZERO) == 0)){
            totalIncome = totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(exchangeRate);
        }

        log.info("tradeNo="+tradeNo+",tid="+tid+",srcOid="+srcOid+",totalIncome="+totalIncome+",exchangeRate="+exchangeRate);

        /*BigDecimal exchangeRate_1 = new BigDecimal("0.0003");
        log.info("人民币收入2="+totalIncome.multiply(exchangeRate_1));*/



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
