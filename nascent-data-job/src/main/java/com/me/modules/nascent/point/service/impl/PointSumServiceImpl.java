package com.me.modules.nascent.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.nascent.point.entity.PointSum;
import com.me.modules.nascent.point.mapper.PointSumMapper;
import com.me.modules.nascent.point.service.PointSumService;
import org.springframework.stereotype.Service;

@Service
public class PointSumServiceImpl extends ServiceImpl<PointSumMapper, PointSum> implements PointSumService {
}
