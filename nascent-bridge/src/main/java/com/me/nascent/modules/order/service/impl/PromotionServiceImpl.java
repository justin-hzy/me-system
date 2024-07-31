package com.me.nascent.modules.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.order.entity.Promotion;
import com.me.nascent.modules.order.mapper.PromotionMapper;
import com.me.nascent.modules.order.service.PromotionService;
import org.springframework.stereotype.Service;

@Service
public class PromotionServiceImpl extends ServiceImpl<PromotionMapper, Promotion> implements PromotionService {
}
