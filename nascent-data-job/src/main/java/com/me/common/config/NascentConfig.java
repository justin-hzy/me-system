package com.me.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
public class NascentConfig {

    @Value("${nascent.btnServerUrl}")
    private String btnServerUrl;

    @Value("${nascent.btnAppKey}")
    private String btnAppKey;

    @Value("${nascent.btnAppSerect}")
    private String btnAppSerect;

    @Value("${nascent.btnGroupID}")
    private Long btnGroupID;

}
