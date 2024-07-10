package com.me.modules.sys.http;

import java.util.Map;

public interface HttpService {

    String urlencode(Map<String, String> params);


    String sendPostRequest(String url, Map<String, String> header, Map<String,String> body);

    String linkParams(Map<String, String> map,String secret);


}
