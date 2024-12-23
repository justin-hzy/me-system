package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class DmsConfig {


    @Value("${dms.orderUrl}")
    private String orderUrl;
}
