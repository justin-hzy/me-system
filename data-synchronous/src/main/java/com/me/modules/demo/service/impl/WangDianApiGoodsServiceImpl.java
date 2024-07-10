package com.me.modules.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.common.core.JsonResult;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.BPayable;
import com.me.modules.demo.entity.WangDianApiGoods;
import com.me.modules.demo.mapper.BPayableMapper;
import com.me.modules.demo.mapper.WangDianApiGoodsMapper;
import com.me.modules.demo.service.BPayableService;
import com.me.modules.demo.service.WangDianApiGoodsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class WangDianApiGoodsServiceImpl extends ServiceImpl<WangDianApiGoodsMapper, WangDianApiGoods> implements WangDianApiGoodsService {

    private WangDianApiGoodsMapper wangDianApiGoodsMapper;

    @Override
    public JsonResult<WangDianApiGoods> queryWangDianApiGoodsById(WangDianApiGoods wangDianApiGoods) {
        WangDianApiGoods wangDianApiGoods1 = wangDianApiGoodsMapper.queryWangDianApiGoodsById(wangDianApiGoods.getId().toString());
        log.info("wangDianApiGoods"+wangDianApiGoods1.toString());
        return JsonResult.ok();
    }

    @Override
    public JsonResult<WangDianApiGoods> insertWangDianApiGoods(WangDianApiGoods wangDianApiGoods) {
        wangDianApiGoodsMapper.insert(wangDianApiGoods);
        return JsonResult.ok();
    }

    @Override
    public void insertWangDianApiGoodsBySql(WangDianApiGoods wangDianApiGoods) {
        wangDianApiGoodsMapper.insertWangDianApiGoodsBySql(wangDianApiGoods);
    }


}
