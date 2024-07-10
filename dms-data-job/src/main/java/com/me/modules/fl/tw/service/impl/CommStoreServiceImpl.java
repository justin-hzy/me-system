package com.me.modules.fl.tw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.fl.tw.entity.CommStore;
import com.me.modules.fl.tw.mapper.CommStoreMapper;
import com.me.modules.fl.tw.service.CommStoreService;
import org.springframework.stereotype.Service;

@Service
public class CommStoreServiceImpl extends ServiceImpl<CommStoreMapper, CommStore> implements CommStoreService {
}
