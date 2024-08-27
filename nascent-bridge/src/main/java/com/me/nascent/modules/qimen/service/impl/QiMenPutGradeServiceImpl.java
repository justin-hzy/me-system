package com.me.nascent.modules.qimen.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.entity.GradeCustomerInfo;
import com.me.nascent.modules.qimen.entity.QiMenCustomer;
import com.me.nascent.modules.qimen.service.QiMenCustomerService;
import com.me.nascent.modules.qimen.service.QiMenPutGradeService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CustomerGradeUpdateInfo;
import com.nascent.ecrp.opensdk.request.customer.CustomerGradeUpdateRequest;
import com.nascent.ecrp.opensdk.response.customer.CustomerGradeUpdateResponse;
import com.nascent.ecrp.opensdk.response.customer.CustomerSaveResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutGradeServiceImpl implements QiMenPutGradeService {

    private QiMenCustomerService qiMenCustomerService;

    private NascentConfig nascentConfig;

    @Override
    public void putQiMenGrade() throws Exception {
        List<QiMenCustomer> qiMenCustomers = qiMenCustomerService.list();

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = qiMenCustomers.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<QiMenCustomer> batchList = qiMenCustomers.subList(start, end);
            log.info("batchList=" + batchList.toString());

            CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();

            for (QiMenCustomer qiMenCustomer : batchList){
                CustomerGradeUpdateInfo customerGradeUpdateInfo = new CustomerGradeUpdateInfo();
                customerGradeUpdateInfo.setGrade(Integer.valueOf(qiMenCustomer.getLevel()));
                String nasOuid = qiMenCustomer.getNasOuid();
                if(nasOuid != null){
                    customerGradeUpdateInfo.setNasOuid(nasOuid);
                }else {
                    customerGradeUpdateInfo.setNasOuid(qiMenCustomer.getOuid());
                }
                customerGradeUpdateInfoList.add(customerGradeUpdateInfo);
            }

            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);
            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setAppSecret(nascentConfig.getBtnAppSerect());
            request.setGroupId(nascentConfig.getGroupID());
            // 待处理
            /*request.setViewId();
            request.setPlatform();*/

            ApiClient client = new ApiClientImpl(request);

            CustomerGradeUpdateResponse response = client.execute(request);
        }
    }
}
