package com.me.modules.refund.service;

import com.me.common.core.JsonResult;
import com.me.modules.refund.dto.PutRefundDto;

import java.io.IOException;

public interface RefundService {

    JsonResult putFlashRefund(PutRefundDto dto) throws IOException;
}
