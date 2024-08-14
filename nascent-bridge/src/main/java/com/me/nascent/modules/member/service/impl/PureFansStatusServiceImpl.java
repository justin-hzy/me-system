package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.PureFansStatus;
import com.me.nascent.modules.member.mapper.PureFansStatusMapper;
import com.me.nascent.modules.member.service.PureFansStatusService;
import org.springframework.stereotype.Service;

@Service
public class PureFansStatusServiceImpl extends ServiceImpl<PureFansStatusMapper, PureFansStatus> implements PureFansStatusService {
}
