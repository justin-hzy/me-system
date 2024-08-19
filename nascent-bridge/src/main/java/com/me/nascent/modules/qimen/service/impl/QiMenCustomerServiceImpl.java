package com.me.nascent.modules.qimen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.mapper.QiMenCustomerMapper;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import org.springframework.stereotype.Service;

@Service
public class QiMenCustomerServiceImpl extends ServiceImpl<QiMenCustomerMapper, QiMenCustomer> implements QiMenCustomerService {
}
