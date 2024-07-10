package com.me.modules.order.stockin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.stockin.entity.WangDianAbroadStockInList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WangDianAbroadStockInListMapper extends BaseMapper<WangDianAbroadStockInList> {


    Integer queryCount(@Param("stockinid") String stockInId);

    String querySku(@Param("stockinid") String stockInId);
}
