package com.me.modules.nascent.token.service.impl;

import com.me.common.config.NascentConfig;
import com.me.modules.nascent.token.service.TokenService;
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
    public String getBtnToken() throws Exception {
        AccessTokenRegisterRequest request = new AccessTokenRegisterRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setForceRefresh(false);

        ApiClient client = new ApiClientImpl(request);
        AccessTokenRegisterResponse response = client.execute(request);
            /*log.info("token="+response.getResult().getAccessToken());*/

        return response.getResult().getAccessToken();
    }
}
