package com.me.nascent.modules.qimen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.qimen.entity.QiMenOrder;
import com.me.nascent.modules.qimen.mapper.QiMenOrderMapper;
import com.me.nascent.modules.qimen.service.QiMenOrderService;
import org.springframework.stereotype.Service;

@Service
public class QiMenOrderServiceImpl extends ServiceImpl<QiMenOrderMapper,QiMenOrder> implements QiMenOrderService {
}
