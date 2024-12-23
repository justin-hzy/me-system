package com.me.modules.mabang.refund.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.mabang.order.dto.InsertMBOrderDto;
import com.me.modules.mabang.refund.dto.InsertMBRefundDto;
import com.me.modules.mabang.refund.entity.MBRefund;

public interface MBRefundService extends IService<MBRefund> {

    void insertMBRefund(InsertMBRefundDto dto);
}
