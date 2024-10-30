package com.me.nascent.modules.grade.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.grade.entity.*;
import com.me.nascent.modules.grade.service.*;

import com.me.nascent.modules.member.entity.*;
import com.me.nascent.modules.member.mapper.PureMemberNickInfoMapper;
import com.me.nascent.modules.member.mapper.ShopActiveCustomerMapper;
import com.me.nascent.modules.member.service.MemberTongService;
import com.me.nascent.modules.member.service.PureMemberNickInfoService;
import com.me.nascent.modules.member.service.TransBtnCustomerFailService;
import com.me.nascent.modules.member.service.ZaMemberNickInfoService;
import com.me.nascent.modules.member.vo.QueryMemberVo;
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

import java.text.SimpleDateFormat;
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

    private ShopActiveCustomerMapper shopActiveCustomerMapper;

    private TransBtnCustomerFailService transBtnCustomerFailService;

    @Override
    public void putMemberTongGrade(Long shopId) throws Exception {

        CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());

        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

        //za会员专用条件，zA数据大使用时间分割
        Date startDate = sdf.parse("2024-07-01 00:00:00");
        Date endDate = sdf.parse("2024-12-31 23:59:59");


        gradeMemberInfoQuery.eq("shopId",shopId).between("updateTime",startDate,endDate);

        List<GradeMemberInfo> gradeMemberInfos = gradeMemberInfoService.list(gradeMemberInfoQuery);

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = gradeMemberInfos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<GradeMemberInfo> batchList = gradeMemberInfos.subList(start, end);

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();
            for (GradeMemberInfo gradeMemberInfo : batchList){

                CustomerGradeUpdateInfo customerGradeUpdateInfo = new CustomerGradeUpdateInfo();
                customerGradeUpdateInfo.setNasOuid(gradeMemberInfo.getNasOuid());
                customerGradeUpdateInfo.setGrade(gradeMemberInfo.getGrade());
                customerGradeUpdateInfoList.add(customerGradeUpdateInfo);

            }


            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);
            request.setPlatform(1);
            //泊美 101130619L Za 101130616L
            request.setShopId(101130616L);


            ApiClient apiClient =new ApiClientImpl(request);
            CustomerGradeUpdateResponse response = apiClient.execute(request);

            if ("200".equals(response.getCode())){
                log.info(response.getMsg());
            }else {
                log.info(response.getBody());
            }
            log.info("执行下次循环");
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
                //.eq("sellerNick","泊美官方旗舰店");
                .eq("sellerNick","za姬芮官方旗舰店");

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
            request.setShopId(100149660L);
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
    public void transShopActiveCustomerGrade(Long viewId) throws Exception {

        CustomerInfoQueryRequest request = new CustomerInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        List<Long> shopIds = new ArrayList<>();
        //抖店 泊美 平台id 111 改执行sql
        shopIds.add(100149663L);
        //有赞 泊美 平台 id 11
        //shopIds.add(100150166L);
        //会员中心 平台id 19
        //泊美 会员
        //shopIds.add(100156928L);
        //Za 会员
        //shopIds.add(100150083L);
        //抖店Za 100149661 平台id 111 改执行sql
        //shopIds.add(100149661L);
        //有赞Za 100150165 平台 id 11
        //shopIds.add(100150165L);


        List<QueryMemberVo> queryMemberVos = shopActiveCustomerMapper.queryMemberVos1(shopIds);

        int batchSize = 100; // 每次处理的数据量
        //int totalSize  = 1; // 总数据量;
        int totalSize  = queryMemberVos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<QueryMemberVo> batchList = queryMemberVos.subList(start, end);

            List<NickPlatform> nickList = new ArrayList<>();

            for (QueryMemberVo queryMemberVo : batchList) {
                String nasOuid = queryMemberVo.getNasOuid();

                NickPlatform nickPlatform = new NickPlatform();
                nickPlatform.setNasOuid(nasOuid);
                nickPlatform.setPlatform(queryMemberVo.getPlatform());
                nickList.add(nickPlatform);
            }
            request.setNickList(nickList);

            //获取上面的店铺id
            request.setShopId(shopIds.get(0));
            //request.setViewId(viewId);
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

                    if(customerId.equals(2737309547L)){
                        log.info("111111");
                    }

                    List<NickInfo> nickInfoList = systemCustomerInfo.getNickInfoList();
                    for (NickInfo nick : nickInfoList){
                        String nasOuid = nick.getNasOuid();
                        Integer platform = nick.getPlatform();

                        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();
                        gradeMemberInfoQuery.eq("nasOuid",nasOuid);
                        gradeMemberInfoQuery.eq("platform",platform);
                        gradeMemberInfoQuery.eq("shopId",shopIds.get(0));
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
    public void transOffLineShopActiveCustomerGrade() throws Exception {
        CustomerInfoQueryRequest request = new CustomerInfoQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        List<Long> shopIds = new ArrayList<>();

        long[] data = {
                100234651, 100186884, 100186879, 100186880, 100186881, 100186882, 100186883,
                100186877, 100186878, 100157262, 100150553, 100150168, 100150169, 100150170,
                100150171, 100150172, 100150173, 100150175, 100150176, 100150177, 100150178,
                100150180, 100150181, 100150182, 100150183, 100150184, 100150186, 100150187,
                100150188, 100150189, 100150190, 100150191, 100150192, 100150194, 100150195,
                100150196, 100150197, 100150198, 100150199, 100150200, 100150201, 100150202,
                100150203, 100150204, 100150205, 100150206, 100150207, 100150208, 100150209,
                100150210, 100150211, 100150212, 100150213, 100150214, 100150215, 100150216,
                100150217, 100150218, 100150219, 100150221, 100150222, 100150223, 100150224,
                100150226, 100150227, 100150229, 100150230, 100150231, 100150232, 100150234,
                100150235, 100150236, 100150237, 100150238, 100150240, 100150241
        };

        for (long num : data) {
            shopIds.add(num);
        }

        List<QueryMemberVo> queryMemberVos = shopActiveCustomerMapper.queryMemberVos1(shopIds);

        int batchSize = 100; // 每次处理的数据量
        //int totalSize  = 1; // 总数据量;
        int totalSize  = queryMemberVos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<QueryMemberVo> batchList = queryMemberVos.subList(start, end);

            List<NickPlatform> nickList = new ArrayList<>();

            for (QueryMemberVo queryMemberVo : batchList) {
                String nasOuid = queryMemberVo.getNasOuid();

                NickPlatform nickPlatform = new NickPlatform();
                nickPlatform.setNasOuid(nasOuid);
                nickPlatform.setPlatform(queryMemberVo.getPlatform());
                nickList.add(nickPlatform);
            }
            request.setNickList(nickList);

            //获取上面的店铺id
            request.setShopId(shopIds.get(0));
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
                        Integer platform = nick.getPlatform();

                        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();
                        gradeMemberInfoQuery.eq("nasOuid",nasOuid);
                        gradeMemberInfoQuery.eq("platform",platform);
                        gradeMemberInfoQuery.eq("shopId",shopIds.get(0));
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
    public void putShopActiveCustomerGrade(List<Long> shopIds) throws Exception {

        CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());


        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();

        gradeMemberInfoQuery.in("shopId",shopIds).eq("platform","111")
                .in("nasOuid","1@#yJt7dWzZHWjSe1n+ID6z7Z8HJ3WtxITjEUOJzIDKOMuRz1EdWZGlOj+YryUqyXny3Q==",
                "1@#kOQ+lPEox2LFsf17lKgEYXlVyqUZqXIyMZlOQoLagyZiCG+qdqxl9jBgoSn48EJNzaQ5aOPs",
                "1@#Q+y1lSDhHItmXNd2kdT0S6aSaEyj2hjankZ2+Bko7Pw4pc5BuQLqhj1kS0yigYzYmv/aazE2",
                "1@#JR/DRCDuMPp3jG/tvGnxaz8rbV4Svi7eyrTXxPYQiCkCZkLOt70X94gXuyb6PnjSgA==",
                "1@#ANIJ03A5A01Rm5QfVkQ2cdZBmGLJEQne3JqRvsE07CjKSiQQyCo/IpX/Em3smkA2nQ==",
                "1@#iqcOe4wqO/zxpExONIagptbJqNnXQbMXVRQh8+K0hRkMXhNdQsYI/XuwrAPLyTkABEKwOBmW",
                "1@#mkIQ2RamVcwccMbwmOctIXE4NhrrDuN2EOmKEaBm+6j3jnbFeHiKYu5W+NKmkRFxNrYuPDxQ",
                "1@#aKPyN8y2HArDUzTNsVJg7LtJbzPcn/j94XwOUYKVo+8VTog7RrIhFVdCUvb4YS9WSg==",
                "1@#RQ+mnkMa+FGSqYrZWVxl45wftHpDC5f7LMeks5WZZncxdPgEBR5u823zZqu4bHfnjdg/qqBC",
                "1@#QRlEI7nIMAmMebnUfDtI+/JQE1yClv6POGE4kdclsJcAlzdwjecre6doP7h4yIxzUMlJTeGe",
                "1@#XN9RqCu7Er/OviRCMNdiYcyzjfpsI+1m2px5nm99939zKroZLdDvnwmFQ+GfGYuYTg==",
                "1@#RYpS39/o/+qN0n3+7A/+Bea+tbv2mGhYdHa7ccA+XuSgAeWmomDTkqz1tARM7dWetVTSzydn",
                "1@#3gUSZ+bb8srNWpsk0XizB6VWc/RjX1ozh3pV8O+hm3PRPPJRbanKVQi3sUUHHhQzJw==",
                "1@#KPv+QKcoTaNEF3ccocdHI4c2NIEr3siQDmuJmHWqQVofx+yf6fcNdyWwT7w9zn+Iid3swx4=",
                "1@#sJdZoaPFwz6l9Z8M9RrW9IbKf3jcALQo+6lpEq20rv9IM7iapeswzMBw+SMbI51U3g==",
                "1@#izGdfsauyFmMwA6VliuipnOVZVVhqPtjtVuan0qY2xTQpEcJOnnCBfHC5r19WjPqtQ==",
                "1@#P4iddKyRvnIoQm8VEXFOxEJ7MKDOvt57GB/cOuMC3dLTs8hwPFUCLXdHJfv8OTZkS11o22k/",
                "1@#kkzmwt/RbzES8RPOywzVJNOpV4xh9wtoiEuuZum2X7lSRtMs5PyDoSi9jtTiJG+OndaZJv+j",
                "1@#KhEH+wanfToL+WtzhFIPKC8RuUj0AEYHdi+DURcv+yW5F8LIFyujea0TWt8KxwNGZp3LjFw=",
                "1@#cft5zhwjO7ayF+qX7sJhfhWLyNNp8lA/MVUi+J/PnIOX6KHWeZLUg0e2QFw7W+nPsQ==",
                "1@#XN8XQWS6tRJrTleecGe7FXqvgXnmbINK1h8zSa20sckMyj0cOuVSFBXJMwTQQrjllSXZqv1k",
                "1@#HCiX6v5LZX7pVJPQ5iKP6Fql2feLtCXPnm4DzhHXrmS4vfEF7IWfaqrm9D7BqaSRCw==",
                "1@#ULd42s/hRdwQbnMJ/55TBMU4OQqwxPTOFBC4q/+Ftox/JUkWtjIKePeOEHzNnVxac1w3olDw");

        List<GradeMemberInfo> gradeMemberInfos = gradeMemberInfoService.list(gradeMemberInfoQuery);

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = gradeMemberInfos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<GradeMemberInfo> batchList = gradeMemberInfos.subList(start, end);

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();
            for (GradeMemberInfo gradeMemberInfo : batchList){

                CustomerGradeUpdateInfo customerGradeUpdateInfo = new CustomerGradeUpdateInfo();
                customerGradeUpdateInfo.setNasOuid(gradeMemberInfo.getNasOuid());
                customerGradeUpdateInfo.setGrade(gradeMemberInfo.getGrade());
                customerGradeUpdateInfoList.add(customerGradeUpdateInfo);

            }


            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);
            request.setPlatform(111);


            Map<Long,Long> storeIdMap= storeIdMap();
            Long shopId = shopIds.get(0);
            request.setShopId(storeIdMap.get(shopId));


            ApiClient apiClient =new ApiClientImpl(request);
            CustomerGradeUpdateResponse response = apiClient.execute(request);
            log.info(response.getBody());
            if ("200".equals(response.getCode())){
                log.info(response.getMsg());
            }else {
                log.info(response.getBody());
                TransBtnCustomerFail transBtnCustomerFail = new TransBtnCustomerFail();
                transBtnCustomerFail.setId(customerGradeUpdateInfoList.get(0).getNasOuid());
                transBtnCustomerFail.setMessage(response.getBody());
                transBtnCustomerFail.setType("grade");
                transBtnCustomerFailService.save(transBtnCustomerFail);
            }

            log.info("执行下次循环");
        }

    }

    @Override
    public void putOffLineShopActiveCustomerGrade(List<Long> shopIds) throws Exception {
        CustomerGradeUpdateRequest request = new CustomerGradeUpdateRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());


        QueryWrapper<GradeMemberInfo> gradeMemberInfoQuery = new QueryWrapper<>();

        gradeMemberInfoQuery.in("shopId",shopIds).eq("platform","0");

        List<GradeMemberInfo> gradeMemberInfos = gradeMemberInfoService.list(gradeMemberInfoQuery);

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = gradeMemberInfos.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量
            List<GradeMemberInfo> batchList = gradeMemberInfos.subList(start, end);

            List<CustomerGradeUpdateInfo> customerGradeUpdateInfoList = new ArrayList<>();
            for (GradeMemberInfo gradeMemberInfo : batchList){

                CustomerGradeUpdateInfo customerGradeUpdateInfo = new CustomerGradeUpdateInfo();
                customerGradeUpdateInfo.setNasOuid(gradeMemberInfo.getNasOuid());
                customerGradeUpdateInfo.setGrade(gradeMemberInfo.getGrade());
                customerGradeUpdateInfoList.add(customerGradeUpdateInfo);

            }


            request.setCustomerGradeUpdateInfoList(customerGradeUpdateInfoList);
            request.setPlatform(0);


            Map<Long,Long> storeIdMap= storeIdMap();
            Long shopId = shopIds.get(0);
            request.setShopId(storeIdMap.get(shopId));


            ApiClient apiClient =new ApiClientImpl(request);
            CustomerGradeUpdateResponse response = apiClient.execute(request);

            if ("200".equals(response.getCode())){
                log.info(response.getMsg());
            }else {
                log.info(response.getBody());
                TransBtnCustomerFail transBtnCustomerFail = new TransBtnCustomerFail();
                transBtnCustomerFail.setId(customerGradeUpdateInfoList.get(0).getNasOuid());
                transBtnCustomerFail.setMessage(response.getBody());
                transBtnCustomerFail.setType("grade");
                transBtnCustomerFailService.save(transBtnCustomerFail);
            }

            log.info("执行下次循环");
        }
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

    public static Map<Long,Long> storeIdMap(){

        Map<Long, Long> storeIdMap = new HashMap<>();

        // 数据初始化
        storeIdMap.put(100150234L, 101130609L);
        storeIdMap.put(100157262L, 101130549L);
        storeIdMap.put(100150235L, 101130610L);
        storeIdMap.put(100234651L, 101130540L);
        storeIdMap.put(100150179L, 101155857L);
        storeIdMap.put(100186879L, 101130542L);
        storeIdMap.put(100150210L, 101130589L);
        storeIdMap.put(100150228L, 101155862L);
        storeIdMap.put(100150244L, 101155867L);
        storeIdMap.put(100150229L, 101130605L);
        storeIdMap.put(100150230L, 101130606L);
        storeIdMap.put(100150211L, 101130590L);
        storeIdMap.put(100150233L, 101155863L);
        storeIdMap.put(100150166L, 101130620L);
        storeIdMap.put(100156928L, 101092686L);
        storeIdMap.put(100149662L, 101130619L);
        storeIdMap.put(100150205L, 101130584L);
        storeIdMap.put(100150214L, 101130593L);
        storeIdMap.put(100150199L, 101130578L);
        storeIdMap.put(100150197L, 101130576L);
        storeIdMap.put(100150169L, 101130552L);
        storeIdMap.put(100186877L, 101130547L);
        storeIdMap.put(100150226L, 101130603L);
        storeIdMap.put(100150225L, 101155861L);
        storeIdMap.put(100150212L, 101130591L);
        storeIdMap.put(100150237L, 101130612L);
        storeIdMap.put(100150180L, 101130561L);
        storeIdMap.put(100150219L, 101130598L);
        storeIdMap.put(100186881L, 101130544L);
        storeIdMap.put(100150177L, 101130559L);
        storeIdMap.put(100150221L, 101130599L);
        storeIdMap.put(100150168L, 101130551L);
        storeIdMap.put(100150172L, 101130555L);
        storeIdMap.put(100150175L, 101130557L);
        storeIdMap.put(100150182L, 101130563L);
        storeIdMap.put(100150184L, 101130565L);
        storeIdMap.put(100150183L, 101130564L);
        storeIdMap.put(100156915L, 101155854L);
        storeIdMap.put(100150232L, 101130608L);
        storeIdMap.put(100150173L, 101130556L);
        storeIdMap.put(100150245L, 101155868L);
        storeIdMap.put(100150227L, 101130604L);
        storeIdMap.put(100186878L, 101130548L);
        storeIdMap.put(100150213L, 101130592L);
        storeIdMap.put(100149663L, 101130621L);
        storeIdMap.put(100149661L, 101130618L);
        storeIdMap.put(100150241L, 101130615L);
        storeIdMap.put(100150238L, 101130613L);
        storeIdMap.put(100150243L, 101155866L);
        storeIdMap.put(100150242L, 101155865L);
        storeIdMap.put(100150198L, 101130577L);
        storeIdMap.put(100150239L, 101155864L);
        storeIdMap.put(100150174L, 101155856L);
        storeIdMap.put(100150208L, 101130587L);
        storeIdMap.put(100150209L, 101130588L);
        storeIdMap.put(100150218L, 101130597L);
        storeIdMap.put(100150240L, 101130614L);
        storeIdMap.put(100150178L, 101130560L);
        storeIdMap.put(100150181L, 101130562L);
        storeIdMap.put(100150176L, 101130558L);
        storeIdMap.put(100150171L, 101130554L);
        storeIdMap.put(100186883L, 101130546L);
        storeIdMap.put(100150223L, 101130601L);
        storeIdMap.put(100150167L, 101155855L);
        storeIdMap.put(100186882L, 101130545L);
        storeIdMap.put(100150222L, 101130600L);
        storeIdMap.put(100150236L, 101130611L);
        storeIdMap.put(100186884L, 101130541L);
        storeIdMap.put(100150224L, 101130602L);
        storeIdMap.put(100150553L, 101130550L);
        storeIdMap.put(100149660L, 101130616L);
        storeIdMap.put(100150083L, 101092685L);
        storeIdMap.put(100150165L,101130617L);
        return storeIdMap;
    }
}
