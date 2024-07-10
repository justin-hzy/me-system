package com.me.modules.fl.service;

import com.alibaba.fastjson.JSONObject;

public interface FuLunHttpService {

    public JSONObject doAction(String apiId, String params);

    public JSONObject doAction1(String url,String params);
}
