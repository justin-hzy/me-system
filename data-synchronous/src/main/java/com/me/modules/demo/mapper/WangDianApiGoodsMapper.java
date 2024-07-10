package com.me.modules.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.modules.demo.entity.WangDianApiGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WangDianApiGoodsMapper extends BaseMapper<WangDianApiGoods> {

    WangDianApiGoods queryWangDianApiGoodsById(@Param("id") String id);

    void insertWangDianApiGoodsBySql(WangDianApiGoods wangDianApiGoods);
}
