package com.me.modules.order.stockout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;

import java.util.List;

public interface WangDianAbroadStockOutService extends IService<WangDianAbroadStockOut> {

    List<WangDianAbroadStockOut> queryUnTransWangDianAbroadStockOut();
}
