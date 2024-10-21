package com.me.modules.mabang.trans.service;


import com.alibaba.fastjson.JSONObject;

public interface MaBangTransService {

    JSONObject transMaBangOrder(String createDateStart,String createDateEnd,String cursor);

    JSONObject transMaBangReOrder(String createDateStart,String createDateEnd,String cursor);
}
