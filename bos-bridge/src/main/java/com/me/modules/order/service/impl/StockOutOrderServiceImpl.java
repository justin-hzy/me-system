package com.me.modules.order.service.impl;

import com.me.modules.order.service.StockOutOrderService;
import com.me.modules.sys.service.HttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class StockOutOrderServiceImpl implements StockOutOrderService {

    private HttpService httpService;

    @Override
    public List queryStockOutOrder() {
        String baseUrl = "https://api.wangdian.cn/openapi2/stockout_order_query_trade.php";

        return null;
    }
}
