package com.me.modules.mabang.trans.service;


import com.alibaba.fastjson.JSONObject;

public interface MaBangTransService {

    JSONObject transMaBangOrder(String expressTimeStart,String expressTimeEnd,String cursor);

    JSONObject transMaBangRefund(String createDateStart,String createDateEnd,Integer page);
}
