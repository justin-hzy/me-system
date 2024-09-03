package com.me.nascent.modules.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.member.entity.ShopActiveCustomer;
import com.me.nascent.modules.member.mapper.ShopActiveCustomerMapper;
import com.me.nascent.modules.member.service.ShopActiveCustomerService;
import org.springframework.stereotype.Service;

@Service
public class ShopActiveCustomerServiceImpl extends ServiceImpl<ShopActiveCustomerMapper, ShopActiveCustomer> implements ShopActiveCustomerService {
}
