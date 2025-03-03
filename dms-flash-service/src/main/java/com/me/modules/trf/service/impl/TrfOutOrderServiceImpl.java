package com.me.modules.trf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.trf.entity.TrfOutOrder;
import com.me.modules.trf.mapper.TrfOutOrderMapper;
import com.me.modules.trf.service.TrfOutOrderService;
import org.springframework.stereotype.Service;

@Service
public class TrfOutOrderServiceImpl extends ServiceImpl<TrfOutOrderMapper,TrfOutOrder> implements TrfOutOrderService {
}
