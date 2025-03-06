package com.me.modules.bos.point.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.BosConfig;
import com.me.common.core.JsonResult;
import com.me.common.utils.Md5Util;
import com.me.modules.bos.point.service.BosMemberService;
import com.me.modules.bos.point.service.BosPointService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class BosMemberServiceImpl implements BosMemberService {

    private BosConfig bosConfig;

    @Override
    public JsonResult getBosMemberCount() {
        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","Query");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        params.put("start",0);
        params.put("count",true);
        params.put("range",1);
        params.put("table",12899);

        jsonObject.put("params",params);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        // 准备需要提交的参数
        List<NameValuePair> paramsMap = new ArrayList<>();
        paramsMap.add(new BasicNameValuePair("sip_appkey", sipAppKey));
        paramsMap.add(new BasicNameValuePair("sip_timestamp", sip_timestamp));
        paramsMap.add(new BasicNameValuePair("sip_sign", Md5Util.getSign(sipAppKey+sip_timestamp+ DigestUtils.md5DigestAsHex(appSecret.getBytes()))));
        paramsMap.add(new BasicNameValuePair("transactions",jsonArray.toJSONString()));

        log.info("transactions="+jsonArray.toJSONString());

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // 将参数编码为URL编码的字符串
        String encodedParams = URLEncodedUtils.format(paramsMap, StandardCharsets.UTF_8);
        // 设置请求体
        httpPost.setEntity(new StringEntity(encodedParams, StandardCharsets.UTF_8));

        // 发送请求并获取响应

        JsonResult jsonResult = new JsonResult();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject respJson = retJsonArr.getJSONObject(0);

            String code = respJson.getString("code");

            Integer count = respJson.getInteger("count");

            if ("0".equals(code)){
                jsonResult.setCode("200");
                jsonResult.setMessage("查询成功");
                jsonResult.setData(count);
            }else {
                jsonResult.setCode("500");
                jsonResult.setMessage("查询失败,请求失败");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            jsonResult.setCode("500");
            jsonResult.setMessage("查询失败,程序异常");
        }

        return jsonResult;
    }

    @Override
    public JsonResult getBosOffMembers(Integer count) {

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","Query");

        jsonObject.put("id",id);

        JSONObject params1 = new JSONObject();

        JSONArray columnsJsonArr = new JSONArray();

        columnsJsonArr.add("ID;CARDNO");

        params1.put("columns",columnsJsonArr);
        params1.put("start",0);
        params1.put("count","true");
        params1.put("range",count);

        JSONObject params2 = new JSONObject();

        JSONObject expr2Json = new JSONObject();

        params2.put("expr2",expr2Json);

        expr2Json.put("condition","Y");
        expr2Json.put("column","ID;C_STORE_ID;ISACTIVE");


        JSONObject expr1Json = new JSONObject();
        expr1Json.put("condition","POS");
        expr1Json.put("column","ID;C_STORE_ID;CODE");

        params2.put("expr1",expr1Json);
        params2.put("combine","and");

        params1.put("params",params2);
        params1.put("table",14845);

        jsonObject.put("params",params1);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        // 准备需要提交的参数
        List<NameValuePair> paramsMap = new ArrayList<>();
        paramsMap.add(new BasicNameValuePair("sip_appkey", sipAppKey));
        paramsMap.add(new BasicNameValuePair("sip_timestamp", sip_timestamp));
        paramsMap.add(new BasicNameValuePair("sip_sign", Md5Util.getSign(sipAppKey+sip_timestamp+ DigestUtils.md5DigestAsHex(appSecret.getBytes()))));
        paramsMap.add(new BasicNameValuePair("transactions",jsonArray.toJSONString()));

        log.info("transactions="+jsonArray.toJSONString());

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // 将参数编码为URL编码的字符串
        String encodedParams = URLEncodedUtils.format(paramsMap, StandardCharsets.UTF_8);
        // 设置请求体
        httpPost.setEntity(new StringEntity(encodedParams, StandardCharsets.UTF_8));

        // 发送请求并获取响应

        JsonResult jsonResult = new JsonResult();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            //log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject respJson = retJsonArr.getJSONObject(0);

            String code = respJson.getString("code");



            if ("0".equals(code)){
                jsonResult.setCode("200");
                jsonResult.setMessage("新增成功");

                JSONArray rowsJsonArr = respJson.getJSONArray("rows");

                log.info("rowsJsonArr.size="+rowsJsonArr.size());

                List<String> mobilList = new ArrayList<>();
                for (int i = 0;i<rowsJsonArr.size();i++){
                    JSONArray rowJsonArr = rowsJsonArr.getJSONArray(i);
                    String mobil = rowJsonArr.getString(0);
                    mobilList.add(mobil);
                }
                //log.info("mobilList="+mobilList.toString());
                log.info("size="+mobilList.size());
                jsonResult.setData(mobilList);
            }else {
                jsonResult.setCode("500");
                jsonResult.setMessage("新增失败,请求失败");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            jsonResult.setCode("500");
            jsonResult.setMessage("新增失败,程序异常");
        }
        return jsonResult;
    }

    @Override
    public JsonResult getBosOffPoint(Integer count) {
        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","Query");

        jsonObject.put("id",id);

        JSONObject params1 = new JSONObject();

        JSONArray columnsJsonArr = new JSONArray();

        columnsJsonArr.add("ID;CARDNO");
        columnsJsonArr.add("ID;INTEGRAL");

        params1.put("columns",columnsJsonArr);
        params1.put("start",0);
        params1.put("count","true");
        params1.put("range",count);

        JSONObject params2 = new JSONObject();

        JSONObject expr2Json = new JSONObject();

        params2.put("expr2",expr2Json);

        expr2Json.put("condition","Y");
        expr2Json.put("column","ID;C_STORE_ID;ISACTIVE");


        JSONObject expr1Json = new JSONObject();
        expr1Json.put("condition","POS");
        expr1Json.put("column","ID;C_STORE_ID;CODE");

        params2.put("expr1",expr1Json);
        params2.put("combine","and");

        params1.put("params",params2);
        params1.put("table",14845);

        jsonObject.put("params",params1);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        // 准备需要提交的参数
        List<NameValuePair> paramsMap = new ArrayList<>();
        paramsMap.add(new BasicNameValuePair("sip_appkey", sipAppKey));
        paramsMap.add(new BasicNameValuePair("sip_timestamp", sip_timestamp));
        paramsMap.add(new BasicNameValuePair("sip_sign", Md5Util.getSign(sipAppKey+sip_timestamp+ DigestUtils.md5DigestAsHex(appSecret.getBytes()))));
        paramsMap.add(new BasicNameValuePair("transactions",jsonArray.toJSONString()));

        log.info("transactions="+jsonArray.toJSONString());

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // 将参数编码为URL编码的字符串
        String encodedParams = URLEncodedUtils.format(paramsMap, StandardCharsets.UTF_8);
        // 设置请求体
        httpPost.setEntity(new StringEntity(encodedParams, StandardCharsets.UTF_8));

        // 发送请求并获取响应

        JsonResult jsonResult = new JsonResult();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            //log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject respJson = retJsonArr.getJSONObject(0);

            String code = respJson.getString("code");



            if ("0".equals(code)){
                jsonResult.setCode("200");
                jsonResult.setMessage("新增成功");

                JSONArray rowsJsonArr = respJson.getJSONArray("rows");

                log.info("rowsJsonArr.size="+rowsJsonArr.size());

                List<Map<String,String>> pointList = new ArrayList<>();
                for (int i = 0;i<rowsJsonArr.size();i++){
                    Map<String,String> pointMap = new HashMap<>();
                    JSONArray rowJsonArr = rowsJsonArr.getJSONArray(i);
                    String cardNo = rowJsonArr.getString(0);
                    pointMap.put("cardNo",cardNo);
                    String point = rowJsonArr.getString(1);
                    pointMap.put("point",point);
                    pointList.add(pointMap);
                }
                //log.info("mobilList="+mobilList.toString());
                log.info("size="+pointList.size());
                jsonResult.setData(pointList);
            }else {
                jsonResult.setCode("500");
                jsonResult.setMessage("新增失败,请求失败");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            jsonResult.setCode("500");
            jsonResult.setMessage("新增失败,程序异常");
        }
        return jsonResult;
    }
}
