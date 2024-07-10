package com.me.modules.dms.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.dms.sys.entity.UfCity;
import com.me.modules.dms.sys.mapper.UfCityMapper;
import com.me.modules.dms.sys.service.UfCityService;
import org.springframework.stereotype.Service;

@Service
public class UfCityServiceImpl extends ServiceImpl<UfCityMapper, UfCity> implements UfCityService {
}
