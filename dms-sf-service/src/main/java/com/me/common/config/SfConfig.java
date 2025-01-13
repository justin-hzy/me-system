package com.me.common.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SfConfig {

    @Value("${sf.th01.accessCode1}")
    private String accessCode1;

    @Value("${sf.th01.checkWord1}")
    private String checkWord1;

    @Value("${sf.th01.key1}")
    private String key1;

    @Value("${sf.th02.accessCode2}")
    private String accessCode2;

    @Value("${sf.th02.checkWord2}")
    private String checkWord2;

    @Value("${sf.th02.key2}")
    private String key2;

    @Value("${sf.url}")
    private String url;

    @Value("${sf.saleOrderServiceCode}")
    private String SaleOrderServiceCode;


    @Value("${sf.saleOrderDtlServiceCode}")
    private String saleOrderDtlServiceCode;

    @Value("${sf.purOrderServiceCode}")
    private String purOrderServiceCode;

    @Value("${sf.purOrderDtlServiceCode}")
    private String purOrderDtlServiceCode;

    @Value("${sf.cancelSaleOrderServiceCode}")
    private String cancelSaleOrderServiceCode;

    @Value("${sf.cancelPurOrderServiceCode}")
    private String cancelPurOrderServiceCode;
}
