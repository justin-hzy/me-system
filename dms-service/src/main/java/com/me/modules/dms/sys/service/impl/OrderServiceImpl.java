package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.dms.sys.entity.Order;
import com.me.modules.dms.sys.mapper.OrderMapper;
import com.me.modules.dms.sys.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
