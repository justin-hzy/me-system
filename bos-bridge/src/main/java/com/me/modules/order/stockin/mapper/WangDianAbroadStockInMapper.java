package com.me.modules.order.stockin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WangDianAbroadStockInMapper extends BaseMapper<WangDianAbroadStockIn> {

    List<WangDianAbroadStockIn> queryUnTransWangDianAbroadStockIn();

    Integer updateError(@Param("orderno") String orderNo,@Param("message") String message);



}
