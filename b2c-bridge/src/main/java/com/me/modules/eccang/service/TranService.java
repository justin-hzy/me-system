package com.me.modules.eccang.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Objects;

public interface TranService {

    JSONObject tranEcReOrder(Integer page,Integer pageSize);
}
