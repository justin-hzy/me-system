package com.me.token;

import cn.hutool.core.util.StrUtil;
import com.me.nascent.common.config.NascentConfig;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.basis.AccessTokenRegisterRequest;
import com.nascent.ecrp.opensdk.response.basis.AccessTokenRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class TokenTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Test
    public void getToken(){
        log.info(nascentConfig.toString());
        AccessTokenRegisterRequest request = new AccessTokenRegisterRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setForceRefresh(false);


        ApiClient client = new ApiClientImpl(request);
        try {
            AccessTokenRegisterResponse response = client.execute(request);
            if (null != response) {
                log.info(response.getSuccess()+"");
                log.info(response.getResult().getAccessToken());
                if(StrUtil.isNotEmpty(response.getSuccess()+"") && response.getSuccess()){
                    //return response.getResult();
                }

            }
        } catch (Exception e) {
            log.info("查询开放平台获取Token异常！", e);
        }
    }



}
