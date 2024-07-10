package com.me.modules.order.stockin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import com.me.modules.order.stockin.mapper.WangDianAbroadStockInMapper;
import com.me.modules.order.stockin.service.WangDianAbroadStockInService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WangDianAbroadStockInServiceImpl extends ServiceImpl<WangDianAbroadStockInMapper, WangDianAbroadStockIn> implements WangDianAbroadStockInService {

    private WangDianAbroadStockInMapper wangDianAbroadStockInMapper;


    @Override
    public List<WangDianAbroadStockIn> queryUnTransWangDianAbroadStockIn() {
        return wangDianAbroadStockInMapper.queryUnTransWangDianAbroadStockIn();
    }

    @Override
    public void updateError(String orderNo,String message) {
        wangDianAbroadStockInMapper.updateError(orderNo,message);
    }
}
