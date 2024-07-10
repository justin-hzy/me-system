package com.me.modules.fl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.me.modules.fl.entity.FlOrderReqLog;
import com.me.modules.fl.mapper.FlOrderReqLogMapper;
import com.me.modules.fl.service.FlOrderReqLogService;
import org.springframework.stereotype.Service;

@Service
public class FlOrderReqLogServiceImpl extends ServiceImpl<FlOrderReqLogMapper, FlOrderReqLog> implements FlOrderReqLogService {
}
