package com.me.modules.eccang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.eccang.entity.EcReOrder;
import com.me.modules.eccang.mapper.EcReOrderMapper;
import com.me.modules.eccang.service.EcReOrderService;
import org.springframework.stereotype.Service;

@Service
public class EcReOrderServiceImpl extends ServiceImpl<EcReOrderMapper, EcReOrder> implements EcReOrderService {
}
