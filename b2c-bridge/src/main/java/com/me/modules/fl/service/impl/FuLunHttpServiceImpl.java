package com.me.modules.fl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.fl.service.FuLunHttpService;
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

@Service
@Slf4j
@AllArgsConstructor
public class FuLunHttpServiceImpl implements FuLunHttpService {

    @Override
    public JSONObject doGetAction(String url) {

        JSONObject apiRes = new JSONObject();

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        log.info("url="+url);


        //restful接口url
        //HttpPost httpPost = new HttpPost(url);

        HttpGet httpGet = new HttpGet(url);
        //设置请求头
        httpGet.addHeader("Content-Type","application/json;charset=utf-8");
        String authorization = "Bearer gabikYQloi0CRLf-lZK9t-yuu-fN90SrrRcEcixaSCo";

        /*通过仓库指定主体*/
        /*JSONObject jsonObject = JSONObject.parseObject(params);
        String tkczt = jsonObject.getString("kczt");

        if("ZT026".equals(tkczt)){
            String authorizationTW = "Bearer gabikYQloi0CRLf-lZK9t-yuu-fN90SrrRcEcixaSCo";
            authorization = authorizationTW;
        }
        if("ZT021".equals(tkczt)){
            String authorizationHK = "Bearer Hw_m_wYL7l8qaVuJNrgUXBYKn55YQO3w5X-Bdi5FKEo";
            authorization = authorizationHK;
        }
        if("ZT030".equals(tkczt)){
            String authorizationKYJ = "Bearer lr-O4L4eO88m2cqOR15c7NKGeW0ETH-u6o474ohub3c";
            authorization = authorizationKYJ;
        }*/

        if(StrUtil.isNotEmpty(authorization)){
            httpGet.addHeader("Authorization", authorization);

            try{
//                httpPost.setEntity(new StringEntity(params, "UTF-8"));
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
        return apiRes;
    }

    @Override
    public JSONObject doPostAction(String params, String url) {

        JSONObject apiRes = new JSONObject();
        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        String authorization = "";

        // todo 根据参数定义token


        if(StrUtil.isNotEmpty(authorization)){
            httpPost.addHeader("Authorization", authorization);

            try{
                httpPost.setEntity(new StringEntity(params, "UTF-8"));
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
            } catch (Exception e) {
                apiRes.put("result","false");
                apiRes.put("message","接口请求失败！");
                log.info("请求失败:"+e);
            }
        }

        return apiRes;

    }
}
