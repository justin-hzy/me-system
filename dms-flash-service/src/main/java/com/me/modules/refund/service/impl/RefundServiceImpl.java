package com.me.modules.refund.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.me.common.config.FlashConfig;
import com.me.common.core.JsonResult;
import com.me.modules.http.service.FlashHttpService;
import com.me.modules.json.JsonService;
import com.me.modules.refund.dto.PutRefundDto;
import com.me.modules.refund.service.RefundService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class RefundServiceImpl implements RefundService {


    private FlashConfig flashConfig;

    private FlashHttpService flashHttpService;

    private JsonService jsonService;

    @Override
    public JsonResult putFlashRefund(PutRefundDto dto) throws IOException {
        String storeCode = dto.getStoreCode();
        Map<String,String> commonParam = flashHttpService.createCommonParam(storeCode);
        log.info("commonParam="+commonParam.toString());

        JSONObject putRefundJson = jsonService.createPutRefundJson(dto);
        log.info(putRefundJson.toJSONString());

        String key = "";
        if("ME01".equals(storeCode)){
            key = flashConfig.getKey1();
        }else if ("ME02".equals(storeCode)){
            key = flashConfig.getKey2();
        }else if("YJDR".equals(storeCode)){
            key = flashConfig.getKey3();
        }

        String sign = flashHttpService.generateSign(commonParam,key,putRefundJson.toJSONString());
        log.info(sign);

        String url = flashHttpService.joinUrl(commonParam,sign,flashConfig.getPutRefundUrl());
        log.info(url);

        flashHttpService.doAction(url,putRefundJson);

        return JsonResult.ok();
    }
}
