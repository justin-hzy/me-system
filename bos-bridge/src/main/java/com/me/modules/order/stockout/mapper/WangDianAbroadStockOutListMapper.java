package com.me.modules.order.stockout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOutList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WangDianAbroadStockOutListMapper extends BaseMapper<WangDianAbroadStockOutList> {

    String queryCount(@Param("stockOutId") String stockOutId);

    String querySku(@Param("stockOutId") String stockOutId);
}
