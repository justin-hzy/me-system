package com.me.modules.order.stockin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.stockin.entity.WangDianAbroadStockInList;
import org.apache.ibatis.annotations.Param;

public interface WangDianAbroadStockInListService extends IService<WangDianAbroadStockInList> {

    Integer queryCount(String stockInId);

    String querySku(String stockInId);
}
