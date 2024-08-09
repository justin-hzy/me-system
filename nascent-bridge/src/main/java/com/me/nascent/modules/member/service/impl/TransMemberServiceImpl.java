package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.CardReceiveInfo;
import com.me.nascent.modules.member.entity.FansStatus;
import com.me.nascent.modules.member.entity.Member;
import com.me.nascent.modules.member.entity.MemberNickInfo;
import com.me.nascent.modules.member.service.*;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CustomerCardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.customer.SystemCustomerInfo;
import com.nascent.ecrp.opensdk.domain.customer.ThirdPartyInviterInfo;
import com.nascent.ecrp.opensdk.domain.customer.wxFansStatus.BaseWxFansStatusVo;
import com.nascent.ecrp.opensdk.request.customer.ActivateCustomerListSyncRequest;
import com.nascent.ecrp.opensdk.response.customer.ActivateCustomerListSyncResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransMemberServiceImpl implements TransMemberService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private MemberService memberService;

    private CardReceiveInfoService cardReceiveInfoService;

    private FansStatusService fansStatusService;

    private MemberNickInfoService memberNickInfoService;

    @Override
    public void TransMemberByRange(Date startDate,Date endDate) throws Exception {
        Long nextId = 0L;
        boolean flag = true;

        while(flag){
            Map resMap =  saveMember(nextId,startDate,endDate);
            startDate = (Date) resMap.get("updateTime");
            nextId = (Long) resMap.get("nextId");
            flag = (boolean) resMap.get("isNext");
            log.info("startDate="+startDate);
            log.info("nextId="+nextId);
            log.info("flag="+flag);
        }


    }

    private Map<String, Object> saveMember(Long nextId,Date startDate,Date endDate) throws Exception {
        Map resMap = new HashMap();
        ActivateCustomerListSyncRequest request = new ActivateCustomerListSyncRequest();

        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());

        List<Token> tokens = tokenService.list();
        request.setAccessToken(tokens.get(0).getToken());
        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setActivate(true);
        request.setNextId(nextId);
        request.setPageSize(50);
        //100000387 泊美  100000386	Za姬芮 80000078 集团会员
        request.setViewId(100000387L);
        //request.setShopId(3411331L);
        ApiClient client = new ApiClientImpl(request);
        ActivateCustomerListSyncResponse response = client.execute(request);

        if("60001".equals(response.getCode())){
            UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
            tokenService.update(updateWrapper);
            resMap.put("nextId",nextId);
            resMap.put("updateTime",startDate);
            resMap.put("isNext",true);
            return resMap;
        }else {
            //log.info(response.getBody());

            List<SystemCustomerInfo> systemCustomerInfos = response.getResult();

            List<Member> insertMembers = new ArrayList<>();

            List<Member> updateMembers = new ArrayList<>();

            Boolean isNext = false;
            Date updateTime = null;

            if (CollUtil.isNotEmpty(systemCustomerInfos)){

                isNext = true;

                for (SystemCustomerInfo systemCustomerInfo : systemCustomerInfos){
                    Long id = systemCustomerInfo.getId();
                    nextId = id;
                    updateTime = systemCustomerInfo.getUpdateTime();
                    QueryWrapper<Member> memberQuery = new QueryWrapper<>();
                    memberQuery.eq("id",id);
                    Member existMember = memberService.getOne(memberQuery);

                    Member member = new Member();
                    BeanUtils.copyProperties(systemCustomerInfo,member);

                    if(existMember != null){
                        updateMembers.add(member);
                    }else {
                        insertMembers.add(member);
                    }

                    List<CustomerCardReceiveInfo> customerCardReceiveInfos = systemCustomerInfo.getCardReceiveInfoList();

                    List<CardReceiveInfo> insertCardReceiveInfos = new ArrayList<>();

                    if(CollUtil.isNotEmpty(customerCardReceiveInfos)){

                        QueryWrapper<CardReceiveInfo> queryWrapper = new QueryWrapper();
                        queryWrapper.eq("mainid",id);

                        List<CardReceiveInfo> existInfo = cardReceiveInfoService.list(queryWrapper);
                        if(CollUtil.isNotEmpty(existInfo)){
                            cardReceiveInfoService.remove(queryWrapper);
                        }

                        for (CustomerCardReceiveInfo customerCardReceiveInfo : customerCardReceiveInfos){
                            CardReceiveInfo cardReceiveInfo = new CardReceiveInfo();
                            BeanUtils.copyProperties(customerCardReceiveInfo,cardReceiveInfo);
                            cardReceiveInfo.setMainId(id);
                            insertCardReceiveInfos.add(cardReceiveInfo);
                        }

                        cardReceiveInfoService.saveBatch(insertCardReceiveInfos);
                    }

                    List<BaseWxFansStatusVo> baseWxFansStatusVos = systemCustomerInfo.getFansStatusVos();

                    if(CollUtil.isNotEmpty(baseWxFansStatusVos)){

                        QueryWrapper<FansStatus> fansStatusQuery = new QueryWrapper<>();
                        fansStatusQuery.eq("mainid",id);
                        List<FansStatus> existInfo = fansStatusService.list(fansStatusQuery);

                        if(CollUtil.isNotEmpty(existInfo)){
                            fansStatusService.remove(fansStatusQuery);
                        }
                        List<FansStatus> insertFansStatusList = new ArrayList<>();
                        for (BaseWxFansStatusVo baseWxFansStatusVo : baseWxFansStatusVos){
                            FansStatus fansStatus = new FansStatus();
                            BeanUtils.copyProperties(baseWxFansStatusVo,fansStatus);
                            fansStatus.setMainId(id);
                        }

                        fansStatusService.saveBatch(insertFansStatusList);
                    }



                    List<NickInfo> nickInfos = systemCustomerInfo.getNickInfoList();
                    if (CollUtil.isNotEmpty(nickInfos)){
                        QueryWrapper<MemberNickInfo> memberNickInfoQuery = new QueryWrapper<>();
                        memberNickInfoQuery.eq("mainid",id);
                        List<MemberNickInfo> existInfo = memberNickInfoService.list(memberNickInfoQuery);
                        if(CollUtil.isNotEmpty(existInfo)){
                            memberNickInfoService.remove(memberNickInfoQuery);
                        }

                        List<MemberNickInfo> memberNickInfos = new ArrayList<>();

                        for (NickInfo nickInfo : nickInfos){
                            MemberNickInfo memberNickInfo = new MemberNickInfo();
                            BeanUtils.copyProperties(nickInfo,memberNickInfo);
                            memberNickInfo.setMainId(id);
                            memberNickInfos.add(memberNickInfo);
                        }

                        if(memberNickInfos.size()>0){
                            memberNickInfoService.saveBatch(memberNickInfos);
                        }
                    }
                }



                if(insertMembers.size()>0){
                    memberService.saveBatch(insertMembers);
                }

                if(updateMembers.size()>0){
                    memberService.saveOrUpdateBatch(updateMembers);
                }
            }

            resMap.put("nextId",nextId);
            resMap.put("updateTime",updateTime);
            resMap.put("isNext",isNext);
            return resMap;
        }

    }
}
