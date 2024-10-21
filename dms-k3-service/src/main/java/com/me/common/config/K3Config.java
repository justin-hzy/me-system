package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class K3Config {

    @Value("${K3.appId}")
    private String appId;

    @Value("${K3.dCID}")
    private String dCID;

    @Value("${K3.appSecret}")
    private String appSecret;

    @Value("${K3.userName}")
    private String userName;

    @Value("${K3.lCID}")
    private Integer lCID;

    @Value("${K3.serverUrl}")
    private String serverUrl;

    @Value("${twk3.twAppId}")
    private String twAppId;

    @Value("${twk3.twDCID}")
    private String twDCID;

    @Value("${twk3.twAppSecret}")
    private String twAppSecret;

    @Value("${twk3.twUserName}")
    private String twUserName;
}
