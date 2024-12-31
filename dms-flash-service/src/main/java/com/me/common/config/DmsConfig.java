package com.me.common.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DmsConfig {


    @Value("${dms.ip}")
    private String ip;

    @Value("${dms.url}")
    private String url;

    @Value("${dms.order.outOrderDtl2}")
    private String outOrderDtl2;

    @Value("${dms.order.inOrderDtl2}")
    private String inOrderDtl2;


}
