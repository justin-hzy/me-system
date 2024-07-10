package com.me.modules.order.stockin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.stockin.entity.WangDianAbroadStockInList;
import com.me.modules.order.stockin.mapper.WangDianAbroadStockInListMapper;
import com.me.modules.order.stockin.service.WangDianAbroadStockInListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WangDianAbroadStockInListServiceImpl extends ServiceImpl<WangDianAbroadStockInListMapper, WangDianAbroadStockInList> implements WangDianAbroadStockInListService {

    private WangDianAbroadStockInListMapper wangDianAbroadStockInListMapper;

    @Override
    public Integer queryCount(String stockInId) {
        return wangDianAbroadStockInListMapper.queryCount(stockInId);
    }

    @Override
    public String querySku(String stockInId) {
        return wangDianAbroadStockInListMapper.querySku(stockInId);
    }

}
