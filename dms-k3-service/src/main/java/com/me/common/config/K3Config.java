package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class K3Config {

    @Value("${k3.hk.appId}")
    private String appId;

    @Value("${k3.hk.dCID}")
    private String dCID;

    @Value("${k3.hk.appSecret}")
    private String appSecret;

    @Value("${k3.tw.appId}")
    private String twAppId;

    @Value("${k3.tw.dCID}")
    private String twdCID;

    @Value("${k3.tw.appSecret}")
    private String twAppSecret;

    @Value("${k3.userName}")
    private String userName;

    @Value("${k3.lCID}")
    private Integer lCID;

    @Value("${k3.serverUrl}")
    private String serverUrl;

    @Value("${k3.produceDate}")
    private String FProduceDate;

    @Value("${k3.expiryDate}")
    private String FExpiryDate;

    @Value("${k3.flot}")
    private String flot;




}
