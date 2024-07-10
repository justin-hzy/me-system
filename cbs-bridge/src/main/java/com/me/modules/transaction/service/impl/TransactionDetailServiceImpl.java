package com.me.modules.transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.modules.transaction.entity.TransactionDetail;
import com.me.modules.transaction.mapper.TransactionDetailMapper;
import com.me.modules.transaction.service.TransactionDetailService;
import org.springframework.stereotype.Service;

@Service
public class TransactionDetailServiceImpl extends ServiceImpl<TransactionDetailMapper, TransactionDetail> implements TransactionDetailService {
}
