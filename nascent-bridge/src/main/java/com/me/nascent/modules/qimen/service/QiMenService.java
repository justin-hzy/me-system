package com.me.nascent.modules.qimen.service;

import com.me.nascent.modules.qimen.dto.QiMenDto;

import java.text.ParseException;

public interface QiMenService {

    String transQiMen(QiMenDto dto);

    void putQiMenCustomer() throws Exception;

    void putQiMenGrade() throws Exception;

    void putQiMenPoint() throws Exception;

    void putQiMenTrade() throws Exception;

    void putQiMenReFund() throws Exception;
}
