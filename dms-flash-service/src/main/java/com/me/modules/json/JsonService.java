package com.me.modules.json;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.order.in.dto.PutInOrderDto;
import com.me.modules.order.in.entity.FlashInOrder;
import com.me.modules.order.out.dto.PutOutOrderDto;
import com.me.modules.refund.dto.PutRefundDto;

public interface JsonService {

    JSONObject createPutOutOrderJson(PutOutOrderDto dto);

    JSONObject createPutInOrderJson(PutInOrderDto dto);

    JSONObject createTransOutOrderDtlJson(String orderSn);

    JSONObject createTransInOrderDtlJson(FlashInOrder flashInOrder);

    JSONObject createPutRefundJson(PutRefundDto dto);
}
