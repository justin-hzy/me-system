package com.me.nascent.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.order.entity.Trade;
import com.me.nascent.modules.order.mapper.TradeMapper;
import com.me.nascent.modules.order.service.TradeService;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl extends ServiceImpl<TradeMapper, Trade> implements TradeService {
}
