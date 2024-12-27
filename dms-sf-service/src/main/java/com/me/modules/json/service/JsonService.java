package com.me.modules.json.service;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.entity.ThSaleOrder;

public interface JsonService {

    JSONObject createSaleOrderJson(PutSaleOrderDto dto);

    JSONObject createSaleOrderDtlJson(ThSaleOrder thSaleOrder);

    JSONObject createPurOrderJson(PutRefundPurDto dto);

    JSONObject createPurOrderDtlJson(ThRefund thRefund);
}
