package com.me.modules.trf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.trf.entity.TrfInOrder;
import com.me.modules.trf.mapper.TrfInOrderMapper;
import com.me.modules.trf.service.TrfInOrderService;
import org.springframework.stereotype.Service;

@Service
public class TrfInOrderServiceImpl extends ServiceImpl<TrfInOrderMapper,TrfInOrder> implements TrfInOrderService {
}
