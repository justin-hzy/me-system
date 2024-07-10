package com.me.modules.fl.tw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.me.modules.fl.tw.entity.FlStoreType;
import com.me.modules.fl.tw.mapper.FlStoreTypeMapper;
import com.me.modules.fl.tw.service.FlStoreTypeService;
import org.springframework.stereotype.Service;

@Service
public class FlStoreTypeServiceImpl extends ServiceImpl<FlStoreTypeMapper, FlStoreType> implements FlStoreTypeService {
}
