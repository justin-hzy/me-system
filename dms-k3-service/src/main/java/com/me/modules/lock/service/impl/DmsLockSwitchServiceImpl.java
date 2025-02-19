package com.me.modules.lock.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.lock.entity.DmsLockSwitch;
import com.me.modules.lock.mapper.DmsLockSwitchMapper;
import com.me.modules.lock.service.DmsLockSwitchService;
import org.springframework.stereotype.Service;

@Service
public class DmsLockSwitchServiceImpl extends ServiceImpl<DmsLockSwitchMapper, DmsLockSwitch> implements DmsLockSwitchService {
}
