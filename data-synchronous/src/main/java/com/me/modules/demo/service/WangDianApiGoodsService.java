package com.me.modules.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.common.core.JsonResult;
import com.me.modules.demo.dto.DemoReqDto;
import com.me.modules.demo.entity.BPayable;
import com.me.modules.demo.entity.WangDianApiGoods;

public interface WangDianApiGoodsService extends IService<WangDianApiGoods> {

    JsonResult<WangDianApiGoods> queryWangDianApiGoodsById (WangDianApiGoods wangDianApiGoods);

    JsonResult<WangDianApiGoods> insertWangDianApiGoods (WangDianApiGoods wangDianApiGoods);

    void insertWangDianApiGoodsBySql (WangDianApiGoods wangDianApiGoods);
}
