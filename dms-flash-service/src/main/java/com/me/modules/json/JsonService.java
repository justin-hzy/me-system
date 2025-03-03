package com.me.modules.json;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.sale.in.dto.PutInOrderDto;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.out.dto.PutOutOrderDto;
import com.me.modules.refund.dto.PutRefundDto;

public interface JsonService {

    JSONObject createPutOutOrderJson(PutOutOrderDto dto);

    JSONObject createPutInOrderJson(PutInOrderDto dto);

    JSONObject createTransOutOrderDtlJson(String orderSn);

    JSONObject createTransInOrderDtlJson(FlashInOrder flashInOrder);

    JSONObject createPutRefundJson(PutRefundDto dto);
}
