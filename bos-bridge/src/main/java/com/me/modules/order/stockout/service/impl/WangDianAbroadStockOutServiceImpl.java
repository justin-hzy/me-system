package com.me.modules.order.stockout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.stockout.mapper.WangDianAbroadStockOutMapper;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class WangDianAbroadStockOutServiceImpl extends ServiceImpl<WangDianAbroadStockOutMapper, WangDianAbroadStockOut>
        implements WangDianAbroadStockOutService {

    private WangDianAbroadStockOutMapper abroadStockOutMapper;

    @Override
    public List<WangDianAbroadStockOut> queryUnTransWangDianAbroadStockOut() {
        return abroadStockOutMapper.queryUnTransWangDianAbroadStockOut();
    }
}
