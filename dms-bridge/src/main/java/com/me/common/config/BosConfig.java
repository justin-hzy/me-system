package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BosConfig {

    @Value("${bos.sipAppKey}")
    String sipAppKey;

    @Value("${bos.appSecret}")
    String appSecret;

    @Value("${bos.url}")
    String url;
}
