package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FlashConfig {

    @Value("${flash.me01.merchantId}")
    private String merchantId1;

    @Value("${flash.me01.key}")
    private String key1;

    @Value("${flash.me02.merchantId}")
    private String merchantId2;

    @Value("${flash.me02.key}")
    private String key2;


    @Value("${flash.putOutOrderUrl}")
    private String putOutOrderUrl;

    @Value("${flash.putInOrderUrl}")
    private String putInOrderUrl;

    @Value("${flash.outOrderDetailUrl}")
    private String outOrderDetailUrl;

    @Value("${flash.inOrderDetailUrl}")
    private String inOrderDetailUrl;

    @Value("${flash.putRefundUrl}")
    private String putRefundUrl;

}
