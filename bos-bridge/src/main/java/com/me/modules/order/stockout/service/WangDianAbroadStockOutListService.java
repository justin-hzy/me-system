package com.me.modules.order.stockout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOutList;

public interface WangDianAbroadStockOutListService extends IService<WangDianAbroadStockOutList> {

    String queryCount(String stockOutId);

    String querySku(String stockOutId);
}
