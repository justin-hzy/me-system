package com.me.modules.nascent.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.nascent.point.entity.PointLog;
import com.me.modules.nascent.point.mapper.PointLogMapper;
import com.me.modules.nascent.point.service.PointLogService;
import org.springframework.stereotype.Service;

@Service
public class PointLogServiceImpl extends ServiceImpl<PointLogMapper, PointLog> implements PointLogService {
}
