package com.me.modules.order.out.service;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.order.out.dto.PutOutOrderDto;

import java.io.IOException;

public interface OutOrderService {

    JSONObject putFlashOutOrder(PutOutOrderDto dto) throws IOException;
}
