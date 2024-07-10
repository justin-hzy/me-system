package com.me.modules.bi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.bi.dto.UserReqDto;
import com.me.modules.bi.dto.UserRespDto;
import com.me.modules.bi.service.ByteNewService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ByteNewServiceImpl implements ByteNewService {
    @Override
    public String queryUser(UserReqDto userReqDto) {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", "3331225291");
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method","user.list");
        params.put("access_token", "f0507b2d2d2d3d91f741c4d4277344ba");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", generate_sign(params, "71dc01248f11697674f3e610971c3013"));
        String base_url = "https://open.bytenew.com/gateway/api/user";
        String full_url = base_url + "?" + urlencode(params);
        full_url = full_url.replace(" ","%20");
        //System.out.println("full_url="+full_url);
        // 设置请求体参数
        Map<String,String> body = new HashMap<>();
        body.put("pageSize","1000");
        body.put("pageNum","1");
        String responseBody = send_post_request(full_url,params);
        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONObject("map").getJSONArray("result");
        List<UserRespDto> userRespDtoList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            UserRespDto userRespDto = new UserRespDto();
            for(String key : dataElement.keySet()){
                if("nick".equals(key)){
                    userRespDto.setNick(dataElement.get(key).toString());
                }
                if("id".equals(key)){
                    userRespDto.setNick(dataElement.get(key).toString());
                }
            }
            userRespDtoList.add(userRespDto);
        }
        //筛选出用户
        String nick = "";
        for(UserRespDto userRespDto : userRespDtoList){
            if(userReqDto.getId().equals(userRespDto.getId())){
                nick = userRespDto.getNick();
            }
        }
        return nick;
    }

    public static String generate_sign(Map<String, String> params, String app_secret) {
        // 排除 sign 参数
        params.remove("sign");

        // 按照字母先后顺序对参数进行排序
        List<Map.Entry<String, String>> sorted_params = new ArrayList<>(params.entrySet());
        Collections.sort(sorted_params, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        // 将排序后的参数拼接为字符串
        StringBuilder sign_str = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted_params) {
            sign_str.append(entry.getKey()).append(entry.getValue());
        }

        // 将 app_secret 添加到字符串头尾
        sign_str.insert(0, app_secret);
        sign_str.append(app_secret);
        //System.out.println("sign_str="+sign_str);

        // 进行 MD5 加密
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(sign_str.toString().getBytes("UTF-8"));
            StringBuilder sign = new StringBuilder();
            for (byte b : digest) {
                sign.append(String.format("%02X", b));
            }
            return sign.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String urlencode(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return encodedParams.toString();
    }

    public static String send_get_request(String url) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent","Apifox/1.0.0 (https://apifox.com)");
            httpGet.setHeader("Content-type","Application/json");
            HttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String send_post_request(String url,Map<String, String> params) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            // 使用你喜欢的 HTTP 请求库发送 GET 请求，并返回响应结果
            // 这里只是一个示例
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type","Application/json");
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
