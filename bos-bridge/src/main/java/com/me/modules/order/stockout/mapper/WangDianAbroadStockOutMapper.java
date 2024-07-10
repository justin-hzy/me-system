package com.me.modules.order.stockout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WangDianAbroadStockOutMapper extends BaseMapper<WangDianAbroadStockOut> {

    List<WangDianAbroadStockOut> queryUnTransWangDianAbroadStockOut();
}
