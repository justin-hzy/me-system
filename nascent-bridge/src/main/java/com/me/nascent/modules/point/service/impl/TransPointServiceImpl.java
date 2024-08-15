package com.me.nascent.modules.point.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.point.service.PureMemberPointService;
import com.me.nascent.modules.point.service.TransPointService;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickPlatform;
import com.nascent.ecrp.opensdk.domain.point.CustomerAccountPointInfo;
import com.nascent.ecrp.opensdk.request.point.CustomerPointInfoQueryRequest;
import com.nascent.ecrp.opensdk.request.point.PointAddRequest;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.point.PointAddResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TransPointServiceImpl implements TransPointService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberNickInfoService pureMemberNickInfoService;

    private PureMemberPointService pureMemberPointService;

    @Override
    public void transPoint(String integralAccount,Long viewId) throws Exception {

        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount(integralAccount);
        request.setViewId(viewId);

        List<PureMemberNickInfo> pureMemberNickInfos = pureMemberNickInfoService.list();


        int batchSize = 100; // 每次处理的数据量
        //int totalSize = memberNickInfos.size(); // 总数据量
        int totalSize = 200;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            List<PureMemberPoint> insertMemberPoints = new ArrayList<>();
            List<PureMemberPoint> updateMemberPoints = new ArrayList<>();
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<PureMemberNickInfo> batchList = pureMemberNickInfos.subList(start,end);
            log.info("batchList="+batchList.toString());

            List<NickPlatform> nickPlatforms = new ArrayList<>();

            for (PureMemberNickInfo obj : batchList){
                NickPlatform nickPlatform = new NickPlatform();
                BeanUtils.copyProperties(obj,nickPlatform);
                nickPlatforms.add(nickPlatform);
            }
            request.setNickList(nickPlatforms);

            ApiClient client = new ApiClientImpl(request);
            CustomerPointInfoQueryResponse response = client.execute(request);
            log.info(response.getBody());

            if("60001".equals(response.getCode())){
                UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
                tokenService.update(updateWrapper);
            }else if("200".equals(response.getCode())){
                log.info(response.getRequestId());
                List<CustomerAccountPointInfo> accountPointInfos = response.getResult();

                for (CustomerAccountPointInfo accountPointInfo  : accountPointInfos){

                    QueryWrapper<PureMemberPoint> pureMemberPointQuery = new QueryWrapper<>();
                    pureMemberPointQuery.eq("nasOuid",accountPointInfo.getNasOuid()).eq("platform",accountPointInfo.getPlatform());

                    PureMemberPoint existObj = pureMemberPointService.getOne(pureMemberPointQuery);

                    PureMemberPoint memberPoint = new PureMemberPoint();
                    BeanUtils.copyProperties(accountPointInfo,memberPoint);


                    if(existObj!=null){
                        UpdateWrapper<PureMemberPoint> pureMemberPointUpdate = new UpdateWrapper<>();
                        pureMemberPointUpdate.eq("nasOuid",accountPointInfo.getNasOuid()).set("score",accountPointInfo.getScore());

                        //pureMemberPointService.update(pureMemberPointUpdate);

                    }else {
                        insertMemberPoints.add(memberPoint);
                    }
                }

                if (CollUtil.isNotEmpty(updateMemberPoints)){
                    //pureMemberPointService.saveOrUpdateBatch(updateMemberPoints);
                }

                if (CollUtil.isNotEmpty(insertMemberPoints)){
                    //pureMemberPointService.saveBatch(insertMemberPoints);
                }
            }
        }
    }

    @Override
    public void putPureMemberPoint() throws Exception {

        PointAddRequest request = new PointAddRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        List<PureMemberPoint> pureMemberPoints = pureMemberPointService.list();

        /*int batchSize = 100; // 每次处理的数据量
        int totalSize = pureMemberPoints.size(); // 总数据量

        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {


        }*/

        for (PureMemberPoint pureMemberPoint : pureMemberPoints){
            request.setNasOuid(pureMemberPoint.getNasOuid());
            request.setPlatform(pureMemberPoint.getPlatform());
            request.setIntegralAccount(pureMemberPoint.getIntegralAccount());
            request.setPoint(BigDecimal.valueOf(pureMemberPoint.getScore()));
            request.setSendType(1);
            request.setRemark("悦江积分初始化");

            ApiClient client = new ApiClientImpl(request);
            PointAddResponse response = client.execute(request);
        }

    }
}
