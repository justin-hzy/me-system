package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class FlashConfig {

    @Value("${flash.merchantId}")
    private String merchantId;

    @Value("${flash.key}")
    private String key;

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
