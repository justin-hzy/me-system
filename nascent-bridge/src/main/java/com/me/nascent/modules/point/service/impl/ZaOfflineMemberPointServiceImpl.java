package com.me.nascent.modules.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.point.entity.ZaOfflineMemberPoint;
import com.me.nascent.modules.point.mapper.ZaOfflineMemberPointMapper;
import com.me.nascent.modules.point.service.ZaOfflineMemberPointService;
import org.springframework.stereotype.Service;

@Service
public class ZaOfflineMemberPointServiceImpl extends ServiceImpl<ZaOfflineMemberPointMapper,ZaOfflineMemberPoint> implements ZaOfflineMemberPointService {
}
