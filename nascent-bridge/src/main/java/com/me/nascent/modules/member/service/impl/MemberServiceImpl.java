package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.Member;
import com.me.nascent.modules.member.mapper.MemberMapper;
import com.me.nascent.modules.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
}
