package com.me.nascent.modules.qimen.service.impl;

import com.me.nascent.modules.qimen.service.QiMenPutCustomerService;
import com.me.nascent.modules.qimen.service.QiMenTransTradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutCustomerServiceImpl implements QiMenPutCustomerService {

    private QiMenTransTradeService qiMenTransTradeService;

    @Override
    public void putQiMenCustomer() {
        //qiMenTransTradeService.transTrade();
    }
}
