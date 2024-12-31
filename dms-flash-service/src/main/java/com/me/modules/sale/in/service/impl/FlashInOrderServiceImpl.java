package com.me.modules.sale.in.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.in.mapper.FlashInOrderMapper;
import com.me.modules.sale.in.service.FlashInOrderService;
import org.springframework.stereotype.Service;

@Service
public class FlashInOrderServiceImpl extends ServiceImpl<FlashInOrderMapper,FlashInOrder> implements FlashInOrderService {
}
