package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Data
public class ByteNewConfig {

    @Value("${byteNew.key}")
    private String app_key;

    @Value("${byteNew.access_token}")
    private String access_token;

    @Value("${byteNew.app_secret}")
    private String app_secret;
}
