package com.me.member;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.service.TransMemberService;
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
    private TransMemberService transMemberService;


    @Test
    public void transMemberTest() throws Exception {
        transMemberService.TransMember();
    }
}
