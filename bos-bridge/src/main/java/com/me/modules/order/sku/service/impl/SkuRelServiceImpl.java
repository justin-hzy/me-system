package com.me.modules.order.sku.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.sku.entity.SkuRel;
import com.me.modules.order.sku.mapper.SkuRelMapper;
import com.me.modules.order.sku.service.SkuRelService;
import org.springframework.stereotype.Service;

@Service
public class SkuRelServiceImpl extends ServiceImpl<SkuRelMapper, SkuRel> implements SkuRelService {
}
