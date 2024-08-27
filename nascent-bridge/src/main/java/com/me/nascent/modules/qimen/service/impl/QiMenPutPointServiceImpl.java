package com.me.nascent.modules.qimen.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenPutPointService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.point.PointAddRequest;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.point.PointAddResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class QiMenPutPointServiceImpl implements QiMenPutPointService {

    private NascentConfig nascentConfig;

    private QiMenCustomerService qiMenCustomerService;

    @Override
    public void putQiMenPoint() throws Exception {
        List<QiMenCustomer> qiMenCustomers = qiMenCustomerService.list();
        for (QiMenCustomer qiMenCustomer : qiMenCustomers){
            PointAddRequest request = new PointAddRequest();
            request.setServerUrl(nascentConfig.getServerUrl());
            request.setAppKey(nascentConfig.getAppKey());
            request.setAppSecret(nascentConfig.getAppSerect());
            request.setGroupId(nascentConfig.getGroupID());
            //积分账号,平台ID 待补充
            request.setIntegralAccount("");
            request.setPlatform(7);
            BigDecimal bigDecimal = new BigDecimal(qiMenCustomer.getPoint());
            request.setPoint(bigDecimal);
            request.setSendType(1);
            request.setRemark("奇门会员积分初始化");

            ApiClient client = new ApiClientImpl(request);
            PointAddResponse response = client.execute(request);
        }
    }
}
