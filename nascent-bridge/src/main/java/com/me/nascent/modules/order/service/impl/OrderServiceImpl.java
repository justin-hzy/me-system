package com.me.nascent.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.order.mapper.OrderMapper;
import com.me.nascent.modules.order.service.OrderService;
import com.me.nascent.modules.order.entity.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
