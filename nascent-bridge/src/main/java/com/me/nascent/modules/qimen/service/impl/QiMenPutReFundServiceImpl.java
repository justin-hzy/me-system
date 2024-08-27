package com.me.nascent.modules.qimen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.qimen.entity.QiMenOrder;
import com.me.nascent.modules.qimen.entity.QiMenTrade;
import com.me.nascent.modules.qimen.service.QiMenOrderService;
import com.me.nascent.modules.qimen.service.QiMenPutReFundService;
import com.me.nascent.modules.qimen.service.QiMenTradeService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.refund.ThirdRefund;
import com.nascent.ecrp.opensdk.domain.trade.TradeDetailVo;
import com.nascent.ecrp.opensdk.request.refund.ThirdRefundSaveRequest;
import com.nascent.ecrp.opensdk.response.refund.ThirdRefundSaveResponse;
import com.nascent.ecrp.opensdk.response.trade.TradeSaveResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class QiMenPutReFundServiceImpl implements QiMenPutReFundService {

    private QiMenOrderService qiMenOrderService;

    private NascentConfig nascentConfig;

    @Override
    public void putQiMenReFund() throws Exception {
        QueryWrapper<QiMenOrder> qiMenOrderQuery = new QueryWrapper<>();
        qiMenOrderQuery.eq("refundStatus", "SUCCESS");
        List<QiMenOrder> qiMenOrders = qiMenOrderService.list(qiMenOrderQuery);

        int batchSize = 100; // 每次处理的数据量
        int totalSize  = qiMenOrders.size(); // 总数据量;
        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < loopCount; i++) {

            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize); // 结束索引，确保不超过总数据量

            List<QiMenOrder> batchList = qiMenOrders.subList(start, end);
            log.info("batchList=" + batchList.toString());

            ThirdRefundSaveRequest request = new ThirdRefundSaveRequest();
            List<ThirdRefund> thirdRefunds = new ArrayList<>();
            for (QiMenOrder qiMenOrder : batchList){
                ThirdRefund thirdRefund = new ThirdRefund();
                Date created = sdf.parse(qiMenOrder.getEndTime());
                thirdRefund.setCreated(created);
                thirdRefund.setOutRefundId(qiMenOrder.getOid());
                thirdRefund.setRefundReasonStr("奇门退款单据初始化");
                thirdRefund.setRefundStatus(qiMenOrder.getRefundStatus());
                thirdRefund.setRefundWay(0);
                thirdRefunds.add(thirdRefund);
            }
            request.setRefunds(thirdRefunds);
            request.setServerUrl(nascentConfig.getBtnServerUrl());
            request.setAppSecret(nascentConfig.getAppSerect());
            request.setAppKey(nascentConfig.getBtnAppKey());
            request.setGroupId(nascentConfig.getBtnGroupID());


            ApiClient client = new ApiClientImpl(request);
            ThirdRefundSaveResponse response = client.execute(request);
        }
    }
}
