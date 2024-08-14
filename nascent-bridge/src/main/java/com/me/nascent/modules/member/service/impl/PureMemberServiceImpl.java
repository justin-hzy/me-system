package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.mapper.PureMemberMapper;
import com.me.nascent.modules.member.service.PureMemberService;
import org.springframework.stereotype.Service;

@Service
public class PureMemberServiceImpl extends ServiceImpl<PureMemberMapper, PureMember> implements PureMemberService {
}
