package com.me.nascent.modules.qimen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.*;
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

    private QiMenPutCustomerService qiMenPutCustomerService;

    private QiMenPutGradeService qiMenPutGradeService;

    private QiMenPutPointService qiMenPutPointService;

    private QiMenPutTradeService qiMenPutTradeService;

    private QiMenPutReFundService qiMenPutReFundService;


    @Override
    public String transQiMen(QiMenDto dto) throws Exception {
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

    @Override
    public void putQiMenCustomer() throws Exception {
        qiMenPutCustomerService.putQiMenCustomer();
    }

    @Override
    public void putQiMenGrade() throws Exception {
        qiMenPutGradeService.putQiMenGrade();
    }

    @Override
    public void putQiMenPoint() throws Exception {
        qiMenPutPointService.putQiMenPoint();
    }

    @Override
    public void putQiMenTrade() throws Exception {
        qiMenPutTradeService.putQiMenTrade();

    }

    @Override
    public void putQiMenReFund() throws Exception {
        qiMenPutReFundService.putQiMenReFund();
    }
}
