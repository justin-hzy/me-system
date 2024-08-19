package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.QiMenTransCustomerService;
import com.me.nascent.modules.qimen.service.QiMenTransReFundService;
import com.me.nascent.modules.qimen.service.QiMenService;
import com.me.nascent.modules.qimen.service.QiMenTransTradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenServiceImpl implements QiMenService {


    private QiMenTransTradeService qiMenTransTradeService;

    private QiMenTransCustomerService qiMenTransCustomerService;

    private QiMenTransReFundService qiMenTransReFundService;


    @Override
    public String transOrder(QiMenDto dto) throws Exception {
        log.info(dto.toString());

        String dataType = dto.getDataType();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code","FAIL");
        jsonObject.put("return_msg","数据处理失败");
        String resJson = jsonObject.toJSONString();

        if("CUSTOMER".equals(dataType)){
            resJson = qiMenTransCustomerService.transCustomer(dto);
        }else if("TRADE".equals(dataType)){
            resJson = qiMenTransTradeService.transTrade(dto);
        }else if("REFUND".equals(dataType)){
            resJson = qiMenTransReFundService.transReFund(dto);
        }

        return resJson;
    }
}
