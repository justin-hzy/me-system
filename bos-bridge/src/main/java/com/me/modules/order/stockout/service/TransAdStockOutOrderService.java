package com.me.modules.order.stockout.service;

import java.util.List;
import java.util.Map;

public interface TransAdStockOutOrderService {



    void tranAbroadDetailOrder(List<String> tradeIdList);

    Map<String,Object> getStockOutOrderDetails(Integer currentPageNo);


    void tranMRetail();
}
