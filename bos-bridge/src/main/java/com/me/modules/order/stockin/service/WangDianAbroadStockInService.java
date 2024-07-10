package com.me.modules.order.stockin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WangDianAbroadStockInService extends IService<WangDianAbroadStockIn> {

    List<WangDianAbroadStockIn> queryUnTransWangDianAbroadStockIn();

    void updateError(String orderNo,String message);



}
