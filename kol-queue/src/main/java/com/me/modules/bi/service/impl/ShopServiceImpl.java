package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.Shop;
import com.me.modules.bi.mapper.ShopMapper;
import com.me.modules.bi.service.ShopService;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
}
