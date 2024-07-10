package com.me.modules.sys.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

public interface HttpService {

    String urlencode(Map<String, String> params);

    String sendPostRequest(String url, Map<String, String> header, Map<String,Object> body);

    String linkParams(Map<String, String> map,String secret);


}
