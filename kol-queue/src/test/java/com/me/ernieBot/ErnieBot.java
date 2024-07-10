package com.me.ernieBot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ErnieBot {

    @Test
    public void Test(){
       String responseBody = getToken();
       System.out.println(responseBody);
       JSONObject dataJsonObject = JSON.parseObject(responseBody);
       String token = dataJsonObject.getString("access_token");
       System.out.println(token);

    }

    public static String getToken(){
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("client_id","R4TuqA6ERlWusCflEzdTlYEr");
        params.put("client_secret","bV6xb2GYVGHnKSblwDoetbix6C6i452j");
        params.put("grant_type","client_credentials");
        String base_url = "https://aip.baidubce.com/oauth/2.0/token";
        String full_url = base_url + "?" + urlencode(params);
        return send_post_request(full_url);
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

    public static String send_post_request(String url) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","application/json");
            httpPost.setHeader("Accept","application/json");
            StringEntity requestEntity = new StringEntity("");
            httpPost.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
