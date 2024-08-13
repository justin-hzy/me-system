package com.me.nascent.modules.point.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.MemberNickInfo;
import com.me.nascent.modules.member.service.MemberNickInfoService;
import com.me.nascent.modules.point.service.TransPointService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickPlatform;
import com.nascent.ecrp.opensdk.request.point.CustomerPointInfoQueryRequest;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.refund.RefundInfoSynResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransPointServiceImpl implements TransPointService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private MemberNickInfoService memberNickInfoService;

    @Override
    public void transPoint(String integralAccount,Long viewId) throws Exception {

        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount(integralAccount);
        request.setViewId(viewId);

        List<MemberNickInfo> memberNickInfos = memberNickInfoService.list();


        int batchSize = 100; // 每次处理的数据量
        //int totalSize = memberNickInfos.size(); // 总数据量
        int totalSize = 200;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<MemberNickInfo> batchList = memberNickInfos.subList(start,end);
            log.info("batchList="+batchList.toString());

            List<NickPlatform> nickPlatforms = new ArrayList<>();

            for (MemberNickInfo obj : batchList){
                NickPlatform nickPlatform = new NickPlatform();
                BeanUtils.copyProperties(obj,nickPlatform);
                nickPlatforms.add(nickPlatform);
            }
            request.setNickList(nickPlatforms);

            ApiClient client = new ApiClientImpl(request);
            CustomerPointInfoQueryResponse response = client.execute(request);
            log.info(response.getBody());

        }



    }
}
