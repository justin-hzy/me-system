package com.me.modules.http.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.FlashConfig;
import com.me.modules.http.entity.FlashRespCode;
import com.me.modules.http.service.FlashHttpService;
import com.me.modules.http.service.FlashRespCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class FlashHttpServiceImpl implements FlashHttpService {

    private FlashConfig flashConfig;

    private FlashRespCodeService flashRespCodeService;

    private static final String SHA256_ALGORITHM_NAME = "SHA-256";
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Map<String,String> createCommonParam(){
        Map<String,String> commonParam = new HashMap<>();
        commonParam.put("mchId",flashConfig.getMerchantId());
//        String nonceStr = UUID.randomUUID().toString().replace("-","");

        Long currentTime = System.currentTimeMillis();
        commonParam.put("timestamp",String.valueOf(currentTime));

        String nonceStr = encryptMD5(String.valueOf(currentTime));
        commonParam.put("nonceStr",nonceStr);
        return commonParam;
    }

    public String generateSign(Map<String, String> paramMap, String secretKey,String jsonBody) {
        Map<String, String> acsMap = sortMapByKey(paramMap);
        StringBuilder sb = new StringBuilder();
        sb.append(secretKey);
        for (Map.Entry<String, String> entry : acsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StrUtil.isNotEmpty(value)) {
                sb.append(key).append(value);
            }
        }
        sb.append(jsonBody).append(secretKey);
        log.info("sb="+sb);
        return sha256HexDigest(sb.toString()).toUpperCase();
    }



    public String joinUrl (Map<String,String> paramMap,String sign,String url){
        Map<String, String> acsMap = sortMapByKey(paramMap);
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        for (Map.Entry<String, String> entry : acsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StrUtil.isNotEmpty(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        url = String.valueOf(sb);

        url = url+"sign="+sign;

        return url;
    }

    public JSONObject doAction(String url, JSONObject reqJson) throws IOException {
        JSONObject apiRes = new JSONObject();

        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //restful接口url
        HttpPost httpPost = new HttpPost(url);
        //设置请求头
        httpPost.addHeader("Content-Type","application/json;charset=utf-8");
        httpPost.setEntity(new StringEntity(reqJson.toJSONString(), "UTF-8"));
        log.info("putStockParam="+reqJson.toJSONString());
        response = httpClient.execute(httpPost);

        if (response != null && response.getEntity() != null) {
            //返回信息
            String resulString = EntityUtils.toString(response.getEntity());

            //处理返回信息
            apiRes = JSON.parseObject(resulString);

            String code = apiRes.getString("code");

            QueryWrapper<FlashRespCode> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("code",code);
            FlashRespCode flashRespCode = flashRespCodeService.getOne(queryWrapper);
            log.info("获取接口数据成功，接口返回体：" + "code:"+code+",msg:"+flashRespCode.getMeaning());
        }else{
            apiRes.put("result","false");
            apiRes.put("message","接口错误返回错误！");
            log.info("获取接口数据失败，接口返回体为空！");
        }



        return apiRes;
    }

    private static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    public String sha256HexDigest(String source) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA256_ALGORITHM_NAME);
            messageDigest.update(source.getBytes("UTF-8"));
            return new String(encodeHex(messageDigest.digest()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public  String encryptMD5(String input) {
        try {
            // 创建MD5加密对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 执行加密操作
            byte[] messageDigest = md.digest(input.getBytes());
            // 将字节数组转换为16进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            // 返回加密后的字符串
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private char[] encodeHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
