package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.mapper.PureMemberNickInfoMapper;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import org.springframework.stereotype.Service;

@Service
public class PureMemberNickInfoServiceImpl extends ServiceImpl<PureMemberNickInfoMapper, PureMemberNickInfo> implements PureMemberNickInfoService {
}
