package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MaBangConfig {

    @Value("${maBang.appKey}")
    private String appKey;

    @Value("${maBang.appToken}")
    private String appToken;

    @Value("${maBang.url.reqUrl}")
    private String reqUrl;

    @Value("${maBang.url.orderUrl}")
    private String orderUrl;
}
