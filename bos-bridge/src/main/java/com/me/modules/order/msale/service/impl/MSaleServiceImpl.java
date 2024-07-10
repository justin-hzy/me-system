package com.me.modules.order.msale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.msale.entity.MSale;
import com.me.modules.order.msale.mapper.MSaleMapper;
import com.me.modules.order.msale.service.MSaleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MSaleServiceImpl extends ServiceImpl<MSaleMapper, MSale> implements MSaleService {

    private MSaleMapper mSaleMapper;

    @Override
    public Integer queryCount(String tid) {
        return mSaleMapper.queryCount(tid);
    }
}
