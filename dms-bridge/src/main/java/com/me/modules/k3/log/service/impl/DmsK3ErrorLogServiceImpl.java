package com.me.modules.k3.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.k3.log.entity.DmsK3ErrorLog;
import com.me.modules.k3.log.mapper.DmsK3ErrorLogMapper;
import com.me.modules.k3.log.service.DmsK3ErrorLogService;
import org.springframework.stereotype.Service;

@Service
public class DmsK3ErrorLogServiceImpl extends ServiceImpl<DmsK3ErrorLogMapper,DmsK3ErrorLog> implements DmsK3ErrorLogService {
}
