package com.me.nascent.modules.point.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.protobuf.Api;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.MemberTong;
import com.me.nascent.modules.member.entity.PureMemberNickInfo;
import com.me.nascent.modules.member.entity.ZaMemberNickInfo;
import com.me.nascent.modules.member.mapper.PureMemberNickInfoMapper;
import com.me.nascent.modules.member.mapper.ZaMemberNickInfoMapper;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import com.me.nascent.modules.point.entity.PureMemberPoint;
import com.me.nascent.modules.point.entity.ZaOfflineMemberPoint;
import com.me.nascent.modules.point.entity.ZaOnlineMemberPoint;
import com.me.nascent.modules.point.service.PureMemberPointService;
import com.me.nascent.modules.point.service.TransPointService;
import com.me.nascent.modules.point.service.ZaOfflineMemberPointService;
import com.me.nascent.modules.point.service.ZaOnlineMemberPointService;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.BaseNasOuid;
import com.nascent.ecrp.opensdk.domain.customer.NickPlatform;
import com.nascent.ecrp.opensdk.domain.point.CustomerAccountPointInfo;
import com.nascent.ecrp.opensdk.domain.point.CustomerAvailablePointDetailInfo;
import com.nascent.ecrp.opensdk.request.point.CustomerAvailablePointUpdateQueryRequest;
import com.nascent.ecrp.opensdk.request.point.CustomerPointInfoQueryRequest;
import com.nascent.ecrp.opensdk.request.point.PointAddRequest;
import com.nascent.ecrp.opensdk.response.customer.CustomerGradeUpdateResponse;
import com.nascent.ecrp.opensdk.response.point.CustomerAvailablePointUpdateQueryResponse;
import com.nascent.ecrp.opensdk.response.point.CustomerPointInfoQueryResponse;
import com.nascent.ecrp.opensdk.response.point.PointAddResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TransPointServiceImpl implements TransPointService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberNickInfoService pureMemberNickInfoService;

    private PureMemberPointService pureMemberPointService;

    private PureMemberNickInfoMapper pureMemberNickInfoMapper;

    private ZaMemberNickInfoMapper zaMemberNickInfoMapper;

    private ZaOnlineMemberPointService zaOnlineMemberPointService;

    private ZaOfflineMemberPointService zaOfflineMemberPointService;

    @Override
    public void transPurePoint(String integralAccount,Long viewId) throws Exception {

        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount(integralAccount);
        request.setViewId(viewId);

        List<PureMemberNickInfo> pureMemberNickInfos = pureMemberNickInfoMapper.findNotInPointCustomerInfo();


        int batchSize = 100; // 每次处理的数据量
        int totalSize = pureMemberNickInfos.size(); // 总数据量
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
                log.info(response.getBody());
                List<CustomerAccountPointInfo> accountPointInfos = response.getResult();

                for (CustomerAccountPointInfo accountPointInfo  : accountPointInfos){

                    PureMemberPoint memberPoint = new PureMemberPoint();
                    BeanUtils.copyProperties(accountPointInfo,memberPoint);

//                    QueryWrapper<PureMemberPoint> pureMemberPointQuery = new QueryWrapper<>();
//                    pureMemberPointQuery.eq("nasOuid",accountPointInfo.getNasOuid()).eq("platform",accountPointInfo.getPlatform());
//
//                    PureMemberPoint existObj = pureMemberPointService.getOne(pureMemberPointQuery);
//
//                    if(existObj!=null){
//                        UpdateWrapper<PureMemberPoint> pureMemberPointUpdate = new UpdateWrapper<>();
//                        pureMemberPointUpdate.eq("nasOuid",accountPointInfo.getNasOuid()).set("score",accountPointInfo.getScore());
//
//                        //pureMemberPointService.update(pureMemberPointUpdate);
//
//                    }else {
//                        insertMemberPoints.add(memberPoint);
//                    }

                    insertMemberPoints.add(memberPoint);
                }

                if (CollUtil.isNotEmpty(updateMemberPoints)){
                    //pureMemberPointService.saveOrUpdateBatch(updateMemberPoints);
                }

                if (CollUtil.isNotEmpty(insertMemberPoints)){
                    pureMemberPointService.saveBatch(insertMemberPoints);
                }
            }
        }
    }

    @Override
    public void transZaOnlinePoint(String integralAccount, Long viewId) throws Exception {
        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount(integralAccount);
        request.setViewId(viewId);

        List<ZaMemberNickInfo> zaMemberNickInfos = zaMemberNickInfoMapper.findNotInZaOnlineCustomerInfo();

        int batchSize = 100; // 每次处理的数据量
        int totalSize = zaMemberNickInfos.size(); // 总数据量
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {

            List<ZaOnlineMemberPoint> insertMemberPoints = new ArrayList<>();

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<ZaMemberNickInfo> batchList = zaMemberNickInfos.subList(start,end);
            log.info("batchList="+batchList.toString());

            List<NickPlatform> nickPlatforms = new ArrayList<>();

            for (ZaMemberNickInfo obj : batchList){
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
            }else if("200".equals(response.getCode())) {
                log.info(response.getBody());

                List<CustomerAccountPointInfo> accountPointInfos = response.getResult();

                for (CustomerAccountPointInfo accountPointInfo  : accountPointInfos){
                    ZaOnlineMemberPoint zaOnlineMemberPoint = new ZaOnlineMemberPoint();
                    BeanUtils.copyProperties(accountPointInfo,zaOnlineMemberPoint);

                    insertMemberPoints.add(zaOnlineMemberPoint);
                }

                if (CollUtil.isNotEmpty(insertMemberPoints)){
                    zaOnlineMemberPointService.saveBatch(insertMemberPoints);
                }
            }
        }
    }

    @Override
    public void getZaOfflinePoint(String integralAccount, Long viewId) throws Exception {
        CustomerPointInfoQueryRequest request = new CustomerPointInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setIntegralAccount(integralAccount);
        request.setViewId(viewId);

        List<ZaMemberNickInfo> zaMemberNickInfos = zaMemberNickInfoMapper.findNotInZaOffCustomerInfo();

        int batchSize = 100; // 每次处理的数据量
        int totalSize = zaMemberNickInfos.size(); // 总数据量
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            List<ZaOfflineMemberPoint> insertMemberPoints = new ArrayList<>();

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<ZaMemberNickInfo> batchList = zaMemberNickInfos.subList(start,end);
            log.info("batchList="+batchList.toString());

            List<NickPlatform> nickPlatforms = new ArrayList<>();

            for (ZaMemberNickInfo obj : batchList){
                NickPlatform nickPlatform = new NickPlatform();
                BeanUtils.copyProperties(obj,nickPlatform);
                nickPlatforms.add(nickPlatform);

                request.setNickList(nickPlatforms);
                ApiClient client = new ApiClientImpl(request);
                CustomerPointInfoQueryResponse response = client.execute(request);
                log.info(response.getBody());

                if("60001".equals(response.getCode())){
                    UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
                    tokenService.update(updateWrapper);
                }else if("200".equals(response.getCode())) {
                    log.info(response.getBody());

                    List<CustomerAccountPointInfo> accountPointInfos = response.getResult();

                    for (CustomerAccountPointInfo accountPointInfo  : accountPointInfos){
                        ZaOfflineMemberPoint zaOnlineMemberPoint = new ZaOfflineMemberPoint();
                        BeanUtils.copyProperties(accountPointInfo,zaOnlineMemberPoint);

                        insertMemberPoints.add(zaOnlineMemberPoint);
                    }

                    if (CollUtil.isNotEmpty(insertMemberPoints)){
                        zaOfflineMemberPointService.saveBatch(insertMemberPoints);
                    }
                }
            }
        }
    }

    @Override
    public void putPureMemberPoint(String integralAccount,String platform) throws Exception {

        PointAddRequest request = new PointAddRequest();
        if ("pcode-206261".equals(integralAccount)){
            //泊美积分账号
            request.setIntegralAccount("pcode-216174");
        }else if("pcode-206256".equals(integralAccount)){
            //Za 积分账号
            request.setIntegralAccount("pcode-216062");
        }
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());

        QueryWrapper<PureMemberPoint> pureMemberPointQuery = new QueryWrapper<>();
        pureMemberPointQuery.eq("integralAccount",integralAccount).eq("isFinsih","0").eq("platform",platform);
        List<PureMemberPoint> pureMemberPoints = pureMemberPointService.list(pureMemberPointQuery);

        for (PureMemberPoint pureMemberPoint : pureMemberPoints){
            request.setNasOuid(pureMemberPoint.getNasOuid());

            request.setPlatform(pureMemberPoint.getPlatform());
            request.setPoint(pureMemberPoint.getAvailPoint());
            request.setSendType(1);
            request.setRemark("积分初始化");

            if ("pcode-206261".equals(integralAccount) && "19".equals(platform)){
                //泊美会员
                request.setShopId(101092686L);
            }else if("pcode-206261".equals(integralAccount) && "111".equals(platform)){
                //泊美 抖音
                request.setShopId(101130621L);
            }else if("pcode-206261".equals(integralAccount) && "11".equals(platform)){
                //泊美 有赞
                request.setShopId(101130620L);
            }else if("pcode-206261".equals(integralAccount) && "703212".equals(platform)){
                //泊美 淘宝 101130619
                request.setShopId(101130619L);
                request.setPlatform(800007);
            }else if("pcode-206256".equals(integralAccount) && "19".equals(platform)){
                //Za会员
                request.setShopId(101092685L);
            }else if("pcode-206256".equals(integralAccount) && "111".equals(platform)){
                //Za 抖音
                request.setShopId(101130618L);
            }else if("pcode-206256".equals(integralAccount) && "11".equals(platform)){
                //Za 有赞
                request.setShopId(101130617L);
            }else if("pcode-206256".equals(integralAccount) && "703184".equals(platform)){
                //Za 淘系
                request.setShopId(101130616L);
            }

            ApiClient client = new ApiClientImpl(request);
            PointAddResponse response = client.execute(request);

            log.info(response.getBody());

            if ("200".equals(response.getCode())){
                UpdateWrapper<PureMemberPoint> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("nasOuid",pureMemberPoint.getNasOuid())
                        .eq("platform",pureMemberPoint.getPlatform())
                        .eq("integralAccount",integralAccount)
                        .set("isFinsih","1");

                pureMemberPointService.update(updateWrapper);
            }
        }

        /*PureMemberPoint pureMemberPoint = pureMemberPoints.get(0);
        request.setNasOuid(pureMemberPoint.getNasOuid());

        request.setPlatform(pureMemberPoint.getPlatform());
        request.setPoint(pureMemberPoint.getAvailPoint());
        request.setSendType(1);
        request.setRemark("积分初始化");

        if ("pcode-206261".equals(integralAccount) && "19".equals(platform)){
            //泊美会员
            request.setShopId(101092686L);
        }

        ApiClient client = new ApiClientImpl(request);
        PointAddResponse response = client.execute(request);

        log.info(response.getBody());

        if ("200".equals(response.getCode())){
            UpdateWrapper<PureMemberPoint> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("nasOuid",pureMemberPoint.getNasOuid())
                    .eq("platform",pureMemberPoint.getPlatform())
                    .eq("integralAccount",integralAccount)
                    .set("isFinsih","1");

            pureMemberPointService.update(updateWrapper);
        }*/


    }

    @Override
    public void transMemberPoint(Date start,Date end,String integralAccount) throws Exception {

        boolean isNext = true;
        Long nextId = 0L;

        while (isNext){
            Map<String,Object> respMap = savePurePoint(start,end,integralAccount,nextId);
            isNext = (boolean) respMap.get("isNext");
            if(isNext == true){
                nextId = (Long) respMap.get("nextId");
            }
        }

    }

    Map<String,Object> savePurePoint(Date start, Date end, String integralAccount, Long nextId) throws Exception {
        Long oriNextId = nextId;
        Map<String,Object> respMap = new HashMap<>();

        CustomerAvailablePointUpdateQueryRequest request = new CustomerAvailablePointUpdateQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setPageSize(200);
        request.setNextId(nextId);

        request.setIntegralAccount(integralAccount);
        request.setUpdateTimeStart(start);
        request.setUpdateTimeEnd(end);


        ApiClient apiClient = new ApiClientImpl(request);
        CustomerAvailablePointUpdateQueryResponse response = apiClient.execute(request);

        log.info(response.getBody());
        if("200".equals(response.getCode())){
            if (CollUtil.isNotEmpty(response.getResult().getCustomerAvailablePointDetailList())){
                nextId = response.getResult().getNextId();
                log.info(response.getResult().getCustomerAvailablePointDetailList().toString());

                List<CustomerAvailablePointDetailInfo> customerAvailablePointDetailInfos = response.getResult().getCustomerAvailablePointDetailList();
                List<PureMemberPoint> pureMemberPoints = new ArrayList<>();
                for (CustomerAvailablePointDetailInfo customerAvailablePointDetailInfo : customerAvailablePointDetailInfos){
                    BigDecimal availPoint = customerAvailablePointDetailInfo.getAvailPoint();
                    List<BaseNasOuid> baseNasOuids = customerAvailablePointDetailInfo.getNickInfoList();

                    /*for (BaseNasOuid baseNasOuid : baseNasOuids){
                        String nasOuid = baseNasOuid.getNasOuid();
                        Integer platform = baseNasOuid.getPlatform();

                        QueryWrapper<PureMemberPoint> pureMemberPointQuery = new QueryWrapper<>();
                        pureMemberPointQuery.eq("nasOuid",nasOuid).eq("platform",platform).eq("integralAccount",integralAccount);

                        List<PureMemberPoint> exists = pureMemberPointService.list(pureMemberPointQuery);
                        if(CollUtil.isEmpty(exists)){
                            PureMemberPoint pureMemberPoint = new PureMemberPoint();
                            pureMemberPoint.setAvailPoint(availPoint);
                            pureMemberPoint.setNasOuid(nasOuid);
                            pureMemberPoint.setPlatform(platform);
                            pureMemberPoint.setIntegralAccount(integralAccount);
                            pureMemberPoints.add(pureMemberPoint);
                        }
                    }*/

                    if(availPoint.compareTo(BigDecimal.ZERO)>0){
                        BaseNasOuid baseNasOuid = baseNasOuids.get(0);

                        String nasOuid = baseNasOuid.getNasOuid();
                        Integer platform = baseNasOuid.getPlatform();

                        QueryWrapper<PureMemberPoint> pureMemberPointQuery = new QueryWrapper<>();
                        pureMemberPointQuery.eq("nasOuid",nasOuid).eq("platform",platform).eq("integralAccount",integralAccount);

                        List<PureMemberPoint> exists = pureMemberPointService.list(pureMemberPointQuery);
                        if(CollUtil.isEmpty(exists)){
                            PureMemberPoint pureMemberPoint = new PureMemberPoint();
                            pureMemberPoint.setAvailPoint(availPoint);
                            pureMemberPoint.setNasOuid(nasOuid);
                            pureMemberPoint.setPlatform(platform);
                            pureMemberPoint.setIntegralAccount(integralAccount);
                            pureMemberPoints.add(pureMemberPoint);
                        }
                    }
                }

                if(CollUtil.isNotEmpty(pureMemberPoints)){
                    pureMemberPointService.saveBatch(pureMemberPoints);
                }
                log.info("本次查询有数据,执行下页循环");
                respMap.put("isNext",true);
                respMap.put("nextId",nextId);
                return respMap;
            }else {
                log.info("本次查询无数据,执行下次循环");
                respMap.put("isNext",false);
                return respMap;
            }
        }else {
            Thread.sleep(5000);
            log.info("接口执行错误，执行下次循环");
            log.info(response.getBody());
            respMap.put("isNext",true);
            respMap.put("nextId",oriNextId);
            return respMap;
        }

    }


    @Override
    public void testQueryPost() throws Exception {

    }



}
