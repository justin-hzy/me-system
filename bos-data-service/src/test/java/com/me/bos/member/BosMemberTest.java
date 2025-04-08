package com.me.bos.member;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.config.NascentConfig;
import com.me.modules.bos.member.dto.QueryBosMemberDto;
import com.me.modules.bos.member.entity.BosMember;
import com.me.modules.bos.member.mapper.BosMemberMapper;


import com.me.modules.bos.member.service.BosMemberService;
import com.me.modules.nascent.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.request.customer.Customer4ThirdPartyGetRequest;
import com.nascent.ecrp.opensdk.response.basis.AccessTokenRegisterResponse;
import com.nascent.ecrp.opensdk.response.customer.Customer4ThirdPartyGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class BosMemberTest {


    @Autowired
    BosMemberMapper bosMemberMapper;

    @Autowired
    private NascentConfig nascentConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    BosMemberService bosMemberService;


    @Test
    public void getMemberTest() throws Exception {


        String accessToken = tokenService.getBtnToken();

        List<QueryBosMemberDto> queryBosMemberDtos = bosMemberMapper.queryBosMember();


        for (QueryBosMemberDto dto : queryBosMemberDtos){
            log.info("手机号="+dto.getCARDNO());
            Customer4ThirdPartyGetRequest request = new Customer4ThirdPartyGetRequest();
            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setAppSecret(nascentConfig.getBtnAppSerect());
            request.setGroupId(nascentConfig.getBtnGroupID());
            request.setViewBindMobile(dto.getCARDNO());
            request.setAccessToken(accessToken);
            request.setViewId(100000751L);
            /*13904147938*/
            ApiClient client = new ApiClientImpl(request);
            Customer4ThirdPartyGetResponse response = client.execute(request);
            log.info(response.getBody());
            if("200".equals(response.getCode())){
                List<NickInfo> nickInfos = response.getResult().getNickInfoList();
                if (CollUtil.isNotEmpty(nickInfos)){
                    NickInfo nickInfo = nickInfos.get(0);
                    String nasOuid = nickInfo.getNasOuid();
                    log.info("会员id="+nasOuid);

                    UpdateWrapper<BosMember> memberUpdate = new UpdateWrapper<>();
                    memberUpdate.lambda().eq(BosMember::getCardNo,dto.getCARDNO())
                            .set(BosMember::getNasouId,nasOuid);

                    bosMemberService.update(memberUpdate);


                }else {
                    log.info("南讯会员id数组回空");
                }
            }else {
                log.info(response.getMsg());
            }
        }
    }
}
