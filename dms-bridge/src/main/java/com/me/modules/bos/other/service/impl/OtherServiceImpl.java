package com.me.modules.bos.other.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.BosConfig;
import com.me.common.utils.Md5Util;
import com.me.modules.bos.other.dto.PostOtherInDto;
import com.me.modules.bos.other.dto.PostOtherOutDto;
import com.me.modules.bos.other.pojo.SubOther;
import com.me.modules.bos.other.service.OtherService;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OtherServiceImpl implements OtherService {

    private BosConfig bosConfig;

    @Override
    public JSONObject PostOtherIn(PostOtherInDto reqDto){

        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();

        List<SubOther> subOthers = reqDto.getSubOthers();

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();
        log.info("url");


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();

        for (SubOther subOther : subOthers){
            JSONObject addListElem = new JSONObject();
            addListElem.put("QTY",subOther.getQty());
            addListElem.put("M_PRODUCT_ID__NAME",subOther.getSku());
            addList.add(addListElem);
        }

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","采购入库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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

        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);

            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败,请求失败");
            }
        }catch (Exception e){
            log.info(e.getMessage());
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败,程序异常！");
        }

        return jsonObject1;
    }

    @Override
    public JSONObject postReOther(PostOtherOutDto reqDto){

        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();

        //负数处理
        /*qty = "-"+qty;*/

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        JSONObject addListElem = new JSONObject();

        addListElem.put("QTY",qty);
        addListElem.put("M_PRODUCT_ID__NAME",sku);

        addList.add(addListElem);

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","采购退货出库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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

        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败,请求失败！");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败,程序异常");
        }
        return jsonObject1;
    }

    @Override
    public JSONObject postSetFr(PostOtherInDto reqDto){


        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();

        for (SubOther subOther : reqDto.getSubOthers()){
            JSONObject addListElem = new JSONObject();

            addListElem.put("QTY",subOther.getQty());
            addListElem.put("M_PRODUCT_ID__NAME",subOther.getSku());

            addList.add(addListElem);
        }

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-组套单入库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }

        return jsonObject1;
    }

    @Override
    public JSONObject postSetSon(  PostOtherOutDto reqDto){

        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();

        //负数处理
        /*qty = "-"+qty;*/

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();

        List<SubOther> subOthers = reqDto.getSubOthers();
        for (SubOther subOther : subOthers){

            JSONObject addListElem = new JSONObject();

            addListElem.put("QTY",subOther.getQty());
            addListElem.put("M_PRODUCT_ID__NAME",subOther.getSku());

            addList.add(addListElem);
        }



        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-组套单出库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }


        return jsonObject1;

    }

    @Override
    public JSONObject postDismantleSon(PostOtherInDto reqDto){

        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();
        List<SubOther> subOthers = reqDto.getSubOthers();

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        for (SubOther subOther : subOthers){
            JSONObject addListElem = new JSONObject();

            addListElem.put("QTY",subOther.getQty());
            addListElem.put("M_PRODUCT_ID__NAME",subOther.getSku());
            addList.add(addListElem);
        }


        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-拆卸入库单");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }
        return jsonObject1;
    }

    @Override
    public JSONObject postDismantleFr(PostOtherOutDto reqDto) {
        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();


        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();

        List<SubOther> subOthers = reqDto.getSubOthers();

        for (SubOther subOther : subOthers){
            JSONObject addListElem = new JSONObject();

            addListElem.put("QTY",subOther.getQty());
            addListElem.put("M_PRODUCT_ID__NAME",subOther.getSku());

            addList.add(addListElem);
        }


        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-拆卸出库单");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }

        return jsonObject1;
    }

    @Override
    public JSONObject postTransCodeFr(PostOtherOutDto reqDto) {
        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        JSONObject addListElem = new JSONObject();

        addListElem.put("QTY",qty);
        addListElem.put("M_PRODUCT_ID__NAME",sku);

        addList.add(addListElem);

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-转码订单出库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }
        return jsonObject1;
    }


    @Override
    public JSONObject postTransCodeSon(PostOtherInDto reqDto){
        String sku = reqDto.getSku();
        String description = reqDto.getDescription();
        String billDate = reqDto.getBillDate();
        String cStoreName = reqDto.getCstore();
        String qty = reqDto.getQty();

        String appSecret = bosConfig.getAppSecret();
        String sipAppKey = bosConfig.getSipAppKey();
        String url = bosConfig.getUrl();


        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";

        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add(688);

        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        JSONObject addListElem = new JSONObject();

        addListElem.put("QTY",qty);
        addListElem.put("M_PRODUCT_ID__NAME",sku);

        addList.add(addListElem);

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION",description);

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","台湾-转码订单入库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME",cStoreName);
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);
        params.put("submit",true);

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
        JSONObject jsonObject1 = new JSONObject();

        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
            log.info("Response: " + responseBody);

            JSONArray retJsonArr = JSONArray.parseArray(responseBody);

            JSONObject RetJson = retJsonArr.getJSONObject(0);

            String code = RetJson.getString("code");

            if ("0".equals(code)){
                jsonObject1.put("code","200");
                jsonObject1.put("messsage","新增成功");
            }else {
                jsonObject1.put("code","500");
                jsonObject1.put("messsage","新增失败，请求失败");
            }
        }catch (Exception e){
            jsonObject1.put("code","500");
            jsonObject1.put("messsage","新增失败，程序异常");
        }


        return jsonObject1;
    }

    @Override
    public void updateOtherLog() {

    }

}
