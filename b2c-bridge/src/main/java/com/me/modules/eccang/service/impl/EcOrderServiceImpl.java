package com.me.modules.eccang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.eccang.entity.EcOrder;
import com.me.modules.eccang.mapper.EcOrderMapper;
import com.me.modules.eccang.service.EcOrderService;
import org.springframework.stereotype.Service;

@Service
public class EcOrderServiceImpl extends ServiceImpl<EcOrderMapper, EcOrder> implements EcOrderService {
}
