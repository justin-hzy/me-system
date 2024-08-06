package com.me.nascent.modules.reorder.service.impl;

import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.reorder.service.ReFundService;
import com.me.nascent.modules.reorder.service.TransReOrderService;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransReOrderServiceImpl implements TransReOrderService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private ReFundService reFundService;

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
        log.info(response.getBody());
        List<RefundSynInfo> refundSynInfos = response.getResult();

        List<ReFund> reFunds = new ArrayList<>();

        if(refundSynInfos.size()>0){
            for (RefundSynInfo refundSynInfo : refundSynInfos){
                ReFund refund = new ReFund();
                BeanUtils.copyProperties(refundSynInfo,refund);
                reFunds.add(refund);
            }

            if(reFunds.size()>0){
                reFundService.saveBatch(reFunds);
            }
        }
    }

    @Override
    public void saveReOrder() throws Exception {

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
