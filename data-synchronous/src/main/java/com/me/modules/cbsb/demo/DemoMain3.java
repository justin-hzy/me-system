package com.me.modules.cbsb.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class DemoMain3 {
    public static void main(String[] args) {
        String url = "http://39.108.136.182:8080/api/cube/restful/interface/saveOrUpdateModeData/ModeDataService_44";
        String dataJson = "{ \"data\":[ { \"operationinfo\":{ \"operationDate\":\"2023-09-14\", \"operator\":\"15273\", \"operationTime\":\"10:38:27\" }, \"mainTable\":{ \"xjlx\":\"\", \"fkrq\":\"2023-09-08\", \"khjsfs\":\"\", \"skyh\":\"工商银行测试支行\", \"szkh\":\"\", \"skzh\":\"测试账号\", \"skgs01\":\"屈臣氏\", \"fkdw\":\"猎尚\", \"fkyh\":\"招商银行测试支行\", \"je\":\"99.99\", \"fkzh\":\"688989221323\", \"fkfs\":\"电汇\", \"zy\":\"test\" } } ], \"header\":{ \"systemid\":\"44E03EF5B5D5E628DC5CAB13A82251EB\", \"currentDateTime\":\"20230914103827\", \"Md5\":\"e2c19bcd41fbdb4d99f232ec60b81ee7\" } }";

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 设置请求体参数
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("datajson", dataJson));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 发送请求并获取响应
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            // 处理响应
            System.out.println(responseString);

            // 关闭HttpClient
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
