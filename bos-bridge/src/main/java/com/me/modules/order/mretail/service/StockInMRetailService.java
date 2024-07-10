package com.me.modules.order.mretail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.order.mretail.dto.TransStockInMRetailReqDto;
import com.me.modules.order.mretail.entity.MRetail;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import org.apache.ibatis.annotations.Param;

public interface StockInMRetailService{

    void transStockInMRetail(WangDianAbroadStockIn order, TransStockInMRetailReqDto reqDto);
}
