package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.ReissueOrder;
import com.me.modules.bi.mapper.ReissueOrderMapper;
import com.me.modules.bi.mapper.ReissueReasonMapper;
import com.me.modules.bi.service.ReissueOrderService;
import com.me.modules.bi.service.ReissueReasonService;
import org.springframework.stereotype.Service;

@Service
public class ReissueOrderServiceImpl extends ServiceImpl<ReissueOrderMapper, ReissueOrder> implements ReissueOrderService {
}
