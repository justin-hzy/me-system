package com.me.modules.order.out.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.out.entity.FlashOutOrder;
import com.me.modules.order.out.mapper.FlashOrderMapper;
import com.me.modules.order.out.service.impl.FlashOutOrderService;
import org.springframework.stereotype.Service;

@Service
public class FlashOutOrderServiceImpl extends ServiceImpl<FlashOrderMapper, FlashOutOrder> implements FlashOutOrderService {
}
