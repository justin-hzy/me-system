package com.me.nascent.modules.qimen.service;

import com.me.nascent.modules.qimen.dto.QiMenDto;

import java.text.ParseException;

public interface QiMenService {

    String transOrder(QiMenDto dto) throws Exception;
}
