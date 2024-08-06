package com.me.nascent.modules.point.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.point.service.TransPointService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.point.CustomerPointInfoQueryRequest;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.refund.RefundInfoSynResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TransPointServiceImpl implements TransPointService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    @Override
    public void transReOrder() throws Exception {

        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount("pcode-282900");

        ApiClient client = new ApiClientImpl(request);
        CustomerPointInfoQueryResponse response = client.execute(request);
        log.info(response.getBody());

    }
}
