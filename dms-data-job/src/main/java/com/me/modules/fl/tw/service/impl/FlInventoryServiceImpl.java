package com.me.modules.fl.tw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.me.modules.fl.tw.entity.FlInventory;
import com.me.modules.fl.tw.mapper.FlInventoryMapper;
import com.me.modules.fl.tw.service.FlInventoryService;
import org.springframework.stereotype.Service;

@Service
public class FlInventoryServiceImpl extends ServiceImpl<FlInventoryMapper, FlInventory> implements FlInventoryService {
}
