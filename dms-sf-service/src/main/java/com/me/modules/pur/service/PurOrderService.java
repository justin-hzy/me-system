package com.me.modules.pur.service;

import com.me.common.core.JsonResult;
import com.me.modules.pur.dto.PurCancelDto;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.refund.entity.ThRefund;

import java.io.IOException;

public interface PurOrderService {

    JsonResult putPurOrder(PutRefundPurDto dto) throws IOException;

    JsonResult transPurOrderDtl(ThRefund thRefund) throws IOException;

    JsonResult purCancel(PurCancelDto dto) throws IOException;
}
