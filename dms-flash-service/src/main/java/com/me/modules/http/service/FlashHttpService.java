package com.me.modules.http.service;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;

public interface FlashHttpService {

    Map<String,String> createCommonParam(String storeCode);

    String generateSign(Map<String, String> paramMap, String secretKey,String jsonBody);

    String joinUrl (Map<String,String> paramMap,String sign,String url);

    JSONObject doAction(String url, JSONObject reqJson) throws IOException;
}
