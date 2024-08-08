package com.me.nascent.modules.token.service.impl;

import cn.hutool.core.util.StrUtil;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.basis.AccessTokenRegisterRequest;
import com.nascent.ecrp.opensdk.response.basis.AccessTokenRegisterResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private NascentConfig nascentConfig;

    @Override
    public String getToken() throws Exception {
        log.info(nascentConfig.toString());
        AccessTokenRegisterRequest request = new AccessTokenRegisterRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setForceRefresh(false);


        ApiClient client = new ApiClientImpl(request);
        AccessTokenRegisterResponse response = client.execute(request);
        if (null != response) {
//            log.info(response.getSuccess()+"");
//            log.info(response.getResult().getAccessToken());
            if(StrUtil.isNotEmpty(response.getSuccess()+"") && response.getSuccess()){
                return response.getResult().getAccessToken();
            }else {
                return "";
            }
        }else {
            return "";
        }

    }
}
