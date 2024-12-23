package com.me.modules.mabang.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.mabang.order.dto.InsertMBOrderDto;
import com.me.modules.mabang.order.entity.MBOrder;

public interface MBOrderService extends IService<MBOrder> {

    void insertMBOrder(InsertMBOrderDto dto);
}
