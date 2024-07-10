package com.me.modules.fl.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FlOrderService {

    void sendOrder(String param);

    List<String> queryOrder();
}
