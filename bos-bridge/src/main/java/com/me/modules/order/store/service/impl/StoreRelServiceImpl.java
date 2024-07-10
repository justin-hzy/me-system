package com.me.modules.order.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.mapper.StoreRelMapper;
import com.me.modules.order.store.service.StoreRelService;
import org.springframework.stereotype.Service;

@Service
public class StoreRelServiceImpl extends ServiceImpl<StoreRelMapper, StoreRel> implements StoreRelService {
}
