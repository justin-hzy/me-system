package com.me.nascent.modules.reorder.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.reorder.service.TransReOrderService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.refund.RefundInfo;
import com.nascent.ecrp.opensdk.domain.refund.RefundSynInfo;
import com.nascent.ecrp.opensdk.request.refund.RefundInfoSynRequest;
import com.nascent.ecrp.opensdk.request.trade.TradeSynRequest;
import com.nascent.ecrp.opensdk.response.refund.RefundInfoSynResponse;
import com.nascent.ecrp.opensdk.response.trade.TradeSynResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransReOrderServiceImpl implements TransReOrderService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    @Override
    public void transReOrder(Long id, Date startDate, Date endDate) throws Exception {
        RefundInfoSynRequest request = new RefundInfoSynRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setId(id);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        RefundInfoSynResponse response = client.execute(request);
        log.info(response.getBody());
        List<RefundSynInfo> refundSynInfos = response.getResult();

        /*if(refundSynInfos.size()>0){

        }*/
    }
}
