package com.me.nascent.modules.qimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.order.entity.Trade;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenPutCustomerService;
import com.me.nascent.modules.qimen.service.QiMenTransTradeService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CustomerSaveInfo;
import com.nascent.ecrp.opensdk.request.customer.BatchCustomerSaveRequest;
import com.nascent.ecrp.opensdk.request.customer.CustomerSaveRequest;
import com.nascent.ecrp.opensdk.response.customer.BatchCustomerSaveResponse;
import com.nascent.ecrp.opensdk.response.customer.CustomerSaveResponse;
import com.nascent.ecrp.opensdk.response.customer.SystemCustomerGetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutCustomerServiceImpl implements QiMenPutCustomerService {

    private QiMenCustomerService qiMenCustomerService;

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    @Override
    public void putQiMenCustomer() throws Exception {

        QueryWrapper<QiMenCustomer> qiMenCustomerQuery = new QueryWrapper<>();
        /*泊美官方旗舰店*/
        qiMenCustomerQuery.eq("sellerNick","泊美官方旗舰店");
        List<QiMenCustomer> qiMenCustomers = qiMenCustomerService.list(qiMenCustomerQuery);


        int batchSize = 100; // 每次处理的数据量
        int totalSize = qiMenCustomers.size(); // 总数据量
        //int totalSize = 1;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize);

            List<QiMenCustomer> batchList = qiMenCustomers.subList(start, end);

            BatchCustomerSaveRequest request = new BatchCustomerSaveRequest();
            List<CustomerSaveInfo> customerSaveInfos = new ArrayList<>();
            for (QiMenCustomer qiMenCustomer : batchList) {

                CustomerSaveInfo customerSaveInfo = new CustomerSaveInfo();
                customerSaveInfo.setNasOuid(qiMenCustomer.getOuid());
                customerSaveInfo.setPlatform(1);
                customerSaveInfo.setSubPlatform(1);
                customerSaveInfos.add(customerSaveInfo);
            }
            //System.out.println(customerSaveInfos.size());
            request.setCustomerSaveList(customerSaveInfos);
            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setAppSecret(nascentConfig.getBtnAppSerect());
            request.setGroupId(nascentConfig.getBtnGroupID());
            request.setAccessToken(tokenService.getBtnToken());
            //泊美官方旗舰店 101130619 za姬芮官方旗舰店 101130616
            request.setShopId(101130619L);

            ApiClient client = new ApiClientImpl(request);

            BatchCustomerSaveResponse response = client.execute(request);

            if("200".equals(response.getCode())){
                log.info(response.getMsg());
            }else {
                log.info(response.getMsg());
            }
        }
    }
}
