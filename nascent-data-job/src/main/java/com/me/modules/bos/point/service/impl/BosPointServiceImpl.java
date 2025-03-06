package com.me.modules.bos.point.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.BosConfig;
import com.me.common.core.JsonResult;
import com.me.common.utils.Md5Util;
import com.me.modules.bos.point.service.BosPointService;
import com.me.modules.nascent.point.entity.PointLog;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BosPointServiceImpl implements BosPointService {

    private BosConfig bosConfig;

    @Override
    public JsonResult putBosPoint(PointLog pointLog) {

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String billDate = dateFormat2.format(new Date());

        billDate = "2025-01-15";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(1541);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        JSONObject addListElem = new JSONObject();
        addListElem.put("MOBIL",pointLog.getNasOuid());
        addListElem.put("DESCRIPTION","日期:"+billDate+"积分源于初始化同步");
        BigDecimal point = pointLog.getPoint();
        if (0 == pointLog.getType() || 1 == pointLog.getType()){
            addListElem.put("INTEGRALADJ",point);
        }else if (2 == pointLog.getType() || 3 == pointLog.getType()){
            addListElem.put("INTEGRALADJ",point.negate());
        }
        addListElem.put("C_VIP_ID__CARDNO",pointLog.getNasOuid());

        addList.add(addListElem);

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",16427);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);

        JSONObject masterobj = new JSONObject();

        masterobj.put("ADJTYPE",1);

        billDate = billDate.replace("-","");

        masterobj.put("BILLDATE",billDate);
        masterobj.put("id","-1");
        masterobj.put("table","16426");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);

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

            Integer objectId = respJson.getInteger("objectid");

            JSONObject dataJson = new JSONObject();
            dataJson.put("objectId",objectId);

            if ("0".equals(code)){
                jsonResult.setCode("200");
                jsonResult.setMessage("新增成功");
                jsonResult.setData(dataJson);
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
        public JsonResult submitBosPoint(Integer objectId) {

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();
        String id = UUID.randomUUID().toString();
        jsonObject.put("command","ObjectSubmit");
        jsonObject.put("id",id);

        JSONObject params = new JSONObject();
        params.put("id",objectId);
        params.put("table","16426");

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


            JSONObject dataJson = new JSONObject();
            dataJson.put("objectId",objectId);

            if ("0".equals(code)){
                jsonResult.setCode("200");
                jsonResult.setMessage("新增成功");
                jsonResult.setData(dataJson);
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
