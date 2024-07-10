package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.AllergyReactionOrder;
import com.me.modules.bi.mapper.AllergyReactionOrderMapper;
import com.me.modules.bi.service.AllergyReactionOrderService;
import org.springframework.stereotype.Service;

@Service
public class AllergyReactionOrderServiceImpl extends ServiceImpl<AllergyReactionOrderMapper, AllergyReactionOrder> implements AllergyReactionOrderService {
}
