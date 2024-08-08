package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.MemberNickInfo;
import com.me.nascent.modules.member.mapper.MemberNickInfoMapper;
import com.me.nascent.modules.member.service.MemberNickInfoService;
import org.springframework.stereotype.Service;

@Service
public class MemberNickInfoServiceImpl extends ServiceImpl<MemberNickInfoMapper, MemberNickInfo> implements MemberNickInfoService {
}
