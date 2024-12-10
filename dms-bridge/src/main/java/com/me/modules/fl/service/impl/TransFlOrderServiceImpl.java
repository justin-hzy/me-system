package com.me.modules.fl.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.FlConfig;
import com.me.modules.fl.dto.TransFlOrderDto;
import com.me.modules.fl.service.TransFlOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransFlOrderServiceImpl implements TransFlOrderService {

    private FlConfig flConfig;

    @Override
    public void TransFlOrder(TransFlOrderDto dto) {

        JSONObject apiRes = new JSONObject();

        List<String> nameList = dto.getNameList();

        String url = "https://upace-api.ibiza.com.tw/v1/orders/";

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        if(CollUtil.isNotEmpty(nameList)){
            for (String name : nameList){
                url = url +name;
                log.info("url="+url);
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("Content-Type","application/json;charset=utf-8");

                String authorization = flConfig.getAuthorization();

                httpGet.addHeader("Authorization", authorization);

                try{
                    response = httpClient.execute(httpGet);
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
                } catch (Exception e) {
                    apiRes.put("result","false");
                    apiRes.put("message","接口请求失败！");
                    log.info("请求失败:"+e);
                }
            }
        }
    }
}
