package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.CardReceiveInfo;
import com.me.nascent.modules.member.entity.FansStatus;
import com.me.nascent.modules.member.entity.Member;
import com.me.nascent.modules.member.service.CardReceiveInfoService;
import com.me.nascent.modules.member.service.FansStatusService;
import com.me.nascent.modules.member.service.MemberService;
import com.me.nascent.modules.member.service.TransMemberService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CustomerCardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.SystemCustomerInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransMemberServiceImpl implements TransMemberService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private MemberService memberService;

    private CardReceiveInfoService cardReceiveInfoService;

    private FansStatusService fansStatusService;

    @Override
    public void TransMember() throws Exception {
        ActivateCustomerListSyncRequest request = new ActivateCustomerListSyncRequest();

        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());

        request.setAccessToken(tokenService.getToken());
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse("2024-07-31 00:00:00", formatter);
        // 转换为 Date
        Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime endDateTime = LocalDateTime.parse("2024-08-02 23:59:59", formatter);
        Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setActivate(true);
        request.setNextId(0L);
        request.setPageSize(50);
        request.setViewId(0L);
        //request.setShopId(3411331L);
        ApiClient client = new ApiClientImpl(request);
        ActivateCustomerListSyncResponse response = client.execute(request);
        log.info(response.getBody());

        List<SystemCustomerInfo> systemCustomerInfos = response.getResult();

        List<Member> insertMembers = new ArrayList<>();

        List<Member> updateMembers = new ArrayList<>();


        if (CollUtil.isNotEmpty(systemCustomerInfos)){

            for (SystemCustomerInfo systemCustomerInfo : systemCustomerInfos){
                Long id = systemCustomerInfo.getId();
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
            }



            if(insertMembers.size()>0){
                memberService.saveBatch(insertMembers);
            }

            if(updateMembers.size()>0){
                memberService.saveOrUpdateBatch(updateMembers);
            }
        }
    }
}
