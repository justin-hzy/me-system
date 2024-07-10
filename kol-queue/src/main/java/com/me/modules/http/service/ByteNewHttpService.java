package com.me.modules.http.service;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.Map;

public interface ByteNewHttpService {
     String generate_sign(Map<String, String> params, String app_secret);

    String urlencode(Map<String, String> params);

    String send_get_request(String url);

    String transUrl();

    String send_post_request(String url,Map<String, String> params);

    String send_post_request_1(String url, List<NameValuePair> params);


}
