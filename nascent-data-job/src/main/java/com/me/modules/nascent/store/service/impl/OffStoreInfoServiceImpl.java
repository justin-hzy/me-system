package com.me.modules.nascent.store.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.nascent.store.entity.OffStoreInfo;
import com.me.modules.nascent.store.mapper.OffStoreInfoMapper;
import com.me.modules.nascent.store.service.OffStoreInfoService;
import org.springframework.stereotype.Service;

@Service
public class OffStoreInfoServiceImpl extends ServiceImpl<OffStoreInfoMapper, OffStoreInfo> implements OffStoreInfoService {
}
