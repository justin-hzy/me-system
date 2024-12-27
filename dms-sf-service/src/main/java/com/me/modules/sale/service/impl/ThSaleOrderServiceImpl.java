package com.me.modules.sale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.mapper.ThSaleOrderMapper;
import com.me.modules.sale.service.ThSaleOrderService;
import org.springframework.stereotype.Service;

@Service
public class ThSaleOrderServiceImpl extends ServiceImpl<ThSaleOrderMapper, ThSaleOrder> implements ThSaleOrderService {
}
