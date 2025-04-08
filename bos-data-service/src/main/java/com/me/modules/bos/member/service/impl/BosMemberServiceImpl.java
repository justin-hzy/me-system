package com.me.modules.bos.member.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bos.member.entity.BosMember;
import com.me.modules.bos.member.mapper.BosMemberMapper;
import com.me.modules.bos.member.service.BosMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BosMemberServiceImpl extends ServiceImpl<BosMemberMapper, BosMember> implements BosMemberService {


}
