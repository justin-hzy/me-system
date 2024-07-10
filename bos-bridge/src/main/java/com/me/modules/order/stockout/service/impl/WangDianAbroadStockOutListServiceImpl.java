package com.me.modules.order.stockout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOutList;
import com.me.modules.order.stockout.mapper.WangDianAbroadStockOutListMapper;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WangDianAbroadStockOutListServiceImpl extends ServiceImpl<WangDianAbroadStockOutListMapper, WangDianAbroadStockOutList> implements WangDianAbroadStockOutListService {

    private WangDianAbroadStockOutListMapper wangDianAbroadStockOutListMapper;

    @Override
    public String queryCount(String stockOutId) {
        return wangDianAbroadStockOutListMapper.queryCount(stockOutId);
    }

    @Override
    public String querySku(String stockOutId) {
        return wangDianAbroadStockOutListMapper.querySku(stockOutId);
    }
}
