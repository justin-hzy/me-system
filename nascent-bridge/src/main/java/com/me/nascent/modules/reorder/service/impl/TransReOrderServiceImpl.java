package com.me.nascent.modules.reorder.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.reorder.entity.ReFundNickInfo;
import com.me.nascent.modules.reorder.service.ReFundNickInfoService;
import com.me.nascent.modules.reorder.service.ReFundService;
import com.me.nascent.modules.reorder.service.TransReOrderService;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.NickInfo;
import com.nascent.ecrp.opensdk.domain.refund.*;
import com.nascent.ecrp.opensdk.request.refund.RefundInfoSynRequest;
import com.nascent.ecrp.opensdk.request.refund.ThirdRefundSaveRequest;
import com.nascent.ecrp.opensdk.request.trade.TradeSynRequest;
import com.nascent.ecrp.opensdk.response.customer.ActivateCustomerListSyncResponse;
import com.nascent.ecrp.opensdk.response.refund.RefundInfoSynResponse;
import com.nascent.ecrp.opensdk.response.refund.ThirdRefundSaveResponse;
import com.nascent.ecrp.opensdk.response.trade.TradeSynResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransReOrderServiceImpl implements TransReOrderService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private ReFundService reFundService;

    private ReFundNickInfoService reFundNickInfoService;

    @Override
    public void transReOrder(Long id, Date startDate, Date endDate) throws Exception {
        RefundInfoSynRequest request = new RefundInfoSynRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setId(id);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        RefundInfoSynResponse response = client.execute(request);
        String requestId = response.getRequestId();
        log.info("requestId="+requestId);
        log.info(response.getMsg());
        List<RefundSynInfo> refundSynInfos = response.getResult();

        List<ReFund> insertReFunds = new ArrayList<>();

        List<ReFund> updateReFunds = new ArrayList<>();

        if(refundSynInfos.size()>0){
            for (RefundSynInfo refundSynInfo : refundSynInfos){

                QueryWrapper<ReFund> reFundQuery = new QueryWrapper<>();
                reFundQuery.eq("id",refundSynInfo.getId());
                ReFund reFundResult = reFundService.getOne(reFundQuery);


                ReFund reFund = new ReFund();
                BeanUtils.copyProperties(refundSynInfo,reFund);

                if(reFundResult != null){
                    updateReFunds.add(reFund);
                }else {
                    insertReFunds.add(reFund);
                }

                List<NickInfo> nickInfos = refundSynInfo.getNickInfos();
                List<ReFundNickInfo> reFundNickInfos = new ArrayList<>();
                for (NickInfo nickInfo : nickInfos){
                    ReFundNickInfo reFundNickInfo = new ReFundNickInfo();
                    BeanUtils.copyProperties(nickInfo,reFundNickInfo);
                    reFundNickInfo.setMainid(refundSynInfo.getId());
                    reFundNickInfos.add(reFundNickInfo);
                }
                reFundNickInfoService.saveBatch(reFundNickInfos);
            }

            if(insertReFunds.size()>0){
                reFundService.saveBatch(insertReFunds);
            }

            if(updateReFunds.size()>0){
                reFundService.saveOrUpdateBatch(updateReFunds);
            }
        }
    }

    @Override
    public void transReOrder(Date startDate, Date endDate) throws Exception {
        Long id = 0L;

        Boolean flag = true;

        while (flag){
            Map<String,Object> resMap = saveReFund(id,startDate,endDate);
            log.info(resMap.toString());
            id = (Long) resMap.get("id");
            startDate = (Date) resMap.get("startDate");
            flag = (Boolean) resMap.get("isNext");
        }


    }


    private Map<String,Object> saveReFund(Long id, Date startDate, Date endDate) throws Exception {

        Map<String,Object> resMap = new HashMap<>();

        Boolean isNext = false;

        RefundInfoSynRequest request = new RefundInfoSynRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.list().get(0).getToken());

        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setId(id);
        request.setPageSize(50);

        ApiClient client = new ApiClientImpl(request);
        RefundInfoSynResponse response = client.execute(request);

        if("60001".equals(response.getCode())){
            String token = tokenService.getToken();
            UpdateWrapper<Token> tokenUpdate = new UpdateWrapper<>();
            tokenUpdate.eq("name","nascent").set("token","token");
            resMap.put("id",id);
            resMap.put("startDate",startDate);
            resMap.put("isNext",true);
        }else if("200".equals(response.getCode())){
            //log.info(response.getBody());
            List<RefundSynInfo> refundSynInfos = response.getResult();

            List<ReFund> insertReFunds = new ArrayList<>();

            List<ReFund> updateReFunds = new ArrayList<>();

            if(refundSynInfos.size()>0){
                isNext = true;
                for (RefundSynInfo refundSynInfo : refundSynInfos){
                    id = refundSynInfo.getId();

                    startDate = refundSynInfo.getUpdateTime();

                    QueryWrapper<ReFund> reFundQuery = new QueryWrapper<>();
                    reFundQuery.eq("id",refundSynInfo.getId());
                    ReFund reFundResult = reFundService.getOne(reFundQuery);


                    ReFund reFund = new ReFund();
                    BeanUtils.copyProperties(refundSynInfo,reFund);
                    log.info(refundSynInfo.toString());
                    log.info(reFund.toString());
                    reFund.setUpdateTime(refundSynInfo.getUpdateTime());

                    if(reFundResult != null){
                        updateReFunds.add(reFund);
                    }else {
                        insertReFunds.add(reFund);
                    }

                    List<NickInfo> nickInfos = refundSynInfo.getNickInfos();
                    List<ReFundNickInfo> reFundNickInfos = new ArrayList<>();

                    if (CollUtil.isNotEmpty(nickInfos)){

                        /*QueryWrapper<ReFundNickInfo> reFundNickInfoQuery = new QueryWrapper<>();
                        reFundNickInfoQuery.eq("mainid",id);
                        reFundNickInfoService.remove(reFundNickInfoQuery);*/

                        for (NickInfo nickInfo : nickInfos){


                            ReFundNickInfo reFundNickInfo = new ReFundNickInfo();
                            BeanUtils.copyProperties(nickInfo,reFundNickInfo);
                            reFundNickInfo.setMainid(refundSynInfo.getId());
                            reFundNickInfos.add(reFundNickInfo);
                        }
                        reFundNickInfoService.saveBatch(reFundNickInfos);
                    }
                }

                if(insertReFunds.size()>0){
                    reFundService.saveBatch(insertReFunds);
                }

                if(updateReFunds.size()>0){
                    reFundService.saveOrUpdateBatch(updateReFunds);
                }

                resMap.put("id",id);
                resMap.put("startDate",startDate);
                resMap.put("isNext",isNext);
            }else {
                resMap.put("id",0L);
                resMap.put("startDate",startDate);
                resMap.put("isNext",false);
            }
        }


        return resMap;
    }



    @Override
    public void putReOrder() throws Exception {

        ThirdRefundSaveRequest request = new ThirdRefundSaveRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());
        request.setShopId(3411331L);

        ThirdRefund refund = new ThirdRefund();

        String createDateStr  = "2022-07-01 00:00:00";

        //定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 解析字符串到 LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.parse(createDateStr, formatter);
        // 转换为 Date
        Date createDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
        refund.setCreated(createDate);
        refund.setOutRefundId("123456");
        BigDecimal reFundFee = new BigDecimal("100");
        refund.setRefundFee(reFundFee);
        refund.setRefundReasonStr("测试");
        refund.setRefundStatus("SUCCESS");
        refund.setRefundWay(0);
        refund.setOutTradeId("1601695815015278366");


        RefundPointInfo refundPointInfo = new RefundPointInfo();
        refundPointInfo.setIntegralAccount("13316001823");
        BigDecimal point = new BigDecimal(100);
        refundPointInfo.setPoint(point);
        refund.setRefundPointInfo(refundPointInfo);

        List<RefundSgInfo> refundSgInfos = new ArrayList<>();
        RefundSgInfo refundSgInfo = new RefundSgInfo();
        BigDecimal ratio = new BigDecimal(0.5);
        refundSgInfo.setRatio(ratio);
        refundSgInfos.add(refundSgInfo);
        refund.setRefundSgInfos(refundSgInfos);

        List<ThirdRefund> refunds = new ArrayList<>();
        refunds.add(refund);
        request.setRefunds(refunds);

        ApiClient client = new ApiClientImpl(request);

        log.info(request.toString());

        ThirdRefundSaveResponse response = client.execute(request);
        log.info(response.getBody());
    }





}
