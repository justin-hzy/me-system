package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BosConfig {

    @Value("${bos.sipAppKey}")
    private String sipAppKey;

    @Value("${bos.appSecret}")
    private String appSecret;

    @Value("${bos.url}")
    private String url;
}
