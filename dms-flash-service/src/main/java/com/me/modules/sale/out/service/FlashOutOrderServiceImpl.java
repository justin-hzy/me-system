package com.me.modules.sale.out.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.sale.out.entity.FlashOutOrder;
import com.me.modules.sale.out.mapper.FlashOrderMapper;
import com.me.modules.sale.out.service.impl.FlashOutOrderService;
import org.springframework.stereotype.Service;

@Service
public class FlashOutOrderServiceImpl extends ServiceImpl<FlashOrderMapper, FlashOutOrder> implements FlashOutOrderService {
}
