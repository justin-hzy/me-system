package com.me.member;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.customer.ActivateCustomerListSyncRequest;
import com.nascent.ecrp.opensdk.response.customer.ActivateCustomerListSyncResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootTest
@Slf4j
public class MemberTest {

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;

    @Test
    public void transMemberTest() throws Exception {
        ActivateCustomerListSyncRequest request = new ActivateCustomerListSyncRequest();

        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());

        request.setAccessToken(tokenService.getToken());
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse("2024-07-31 00:00:00", formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime endDateTime = LocalDateTime.parse("2024-08-01 00:00:00", formatter);
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setActivate(true);
        request.setNextId(0L);
        request.setPageSize(50);
        //request.setViewId(200001728L);
        request.setShopId(3411331L);
        ApiClient client = new ApiClientImpl(request);
        ActivateCustomerListSyncResponse response = client.execute(request);
        log.info(response.getBody());
    }
}
