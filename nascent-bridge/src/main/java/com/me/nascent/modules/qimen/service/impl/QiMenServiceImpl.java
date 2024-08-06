package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenReFundService;
import com.me.nascent.modules.qimen.service.QiMenService;
import com.me.nascent.modules.qimen.service.QiMenTradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenServiceImpl implements QiMenService {


    private QiMenTradeService qiMenTradeService;

    private QiMenCustomerService qiMenCustomerService;

    private QiMenReFundService qiMenReFundService;


    @Override
    public String transOrder(QiMenDto dto) {
        log.info(dto.toString());

        String dataType = dto.getDataType();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code","FAIL");
        jsonObject.put("return_msg","类型错误");
        String resJson = jsonObject.toJSONString();

        if("CUSTOMER".equals(dataType)){
            resJson = qiMenCustomerService.transCustomer(dto);
        }else if("TRADE".equals(dataType)){
            resJson = qiMenTradeService.transTrade(dto);
        }else if("REFUND".equals(dataType)){
            resJson = qiMenReFundService.transReFund(dto);
        }

        return resJson;
    }
}
