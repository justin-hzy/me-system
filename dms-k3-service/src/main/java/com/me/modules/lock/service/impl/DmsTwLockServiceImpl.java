package com.me.modules.lock.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.lock.entity.DmsTwLock;
import com.me.modules.lock.mapper.DmsTwLockMapper;
import com.me.modules.lock.service.DmsTwLockService;
import org.springframework.stereotype.Service;

@Service
public class DmsTwLockServiceImpl extends ServiceImpl<DmsTwLockMapper, DmsTwLock> implements DmsTwLockService {
}
