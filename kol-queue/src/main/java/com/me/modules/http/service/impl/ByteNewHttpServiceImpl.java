package com.me.modules.http.service.impl;

import com.alibaba.fastjson.JSON;
import com.me.modules.http.service.ByteNewHttpService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class ByteNewHttpServiceImpl implements ByteNewHttpService {
    @Override
    public String generate_sign(Map<String, String> params, String app_secret) {
        // 排除 sign 参数
        params.remove("sign");

        // 按照字母先后顺序对参数进行排序
        List<Map.Entry<String, String>> sorted_params = new ArrayList<>(params.entrySet());
        Collections.sort(sorted_params, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        // 将排序后的参数拼接为字符串
        StringBuilder sign_str = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted_params) {
            sign_str.append(entry.getKey()).append(entry.getValue());
        }

        // 将 app_secret 添加到字符串头尾
        sign_str.insert(0, app_secret);
        sign_str.append(app_secret);
        System.out.println("sign_str="+sign_str);

        // 进行 MD5 加密
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(sign_str.toString().getBytes("UTF-8"));
            StringBuilder sign = new StringBuilder();
            for (byte b : digest) {
                sign.append(String.format("%02X", b));
            }
            return sign.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String urlencode(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return encodedParams.toString();
    }

    @Override
    public String send_get_request(String url) {
        try {
            // 原来申请创建httpClient语句
            // HttpClient httpClient = HttpClientBuilder.create().build();
            // 替换 设置了连接超时时间、请求超时时间以及重试机制
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (executionCount > 3) {
                            // 如果重试次数超过3次，则放弃重试
                            return false;
                        }
                        // 在这里可以根据具体的异常类型进行判断是否需要重试
                        return true;
                    })
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent","Apifox/1.0.0 (https://apifox.com)");
            httpGet.setHeader("Content-type","Application/json");
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String transUrl() {
        return null;
    }

    @Override
    public String send_post_request(String url,Map<String, String> params) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","Application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public String send_post_request_1(String url, List<NameValuePair> params) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
