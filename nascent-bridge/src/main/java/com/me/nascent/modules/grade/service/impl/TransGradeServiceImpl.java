package com.me.nascent.modules.grade.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.entity.*;
import com.me.nascent.modules.grade.service.*;

import com.me.nascent.modules.member.entity.MemberTong;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.entity.ZaMemberNickInfo;
import com.me.nascent.modules.member.mapper.PureMemberNickInfoMapper;
import com.me.nascent.modules.member.service.MemberTongService;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import com.me.nascent.modules.member.service.ZaMemberNickInfoService;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;

import com.nascent.ecrp.opensdk.domain.customer.CustomerCardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.CustomerGradeUpdateInfo;
import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.customer.NickPlatform;
import com.nascent.ecrp.opensdk.domain.customer.wxFansStatus.BaseWxFansStatusVo;
import com.nascent.ecrp.opensdk.request.customer.CustomerGradeUpdateRequest;
import com.nascent.ecrp.opensdk.request.customer.CustomerInfoQueryRequest;
import com.nascent.ecrp.opensdk.request.customer.SystemCustomerGetRequest;
import com.nascent.ecrp.opensdk.response.customer.CustomerGradeUpdateResponse;
import com.nascent.ecrp.opensdk.response.customer.CustomerInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.customer.SystemCustomerGetResponse;
import com.nascent.ecrp.opensdk.response.customer.customerInfoQuery.SystemCustomerInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public class TransGradeServiceImpl implements TransGradeService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberNickInfoService pureMemberNickInfoService;

    private GradeCustomerInfoService gradeCustomerInfoService;

    private MemberTongService memberTongService;

    private GradeMemberInfoService gradeMemberInfoService;

    @Override
    public void putMemberTongGrade() throws Exception {

        CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());

        QueryWrapper<MemberTong> memberTongQuery = new QueryWrapper<>();

        memberTongQuery.ne("bindStatus",-1)
                .isNotNull("ouid")
                .eq("sellerNick","泊美官方旗舰店");

        List<MemberTong> memberTongs = memberTongService.list(memberTongQuery);

        int batchSize = 1; // 每次处理的数据量
        int totalSize  = 1; // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<MemberTong> batchList = memberTongs.subList(start, end);

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();
            for (MemberTong memberTong : batchList){
                String extendObj = memberTong.getExtendObj();
                JSONObject extendJson = JSONObject.parseObject(extendObj);
                String level = extendJson.getString("level");
                if (StrUtil.isEmpty(level)){
                    level = "0";
                }

                CustomerGradeUpdateInfo customerGradeUpdateInfo = new CustomerGradeUpdateInfo();
                customerGradeUpdateInfo.setNasOuid(memberTong.getOuid());
                customerGradeUpdateInfo.setGrade(Integer.valueOf(level));
                customerGradeUpdateInfoList.add(customerGradeUpdateInfo);

            }


            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);
            request.setPlatform(1);
            //泊美 101130619L Za 101130616L
            request.setShopId(101130619L);


            ApiClient apiClient =new ApiClientImpl(request);
            CustomerGradeUpdateResponse response = apiClient.execute(request);

            log.info(response.getBody());

        }
    }

    @Override
    public void transMemberTongGrade(Long viewId) throws Exception {

        CustomerInfoQueryRequest request = new CustomerInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        QueryWrapper<MemberTong> memberTongQuery = new QueryWrapper<>();

        memberTongQuery.ne("bindStatus",-1)
                .isNotNull("ouid")
                .eq("sellerNick","泊美官方旗舰店");

        List<MemberTong> memberTongs = memberTongService.list(memberTongQuery);


        int batchSize = 100; // 每次处理的数据量
        int totalSize  = memberTongs.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<MemberTong> batchList = memberTongs.subList(start, end);

            List<NickPlatform> nickList = new ArrayList<>();
            for (MemberTong memberTong : batchList) {
                String ouid = memberTong.getOuid();

                NickPlatform nickPlatform = new NickPlatform();
                nickPlatform.setNasOuid(ouid);
                nickPlatform.setPlatform(1);
                nickList.add(nickPlatform);
            }
            request.setNickList(nickList);
            //100149662 泊美 100149660 Za
            request.setShopId(100149662L);
            request.setViewId(viewId);
            ApiClient apiClient =new ApiClientImpl(request);

            CustomerInfoQueryResponse response = apiClient.execute(request);
            if("200".equals(response.getCode())){
                List<SystemCustomerInfo> systemCustomerInfos = response.getResult();
                List<GradeMemberInfo> gradeMemberInfos = new ArrayList<>();
                for (SystemCustomerInfo systemCustomerInfo : systemCustomerInfos){
                    Integer grade = systemCustomerInfo.getGrade();
                    String gradeName = systemCustomerInfo.getGradeName();
                    Date updateTime = systemCustomerInfo.getUpdateTime();


                    Long shopId = systemCustomerInfo.getShopId();
                    Long customerId = systemCustomerInfo.getCustomerId();

                    List<NickInfo> nickInfoList = systemCustomerInfo.getNickInfoList();
                    for (NickInfo nick : nickInfoList){
                        String nasOuid = nick.getNasOuid();
                        Integer platform = systemCustomerInfo.getPlatform();

                        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();
                        gradeMemberInfoQuery.eq("nasOuid",nasOuid);
                        gradeMemberInfoQuery.eq("platform",platform);
                        List<GradeMemberInfo> exist = gradeMemberInfoService.list(gradeMemberInfoQuery);

                        if (CollUtil.isEmpty(exist)){
                            GradeMemberInfo gradeMemberInfo = new GradeMemberInfo();
                            gradeMemberInfo.setCustomerId(customerId);
                            gradeMemberInfo.setGradeName(gradeName);
                            gradeMemberInfo.setGrade(grade);
                            gradeMemberInfo.setPlatform(platform);
                            gradeMemberInfo.setNasOuid(nasOuid);
                            gradeMemberInfo.setShopId(shopId);
                            gradeMemberInfo.setUpdateTime(updateTime);
                            gradeMemberInfos.add(gradeMemberInfo);
                        }
                    }
                }
                gradeMemberInfoService.saveBatch(gradeMemberInfos);
            }
        }

    }

    @Override
    public void transShopCustomerGrade() throws Exception {

    }


    @Override
    public void putPureGrade() throws Exception {

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



    @Override
    public void checkPureGrade() {
        QueryWrapper<PureMemberNickInfo> pureMemberNickInfoQuery = new QueryWrapper<>();
        pureMemberNickInfoQuery.eq("isTransGrade",1);
        List<PureMemberNickInfo> list = pureMemberNickInfoService.list(pureMemberNickInfoQuery);
        log.info(list.size()+"");
        List<Map<String,String>> list1 = new ArrayList<>();
        for (PureMemberNickInfo pureMemberNickInfo : list){
            String nasOuid = pureMemberNickInfo.getNasOuid();
            Integer platform = pureMemberNickInfo.getPlatform();

            QueryWrapper<GradeCustomerInfo> gradeCustomerInfoQuery = new QueryWrapper<>();
            gradeCustomerInfoQuery.eq("nasOuid",nasOuid);
            gradeCustomerInfoQuery.eq("platform",platform);

            GradeCustomerInfo gradeCustomerInfo = gradeCustomerInfoService.getOne(gradeCustomerInfoQuery);
            if(gradeCustomerInfo == null){
                Map<String,String> map = new HashMap();
                map.put("nasOuid",nasOuid);
                map.put("platform",String.valueOf(platform));
                list1.add(map);
            }
        }

        log.info(list1.toString());

    }
}
