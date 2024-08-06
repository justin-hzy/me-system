package com.me.nascent.modules.grade.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.service.TransGradeService;

import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.systemCustomer.SystemCustomerInfo;
import com.nascent.ecrp.opensdk.request.customer.SystemCustomerGetRequest;
import com.nascent.ecrp.opensdk.response.customer.SystemCustomerGetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TransGradeServiceImpl implements TransGradeService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;



    @Override
    public void TransGrade() throws Exception {

        SystemCustomerGetRequest request = new SystemCustomerGetRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());

        request.setAccessToken(tokenService.getToken());

        ApiClient client = new ApiClientImpl(request);

        SystemCustomerGetResponse response = client.execute(request);

        log.info(response.getBody());

        /*SystemCustomerInfo systemCustomerInfo = response.getResult();

        log.info("systemCustomerInfo="+systemCustomerInfo.toString());*/

    }
}
