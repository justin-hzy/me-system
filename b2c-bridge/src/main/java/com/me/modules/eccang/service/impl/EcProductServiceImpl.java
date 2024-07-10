package com.me.modules.eccang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.eccang.entity.EcProduct;
import com.me.modules.eccang.mapper.EcProductMapper;
import com.me.modules.eccang.service.EcProductService;
import org.springframework.stereotype.Service;

@Service
public class EcProductServiceImpl extends ServiceImpl<EcProductMapper, EcProduct> implements EcProductService {
}
