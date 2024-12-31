package com.me.modules.sale.in.service;

import com.me.common.core.JsonResult;
import com.me.modules.sale.in.dto.PutInOrderDto;

import java.io.IOException;

public interface InOrderService {

    JsonResult putFlashInOrder(PutInOrderDto dto) throws IOException;
}
