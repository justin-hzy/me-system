package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.ReissueOrderProduct;
import com.me.modules.bi.mapper.ReissueOrderProductMapper;
import com.me.modules.bi.service.ReissueOrderProductService;
import org.springframework.stereotype.Service;

@Service
public class ReissueOrderProductServiceImpl extends ServiceImpl<ReissueOrderProductMapper, ReissueOrderProduct> implements ReissueOrderProductService {
}
