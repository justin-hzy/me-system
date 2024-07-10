package com.me.modules.dms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.me.modules.dms.entity.CommStore;
import com.me.modules.dms.mapper.CommStoreMapper;
import com.me.modules.dms.service.CommStoreService;
import org.springframework.stereotype.Service;

@Service
public class CommStoreServiceImpl extends ServiceImpl<CommStoreMapper, CommStore> implements CommStoreService {
}
