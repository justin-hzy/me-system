package com.me.nascent.modules.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.point.mapper.PureMemberPointMapper;
import com.me.nascent.modules.point.service.PureMemberPointService;
import org.springframework.stereotype.Service;

@Service
public class PureMemberPointServiceImpl extends ServiceImpl<PureMemberPointMapper,PureMemberPoint> implements PureMemberPointService {
}
