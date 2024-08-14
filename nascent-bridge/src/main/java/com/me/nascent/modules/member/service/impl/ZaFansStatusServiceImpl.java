package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureFansStatus;
import com.me.nascent.modules.member.entity.ZaFansStatus;
import com.me.nascent.modules.member.mapper.PureFansStatusMapper;
import com.me.nascent.modules.member.mapper.ZaFansStatusMapper;
import com.me.nascent.modules.member.service.PureFansStatusService;
import com.me.nascent.modules.member.service.ZaFansStatusService;
import org.springframework.stereotype.Service;

@Service
public class ZaFansStatusServiceImpl extends ServiceImpl<ZaFansStatusMapper, ZaFansStatus> implements ZaFansStatusService {
}
