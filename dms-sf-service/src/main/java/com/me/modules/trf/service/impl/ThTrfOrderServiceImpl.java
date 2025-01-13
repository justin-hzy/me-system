package com.me.modules.trf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.trf.entity.ThTrfOrder;
import com.me.modules.trf.mapper.ThTrfOrderMapper;
import com.me.modules.trf.service.ThTrfOrderService;
import org.springframework.stereotype.Service;

@Service
public class ThTrfOrderServiceImpl extends ServiceImpl<ThTrfOrderMapper, ThTrfOrder> implements ThTrfOrderService {
}
