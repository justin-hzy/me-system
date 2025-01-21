package com.me.modules.sale.out.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.common.config.FlashConfig;
import com.me.common.core.JsonResult;
import com.me.modules.http.service.FlashHttpService;
import com.me.modules.json.JsonService;
import com.me.modules.sale.out.dto.PutOutOrderDto;
import com.me.modules.sale.out.service.OutOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class OutOrderServiceImpl implements OutOrderService {

    private FlashConfig flashConfig;

    private FlashHttpService flashHttpService;

    private JsonService jsonService;

    @Override
    public JSONObject putFlashOutOrder(PutOutOrderDto dto) throws IOException {

        String storeCode = dto.getStoreCode();

        Map<String,String> commonParam = flashHttpService.createCommonParam(storeCode);
        log.info("commonParam="+commonParam.toString());

        JSONObject putOutOrderJson = jsonService.createPutOutOrderJson(dto);
        log.info(putOutOrderJson.toJSONString());

        String key = "";
        if("ME01".equals(storeCode)){
            key = flashConfig.getKey1();
        }else if ("ME02".equals(storeCode)){
            key = flashConfig.getKey2();
        }else if("YJDR".equals(storeCode)){
            key = flashConfig.getKey3();
        }



        String sign = flashHttpService.generateSign(commonParam,key,putOutOrderJson.toJSONString());
        log.info(sign);

        String url = flashHttpService.joinUrl(commonParam,sign,flashConfig.getPutOutOrderUrl());
        log.info(url);

        JSONObject respJson  = flashHttpService.doAction(url,putOutOrderJson);

        return respJson;
    }
}