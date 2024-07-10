package com.me.order;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@Slf4j
public class BosTest {



    @Test
    public void signTest() throws IOException {

        String sip_appkey = "nea@burgeon.com.cn";

        String status = "0";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sip_timestamp = dateFormat.format(new Date())+".00";
        String pwd= "bos31";

        log.info(sip_timestamp);

        String url = "http://47.106.92.102:9991/bos/p/auth/v3/restapi/secretSign?sip_appkey="+sip_appkey+"&sip_timestamp="+sip_timestamp+"&strPwd="+pwd;


        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);


        response = httpClient.execute(httpGet);

        String resulString = EntityUtils.toString(response.getEntity());
        log.info("获取接口数据成功，接口返回体：" + resulString);
    }

    @Test
    public void otherInTest() throws IOException, NoSuchAlgorithmException {

        String sipAppKey = "nea@burgeon.com.cn";

        String appSecret = "bos31";

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sip_timestamp = dateFormat1.format(new Date())+".000";


        JSONObject jsonObject = new JSONObject();

        String id = UUID.randomUUID().toString();

        jsonObject.put("command","ProcessOrder");

        jsonObject.put("id",id);

        JSONObject params = new JSONObject();

        JSONObject detailobjs = new JSONObject();

        JSONArray reftables = new JSONArray();

        reftables.add("688");


        JSONArray refobjs = new JSONArray();


        JSONObject refobjsElem = new JSONObject();

        JSONArray addList = new JSONArray();


        JSONObject addListElem = new JSONObject();

        addListElem.put("QTY","1");
        addListElem.put("M_PRODUCT_ID__NAME","4711401210962-11111");

        addList.add(addListElem);

        refobjsElem.put("addList",addList);
        refobjsElem.put("table",12984);

        refobjs.add(refobjsElem);


        detailobjs.put("reftables",reftables);
        detailobjs.put("refobjs",refobjs);


        JSONObject masterobj = new JSONObject();

        masterobj.put("DESCRIPTION","测试");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String billDate = dateFormat.format(new Date());

        masterobj.put("BILLDATE",billDate);
        masterobj.put("C_OTHER_INOUTTYPE_ID__NAME","采购入库");

        masterobj.put("id","-1");
        masterobj.put("C_STORE_ID__NAME","良品仓");
        masterobj.put("table","12983");

        params.put("detailobjs",detailobjs);
        params.put("masterobj",masterobj);

        jsonObject.put("params",params);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);


        //String transactions = jsonObject.toJSONString();

        String url = "http://47.106.92.102:9991/servlets/binserv/Rest";

        String sip_sign = getSign(sipAppKey+sip_timestamp+DigestUtils.md5DigestAsHex(appSecret.getBytes()));

        // 准备需要提交的参数
        List<NameValuePair> paramsMap = new ArrayList<>();
        paramsMap.add(new BasicNameValuePair("sip_appkey", sipAppKey));
        paramsMap.add(new BasicNameValuePair("sip_timestamp", sip_timestamp));
        paramsMap.add(new BasicNameValuePair("sip_sign",sip_sign));
        paramsMap.add(new BasicNameValuePair("transactions",jsonArray.toJSONString()));

        log.info("sip_timestamp="+sip_timestamp);
        log.info("sip_sign="+sip_sign);
        log.info("jsonArray="+jsonArray.toJSONString());


        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // 将参数编码为URL编码的字符串
        String encodedParams = URLEncodedUtils.format(paramsMap, StandardCharsets.UTF_8);
        // 设置请求体
        httpPost.setEntity(new StringEntity(encodedParams, StandardCharsets.UTF_8));

        // 发送请求并获取响应
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        String responseBody = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
        System.out.println("Response: " + responseBody);
    }

    @Test
    public String getSign(String str)  {
        return DigestUtils.md5DigestAsHex(str.getBytes());
        /*String r="";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[]=md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            r=buf.toString();
        }catch(Exception e){
        }
        return r;*/
    }

    @Test
    public void test2() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = dateFormat.format(new Date());

        log.info("timeStr="+now);
    }
}
