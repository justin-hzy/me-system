package com.me.modules.refund.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.refund.mapper.ThRefundMapper;
import com.me.modules.refund.service.ThRefundService;
import org.springframework.stereotype.Service;

@Service
public class ThRefundServiceImpl extends ServiceImpl<ThRefundMapper, ThRefund> implements ThRefundService {
}
