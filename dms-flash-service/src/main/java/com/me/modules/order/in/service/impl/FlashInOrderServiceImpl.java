package com.me.modules.order.in.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.in.entity.FlashInOrder;
import com.me.modules.order.in.mapper.FlashInOrderMapper;
import com.me.modules.order.in.service.FlashInOrderService;
import org.springframework.stereotype.Service;

@Service
public class FlashInOrderServiceImpl extends ServiceImpl<FlashInOrderMapper, FlashInOrder> implements FlashInOrderService {
}
