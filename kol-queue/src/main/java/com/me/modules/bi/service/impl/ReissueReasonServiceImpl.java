package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.ReissueReason;
import com.me.modules.bi.mapper.ReissueReasonMapper;
import com.me.modules.bi.service.ReissueReasonService;
import org.springframework.stereotype.Service;

@Service
public class ReissueReasonServiceImpl extends ServiceImpl<ReissueReasonMapper, ReissueReason> implements ReissueReasonService {
}
