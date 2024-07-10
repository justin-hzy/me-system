package com.me.modules.fl.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.me.common.config.FlConfig;
import com.me.modules.fl.entity.FuLunInterface;
import com.me.modules.fl.service.FuLunHttpService;
import com.me.modules.fl.service.FuLunInterfaceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.methods.CloseableHttpResponse;
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


    private FuLunInterfaceService fuLunInterfaceService;

    private FlConfig flConfig;

    @Override
    public JSONObject doAction(String apiId, String params) {

        JSONObject apiRes = new JSONObject();

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        QueryWrapper<FuLunInterface> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("id",apiId);
        FuLunInterface fuLunInterface = fuLunInterfaceService.getOne(queryWrapper2);

        String fjk = fuLunInterface.getFjk();

        String url = "https://upace-api.ibiza.com.tw"+fjk;
        //String url = "https://upace-api.ibiza.com.tw"+fjk;

        log.info("url="+url);

        //restful接口url
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        String authorization = flConfig.getAuthorization();

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
            httpPost.addHeader("Authorization", authorization);

            /*jsonObject.remove("kczt");

            params = jsonObject.toJSONString();*/

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

    @Override
    public JSONObject doAction1(String url, String params) {
        JSONObject apiRes = new JSONObject();

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //restful接口url
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        String authorization = flConfig.getAuthorization();

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
