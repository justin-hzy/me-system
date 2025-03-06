package com.me.nascent.point;

import com.me.common.config.NascentConfig;
import com.me.common.core.JsonResult;
import com.me.modules.bos.point.service.BosMemberService;
import com.me.modules.nascent.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickPlatform;
import com.nascent.ecrp.opensdk.request.point.CustomerPointInfoQueryRequest;
import com.nascent.ecrp.opensdk.request.point.PointInfoGetRequest;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.point.PointInfoGetResponse;
import com.nascent.ecrp.opensdk.response.point.PointLogInfoQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TransPointSumServiceTest {

    @Autowired
    BosMemberService bosMemberService;

    @Autowired
    NascentConfig nascentConfig;

    @Autowired
    TokenService tokenService;


    @Test
    void transPointSum() throws Exception {

        JsonResult countJS = bosMemberService.getBosMemberCount();

        List<Map<String,String>> pointList = null;

        if("200".equals(countJS.getCode())) {
            Integer count = (Integer) countJS.getData();
            log.info("伯俊会员总记录数:" + count);
            JsonResult offPointJS = bosMemberService.getBosOffPoint(count);
            if ("200".equals(offPointJS.getCode())) {
                pointList = (List<Map<String,String>>) offPointJS.getData();
                log.info("伯俊POS会员总记录数:" + pointList.size());
                //log.info(pointList.toString());
            }
        }

        List<String> cardNos = new ArrayList<>();

        Iterator<Map<String,String>> iterator = pointList.iterator();

        while (iterator.hasNext()){
            Map<String,String> map = iterator.next();
            String cardNo = map.get("cardNo");
            cardNos.add(cardNo);
        }

        for (String cardNo : cardNos){
            PointInfoGetRequest request = new PointInfoGetRequest();
            //CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();

            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setAppSecret(nascentConfig.getBtnAppSerect());
            request.setGroupId(nascentConfig.getBtnGroupID());
            request.setAccessToken(tokenService.getBtnToken());

            request.setIntegralAccount("pcode-216062");
            request.setNasOuid(cardNos.get(0));
            request.setPlatform(0);

            ApiClient client = new ApiClientImpl(request);
            PointInfoGetResponse response = client.execute(request);
            if (response.getSuccess()){
                log.info(response.getBody());
                log.info(cardNo);
            }else {
                log.info(response.getBody());
                log.info(cardNo);
            }

        }

    }
}
