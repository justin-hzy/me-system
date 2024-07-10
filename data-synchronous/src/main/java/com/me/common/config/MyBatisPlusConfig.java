package com.me.common.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.me.common.handler.OracleNumberTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class MyBatisPlusConfig {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {

            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.getTypeHandlerRegistry().register(BigDecimal.class, OracleNumberTypeHandler.class);
            }
        };
    }
}