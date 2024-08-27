package com.me.nascent.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
public class NascentConfig {

    @Value("${nascent.serverUrl}")
    private String serverUrl;

    @Value("${nascent.appKey}")
    private String appKey;

    @Value("${nascent.appSerect}")
    private String appSerect;

    @Value("${nascent.groupID}")
    private Long groupID;

    @Value("${nascent.btnServerUrl}")
    private String btnServerUrl;

    @Value("${nascent.btnAppKey}")
    private String btnAppKey;

    @Value("${nascent.btnAppSerect}")
    private String btnAppSerect;

    @Value("${nascent.btnGroupID}")
    private Long btnGroupID;
}
