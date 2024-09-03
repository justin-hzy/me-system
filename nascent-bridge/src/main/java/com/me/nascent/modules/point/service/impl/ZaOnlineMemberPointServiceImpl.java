package com.me.nascent.modules.point.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.point.entity.ZaOnlineMemberPoint;
import com.me.nascent.modules.point.mapper.ZaOnlineMemberPointMapper;
import com.me.nascent.modules.point.service.ZaOnlineMemberPointService;
import org.springframework.stereotype.Service;

@Service
public class ZaOnlineMemberPointServiceImpl extends ServiceImpl<ZaOnlineMemberPointMapper, ZaOnlineMemberPoint> implements ZaOnlineMemberPointService {
}
