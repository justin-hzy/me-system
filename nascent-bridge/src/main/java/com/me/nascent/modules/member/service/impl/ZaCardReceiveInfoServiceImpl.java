package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureCardReceiveInfo;
import com.me.nascent.modules.member.entity.ZaCardReceiveInfo;
import com.me.nascent.modules.member.mapper.PureCardReceiveInfoMapper;
import com.me.nascent.modules.member.mapper.ZaCardReceiveInfoMapper;
import com.me.nascent.modules.member.service.PureCardReceiveInfoService;
import com.me.nascent.modules.member.service.ZaCardReceiveInfoService;
import org.springframework.stereotype.Service;

@Service
public class ZaCardReceiveInfoServiceImpl extends ServiceImpl<ZaCardReceiveInfoMapper, ZaCardReceiveInfo> implements ZaCardReceiveInfoService {
}
