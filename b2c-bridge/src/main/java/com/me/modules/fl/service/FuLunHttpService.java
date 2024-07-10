package com.me.modules.fl.service;

import com.alibaba.fastjson.JSONObject;

public interface FuLunHttpService {

    public JSONObject doGetAction(String url);

    public JSONObject doPostAction(String params,String url);
}
