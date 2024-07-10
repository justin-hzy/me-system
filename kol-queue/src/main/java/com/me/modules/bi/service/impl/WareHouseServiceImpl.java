package com.me.modules.bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.bi.entity.WareHouse;
import com.me.modules.bi.mapper.WareHouseMapper;
import com.me.modules.bi.service.WareHouseService;
import org.springframework.stereotype.Service;

@Service
public class WareHouseServiceImpl extends ServiceImpl<WareHouseMapper, WareHouse> implements WareHouseService {
}
