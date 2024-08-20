package com.me.nascent.modules.grade.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.entity.GradeCustomerCardReceiveInfo;
import com.me.nascent.modules.grade.entity.GradeCustomerInfo;
import com.me.nascent.modules.grade.entity.GradeFansStatusVo;
import com.me.nascent.modules.grade.service.GradeCustomerCardReceiveInfoService;
import com.me.nascent.modules.grade.service.GradeCustomerInfoService;
import com.me.nascent.modules.grade.service.GradeFansStatusVoService;
import com.me.nascent.modules.grade.service.TransGradeService;

import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.entity.ZaMemberNickInfo;
import com.me.nascent.modules.member.service.ZaMemberNickInfoService;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;

import com.nascent.ecrp.opensdk.domain.customer.CustomerCardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.CustomerGradeUpdateInfo;
import com.nascent.ecrp.opensdk.domain.customer.systemCustomer.SystemCustomerInfo;
import com.nascent.ecrp.opensdk.domain.customer.wxFansStatus.BaseWxFansStatusVo;
import com.nascent.ecrp.opensdk.request.customer.CustomerGradeUpdateRequest;
import com.nascent.ecrp.opensdk.request.customer.SystemCustomerGetRequest;
import com.nascent.ecrp.opensdk.response.customer.CustomerGradeUpdateResponse;
import com.nascent.ecrp.opensdk.response.customer.SystemCustomerGetResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class TransGradeServiceImpl implements TransGradeService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private ZaMemberNickInfoService zaMemberNickInfoService;

    private GradeCustomerInfoService gradeCustomerInfoService;

    private GradeCustomerCardReceiveInfoService gradeCustomerCardReceiveInfoService;

    private GradeFansStatusVoService gradeFansStatusVoService;


    @Override
    public void TransZaGrade(Long viewId) throws Exception {

        SystemCustomerGetRequest request = new SystemCustomerGetRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        request.setViewId(viewId);

        List<ZaMemberNickInfo> zaMemberNickInfos = zaMemberNickInfoService.list();



        /*int size = zaMemberNickInfos.size(); // 总数据量


        List<GradeCustomerInfo> list = new ArrayList<>();

        for (int i = 0; i <= size; i++) {

            ZaMemberNickInfo zaMemberNickInfo = zaMemberNickInfos.get(i);

            String nasOuid = zaMemberNickInfo.getNasOuid();
            int platform = zaMemberNickInfo.getPlatform();

            request.setNasOuid(nasOuid);
            request.setPlatform(platform);

            ApiClient client = new ApiClientImpl(request);

            SystemCustomerGetResponse response = client.execute(request);



            if("60001".equals(response.getCode())){
                UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
                tokenService.update(updateWrapper);
            }else if("200".equals(response.getCode())) {
                log.info(response.getBody());
                SystemCustomerInfo systemCustomerInfo = response.getResult();
                if (systemCustomerInfo != null){
                    GradeCustomerInfo gradeCustomerInfo = new GradeCustomerInfo();

                    BeanUtils.copyProperties(systemCustomerInfo,gradeCustomerInfo);
                    gradeCustomerInfo.setNasOuid(zaMemberNickInfo.getNasOuid());


                    QueryWrapper<GradeCustomerInfo> gradeCustomerInfoQuery = new QueryWrapper();
                    gradeCustomerInfoQuery.eq("nasOuid",nasOuid);
                    GradeCustomerInfo existObj = gradeCustomerInfoService.getOne(gradeCustomerInfoQuery);
                    if (existObj != null){
                        UpdateWrapper<GradeCustomerInfo> gradeCustomerInfoUpdate = new UpdateWrapper<>();
                        gradeCustomerInfoUpdate.eq("nasOuid",nasOuid);
                        gradeCustomerInfoService.update(gradeCustomerInfo,gradeCustomerInfoUpdate);
                    }else {
                        list.add(gradeCustomerInfo);
                        //gradeCustomerInfoService.save(gradeCustomerInfo);
                    }
                }
            }

            if(list.size() == 10000 || i==size){
                gradeCustomerInfoService.saveBatch(list);
                list.clear();
            }
        }*/

        for (ZaMemberNickInfo zaMemberNickInfo : zaMemberNickInfos){

            String nasOuid = zaMemberNickInfo.getNasOuid();
            int platform = zaMemberNickInfo.getPlatform();

            request.setNasOuid(nasOuid);
            request.setPlatform(platform);

            ApiClient client = new ApiClientImpl(request);

            SystemCustomerGetResponse response = client.execute(request);



            if("60001".equals(response.getCode())){
                UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
                tokenService.update(updateWrapper);
            }else if("200".equals(response.getCode())) {
                log.info(response.getBody());
                SystemCustomerInfo systemCustomerInfo = response.getResult();
                if (systemCustomerInfo != null){
                    GradeCustomerInfo gradeCustomerInfo = new GradeCustomerInfo();

                    BeanUtils.copyProperties(systemCustomerInfo,gradeCustomerInfo);
                    gradeCustomerInfo.setNasOuid(zaMemberNickInfo.getNasOuid());


                    QueryWrapper<GradeCustomerInfo> gradeCustomerInfoQuery = new QueryWrapper();
                    gradeCustomerInfoQuery.eq("nasOuid",nasOuid);
                    GradeCustomerInfo existObj = gradeCustomerInfoService.getOne(gradeCustomerInfoQuery);
                    if (existObj != null){
                        UpdateWrapper<GradeCustomerInfo> gradeCustomerInfoUpdate = new UpdateWrapper<>();
                        gradeCustomerInfoUpdate.eq("nasOuid",nasOuid);
                        gradeCustomerInfoService.update(gradeCustomerInfo,gradeCustomerInfoUpdate);
                    }else {
                        gradeCustomerInfoService.save(gradeCustomerInfo);
                    }
                }
            }
        }
    }

    @Override
    public void putGrade() throws Exception {

        List<GradeCustomerInfo> gradeCustomerInfos = gradeCustomerInfoService.list();
        CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());




        List<GradeCustomerInfo> list = new ArrayList<>();

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = gradeCustomerInfos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<GradeCustomerInfo> batchList = gradeCustomerInfos.subList(start, end);
            log.info("batchList=" + batchList.toString());

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();
            for (GradeCustomerInfo gradeCustomerInfo : batchList){
                CustomerGradeUpdateInfo gradeUpdateInfo = new CustomerGradeUpdateInfo();
                BeanUtils.copyProperties(gradeCustomerInfo,gradeUpdateInfo);

                customerGradeUpdateInfoList.add(gradeUpdateInfo);
            }
            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);

            ApiClient apiClient =new ApiClientImpl(request);
            CustomerGradeUpdateResponse response = apiClient.execute(request);
        }
    }
}
