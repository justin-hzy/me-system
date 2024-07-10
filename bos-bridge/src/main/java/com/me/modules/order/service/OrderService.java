package com.me.modules.order.service;

import java.util.Map;

public interface OrderService {

    Map<String,Object> getAbdOrder(Integer pageNo,Integer pageSize,Integer tradeStatusCode);

    void getOrder();
}
