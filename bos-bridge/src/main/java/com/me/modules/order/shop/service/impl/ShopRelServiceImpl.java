package com.me.modules.order.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.shop.entity.ShopRel;
import com.me.modules.order.shop.mapper.ShopRelMapper;
import com.me.modules.order.shop.service.ShopRelService;
import org.springframework.stereotype.Service;

@Service
public class ShopRelServiceImpl extends ServiceImpl<ShopRelMapper, ShopRel> implements ShopRelService {
}
