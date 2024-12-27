package com.me.modules.http.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.List;

public interface SlHttpService {

    JSONObject doAction(String url, List<NameValuePair> params) throws IOException;
}
