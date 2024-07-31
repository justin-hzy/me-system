package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.CardReceiveInfo;
import com.me.nascent.modules.member.mapper.CardReceiveInfoMapper;
import com.me.nascent.modules.member.service.CardReceiveInfoService;
import org.springframework.stereotype.Service;

@Service
public class CardReceiveInfoServiceImpl extends ServiceImpl<CardReceiveInfoMapper, CardReceiveInfo> implements CardReceiveInfoService {
}
