package com.me.json;

import com.alibaba.fastjson.JSON;
import com.me.modules.sys.http.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TokenTest {

    static final String TARGET_URL = "https://cbs8-openapi-reprd.csuat.cmburl.cn/openapi/app/v1/app/token";


    private String app_id = "fNVMWxuR";

    private String app_secret = "6eb5da3f18bd5684090147ddf6dc80e19f8a4892";

    private String grant_type = "client_credentials";

    @Autowired
    private HttpService httpService;

    @Test
    public void queryToken() throws Exception {
        String baseUrl = "https://cbs8-openapi-reprd.csuat.cmburl.cn/openapi/app/v1/app/token";

        Map<String,String> header = new HashMap<>();

        Map<String,String> body = new HashMap<>();
        body.put("app_id","fNVMWxuR");
        body.put("app_secret","6eb5da3f18bd5684090147ddf6dc80e19f8a4892");
        body.put("grant_type","client_credentials");

        String response = httpService.sendPostRequest(baseUrl,header,body);

        String token = JSON.parseObject(response).getJSONObject("data").getString("token");

        System.out.println(token);
    }

    public static String sendPostRequest(String url, Map<String, String> header, Map<String,String> body) {
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
