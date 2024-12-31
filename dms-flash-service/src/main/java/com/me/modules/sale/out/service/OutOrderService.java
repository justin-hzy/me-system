package com.me.modules.sale.out.service;

import com.me.common.core.JsonResult;
import com.me.modules.sale.out.dto.PutOutOrderDto;

import java.io.IOException;

public interface OutOrderService {

    JsonResult putFlashOutOrder(PutOutOrderDto dto) throws IOException;
}
