package com.me.modules.http.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.http.service.SlHttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class SlHttpServiceImpl implements SlHttpService {
    @Override
    public JSONObject doAction(String url, List<NameValuePair> params) throws IOException {
        JSONObject apiRes = new JSONObject();

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //restful接口url
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        // 设置Entity
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        httpPost.setEntity(entity);

        response = httpClient.execute(httpPost);

        if (response != null && response.getEntity() != null) {
            //返回信息
            String resulString = EntityUtils.toString(response.getEntity());
            log.info("获取接口数据成功，接口返回体：" + resulString);
            //处理返回信息
            apiRes = JSON.parseObject(resulString);
        }else{
            apiRes.put("result","false");
            apiRes.put("message","接口错误返回错误！");
            log.info("获取接口数据失败，接口返回体为空！");
        }



        return apiRes;
    }
}
