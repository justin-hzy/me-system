package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.MemberTong;
import com.me.nascent.modules.member.mapper.MemberTongMapper;
import com.me.nascent.modules.member.service.MemberTongService;
import org.springframework.stereotype.Service;

@Service
public class MemberTongServiceImpl extends ServiceImpl<MemberTongMapper, MemberTong> implements MemberTongService {
}
