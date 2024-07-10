package com.me.modules.fl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.me.modules.fl.entity.FlCBErrorLog;
import com.me.modules.fl.mapper.FlCBErrorLogMapper;
import com.me.modules.fl.service.FlCBErrorLogService;
import org.springframework.stereotype.Service;

@Service
public class FlCBErrorLogServiceImpl extends ServiceImpl<FlCBErrorLogMapper, FlCBErrorLog> implements FlCBErrorLogService {
}
