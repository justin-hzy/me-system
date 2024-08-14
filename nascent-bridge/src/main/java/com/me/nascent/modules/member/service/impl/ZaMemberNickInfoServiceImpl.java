package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.entity.ZaMemberNickInfo;
import com.me.nascent.modules.member.mapper.PureMemberNickInfoMapper;
import com.me.nascent.modules.member.mapper.ZaMemberNickInfoMapper;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import com.me.nascent.modules.member.service.ZaMemberNickInfoService;
import org.springframework.stereotype.Service;

@Service
public class ZaMemberNickInfoServiceImpl extends ServiceImpl<ZaMemberNickInfoMapper, ZaMemberNickInfo> implements ZaMemberNickInfoService {
}
