package com.me.modules.order.profit.service;

import com.me.modules.order.profit.dto.QryProfitReqDto;
import com.me.modules.order.profit.dto.QryProfitRespDto;

public interface OrderProfitService {

    QryProfitRespDto queryProfit(QryProfitReqDto reqDto);
}
