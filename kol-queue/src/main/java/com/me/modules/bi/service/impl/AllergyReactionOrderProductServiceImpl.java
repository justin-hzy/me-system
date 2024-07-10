package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.AllergyReactionOrderProduct;
import com.me.modules.bi.mapper.AllergyReactionOrderProductMapper;
import com.me.modules.bi.service.AllergyReactionOrderProductService;
import org.springframework.stereotype.Service;

@Service
public class AllergyReactionOrderProductServiceImpl extends ServiceImpl<AllergyReactionOrderProductMapper, AllergyReactionOrderProduct> implements AllergyReactionOrderProductService {
}
