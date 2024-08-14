package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.entity.ZaMember;
import com.me.nascent.modules.member.mapper.PureMemberMapper;
import com.me.nascent.modules.member.mapper.ZaMemberMapper;
import com.me.nascent.modules.member.service.PureMemberService;
import com.me.nascent.modules.member.service.ZaMemberService;
import org.springframework.stereotype.Service;

@Service
public class ZaMemberServiceImpl extends ServiceImpl<ZaMemberMapper, ZaMember> implements ZaMemberService {
}
