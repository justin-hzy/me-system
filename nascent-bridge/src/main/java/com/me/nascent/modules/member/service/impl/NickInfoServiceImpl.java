package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.NickInfo;
import com.me.nascent.modules.member.mapper.NickInfoMapper;
import com.me.nascent.modules.member.service.NickInfoService;
import org.springframework.stereotype.Service;

@Service
public class NickInfoServiceImpl extends ServiceImpl<NickInfoMapper, NickInfo> implements NickInfoService {
}
