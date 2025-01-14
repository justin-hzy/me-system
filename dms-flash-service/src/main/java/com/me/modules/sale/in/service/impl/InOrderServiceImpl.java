package com.me.modules.sale.in.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.common.config.FlashConfig;
import com.me.common.core.JsonResult;
import com.me.modules.http.service.FlashHttpService;
import com.me.modules.json.JsonService;
import com.me.modules.sale.in.dto.PutInOrderDto;
import com.me.modules.sale.in.service.InOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class InOrderServiceImpl implements InOrderService {

    private FlashConfig flashConfig;

    private FlashHttpService flashHttpService;

    private JsonService jsonService;

    @Override
    public JsonResult putFlashInOrder(PutInOrderDto dto) throws IOException {
        Map<String,String> commonParam = flashHttpService.createCommonParam();
        log.info("commonParam="+commonParam.toString());

        JSONObject putInOrderJson = jsonService.createPutInOrderJson(dto);
        log.info(putInOrderJson.toJSONString());

        String key = flashConfig.getKey1();

        String sign = flashHttpService.generateSign(commonParam,key,putInOrderJson.toJSONString());
        log.info(sign);

        String url = flashHttpService.joinUrl(commonParam,sign,flashConfig.getPutInOrderUrl());
        log.info(url);

        flashHttpService.doAction(url,putInOrderJson);

        return JsonResult.ok();
    }
}