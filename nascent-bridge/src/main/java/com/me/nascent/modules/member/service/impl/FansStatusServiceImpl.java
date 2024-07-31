package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.FansStatus;
import com.me.nascent.modules.member.mapper.FansStatusMapper;
import com.me.nascent.modules.member.service.FansStatusService;
import org.springframework.stereotype.Service;

@Service
public class FansStatusServiceImpl extends ServiceImpl<FansStatusMapper, FansStatus> implements FansStatusService {
}
