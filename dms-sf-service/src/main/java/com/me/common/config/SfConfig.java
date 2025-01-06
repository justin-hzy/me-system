package com.me.common.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SfConfig {

    @Value("${sf.companyCode}")
    private String companyCode;

    @Value("${sf.accessCode}")
    private String accessCode;

    @Value("${sf.checkWord}")
    private String checkWord;

    @Value("${sf.url}")
    private String url;

    @Value("${sf.key}")
    private String key;

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
