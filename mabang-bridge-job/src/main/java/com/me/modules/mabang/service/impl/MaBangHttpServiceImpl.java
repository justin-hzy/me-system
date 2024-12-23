package com.me.modules.mabang.service.impl;

import com.alibaba.fastjson.JSON;
import com.me.common.config.MaBangConfig;
import com.me.modules.mabang.service.MaBangHttpService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MaBangHttpServiceImpl implements MaBangHttpService {

    private MaBangConfig maBangConfig;

    @Override
    public String callGwApi(String api, Map<String, Object> reqParams) {

        String reqUrl = maBangConfig.getReqUrl();
        String appKey = maBangConfig.getAppKey();
        String appToken = maBangConfig.getAppToken();


        // 创建httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(reqUrl);

        // 封装传参数据
        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("accesstoken", "");
        datas.put("api", api);
        datas.put("appkey", appKey);
        datas.put("data", reqParams);
        datas.put("timestamp", new Long(System.currentTimeMillis() / 1000).toString());

        // 将传参转为Json格式
        String jsonContent = JSON.toJSONString(datas);
//      System.out.println(jsonContent);

        // 封装请求头
        String authorization = hmacSha256(jsonContent, appToken);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        httpPost.setHeader("Authorization", authorization);

        //System.out.println(jsonContent);

        // 封装提交数据
        StringEntity se = new StringEntity(jsonContent, "UTF-8");
        httpPost.setEntity(se);

        //打印curl



        String body = "";
        CloseableHttpResponse response;
        try {
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                body = EntityUtils.toString(entity, "UTF-8");
            }
            EntityUtils.consume(entity);
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }


    /**
     * hmac256加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] hmacSha256(String data, byte[] key) {
        String algorithm = "HmacSHA256";
        Mac sha256_HMAC;
        byte[] array = null;
        try {
            sha256_HMAC = Mac.getInstance(algorithm);
            sha256_HMAC.init(new SecretKeySpec(key, algorithm));
            array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return array;
    }

    /**
     * hmac256加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String hmacSha256(String data, String key) {
        byte[] array = null;
        try {
            array = hmacSha256(data, key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toLowerCase();
    }
}
