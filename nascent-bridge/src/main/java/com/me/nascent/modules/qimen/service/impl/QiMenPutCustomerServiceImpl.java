package com.me.nascent.modules.qimen.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenPutCustomerService;
import com.me.nascent.modules.qimen.service.QiMenTransTradeService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.request.customer.CustomerSaveRequest;
import com.nascent.ecrp.opensdk.response.customer.CustomerSaveResponse;
import com.nascent.ecrp.opensdk.response.customer.SystemCustomerGetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutCustomerServiceImpl implements QiMenPutCustomerService {

    private QiMenCustomerService qiMenCustomerService;

    private NascentConfig nascentConfig;

    @Override
    public void putQiMenCustomer() throws Exception {

        List<QiMenCustomer> qiMenCustomers = qiMenCustomerService.list();

        for (QiMenCustomer qiMenCustomer : qiMenCustomers){
            String nasOuid = qiMenCustomer.getNasOuid();
            if(nasOuid == null){
                nasOuid = qiMenCustomer.getOuid();
            }

            CustomerSaveRequest request = new CustomerSaveRequest();
            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setAppSecret(nascentConfig.getBtnAppSerect());
            request.setGroupId(nascentConfig.getGroupID());
            request.setNasOuid(nasOuid);
            //平台号待完善
            request.setPlatform(7);

            ApiClient client = new ApiClientImpl(request);

            CustomerSaveResponse response = client.execute(request);
        }

    }
}
