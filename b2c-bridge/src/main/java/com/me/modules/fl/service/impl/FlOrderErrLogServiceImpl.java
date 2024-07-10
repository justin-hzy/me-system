package com.me.modules.fl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.fl.entity.FlOrderErrLog;
import com.me.modules.fl.mapper.FlOrderErrLogMapper;
import com.me.modules.fl.service.FlOrderErrLogService;
import org.springframework.stereotype.Service;

@Service
public class FlOrderErrLogServiceImpl extends ServiceImpl<FlOrderErrLogMapper, FlOrderErrLog> implements FlOrderErrLogService {
}
