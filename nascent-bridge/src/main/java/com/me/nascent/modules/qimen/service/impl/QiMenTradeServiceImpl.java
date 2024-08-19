package com.me.nascent.modules.qimen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.nascent.modules.qimen.entity.QiMenTrade;
import com.me.nascent.modules.qimen.mapper.QiMenTradeMapper;
import com.me.nascent.modules.qimen.service.QiMenTradeService;
import org.springframework.stereotype.Service;

@Service
public class QiMenTradeServiceImpl extends ServiceImpl<QiMenTradeMapper, QiMenTrade> implements QiMenTradeService {
}
