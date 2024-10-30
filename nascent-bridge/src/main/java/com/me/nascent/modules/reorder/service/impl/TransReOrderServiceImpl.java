package com.me.nascent.modules.reorder.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.order.entity.Trade;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.reorder.entity.ReFundNickInfo;
import com.me.nascent.modules.reorder.service.ReFundNickInfoService;
import com.me.nascent.modules.reorder.service.ReFundService;
import com.me.nascent.modules.reorder.service.TransReOrderService;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.me.nascent.modules.trans.entity.TransBtnRefundFail;
import com.me.nascent.modules.trans.service.TransBtnRefundFailService;
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
import com.nascent.ecrp.opensdk.response.trade.TradeSaveResponse;
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

    private TransBtnRefundFailService transBtnRefundFailService;

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
            log.info(response.getBody());
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
                        //updateReFunds.add(reFund);
                    }else {
                        insertReFunds.add(reFund);

                        List<NickInfo> nickInfos = refundSynInfo.getNickInfos();
                        List<ReFundNickInfo> reFundNickInfos = new ArrayList<>();

                        if (CollUtil.isNotEmpty(nickInfos)){

                            for (NickInfo nickInfo : nickInfos){
                                ReFundNickInfo reFundNickInfo = new ReFundNickInfo();
                                BeanUtils.copyProperties(nickInfo,reFundNickInfo);
                                reFundNickInfo.setMainid(refundSynInfo.getId());
                                reFundNickInfos.add(reFundNickInfo);
                            }
                            reFundNickInfoService.saveBatch(reFundNickInfos);
                        }
                    }
                }

                if(insertReFunds.size()>0){
                    reFundService.saveBatch(insertReFunds);
                }

                /*if(updateReFunds.size()>0){
                    reFundService.saveOrUpdateBatch(updateReFunds);
                }*/

                resMap.put("id",id);
                resMap.put("startDate",startDate);
                resMap.put("isNext",isNext);
            }else {
                resMap.put("id",0L);
                resMap.put("startDate",startDate);
                resMap.put("isNext",false);
            }
        }else {
            log.info(response.getBody());
            resMap.put("id",0L);
            resMap.put("startDate",startDate);
            resMap.put("isNext",false);
        }


        return resMap;
    }



    @Override
    public void putReOrder() throws Exception {
        Map<Long,Long> storeIdMap = storeIdMap();
        ThirdRefundSaveRequest request = new ThirdRefundSaveRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());

        List<String> list1 = new ArrayList<>();
        list1.add("100172217");
        list1.add("100172216");

        QueryWrapper<ReFund> reFundQuery = new QueryWrapper<>();
        reFundQuery
                //.between("applyTime","2013-04-01 00:00:00","2013-06-30 23:59:59")
                //.likeRight("applyTime","2024")
                //.ne("shopId","100172217").ne("shopId","100172216").ne("shopId","0");
                .in("shopId",list1);
        List<ReFund> existReFund = reFundService.list(reFundQuery);

        HashMap<Long,List<ReFund>> reFundHashMap = new LinkedHashMap<>();
        for (ReFund reFund : existReFund){
            Long shopId = reFund.getShopId();
            if(reFundHashMap.containsKey(shopId)){
                List<ReFund> reFundList = reFundHashMap.get(shopId);
                reFundList.add(reFund);
            }else {
                List<ReFund> reFundList = new ArrayList<>();
                reFundList.add(reFund);
                reFundHashMap.put(shopId,reFundList);
            }
        }

        Set<Long> keys = reFundHashMap.keySet();

        for (long key : keys){
            List<ReFund> list = reFundHashMap.get(key);

            int batchSize = 100; // 每次处理的数据量
            int totalSize = list.size(); // 总数据量
            //int totalSize = 1;
            int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

            for (int i = 0 ;i< loopCount;i++){
                int start = i * batchSize; // 开始索引
                int end = Math.min((i + 1) * batchSize, totalSize);

                List<ReFund> batchList = list.subList(start, end);

                List<ThirdRefund> refunds = new ArrayList<>();

                for (ReFund reFund : batchList) {
                    ThirdRefund thirdRefund = new ThirdRefund();
                    BeanUtils.copyProperties(reFund, thirdRefund);
                    thirdRefund.setCreated(reFund.getApplyTime());
                    thirdRefund.setRefundWay(reFund.getRefundWap());
                    thirdRefund.setItemNum(reFund.getNumber());
                    thirdRefund.setItemPrice(reFund.getPrice());
                    if(StrUtil.isNotEmpty(reFund.getReasonStr())){
                        thirdRefund.setRefundReasonStr(reFund.getReasonStr());
                    }else {
                        thirdRefund.setRefundReasonStr("-");
                    }
                    thirdRefund.setItemTitle(reFund.getTitle());
                    refunds.add(thirdRefund);
                }
                request.setRefunds(refunds);
                log.info(refunds.size()+"");

                log.info("悦江shopid="+key);
                log.info("贝泰妮shopid="+storeIdMap.get(key));
                request.setShopId(storeIdMap.get(key));
                request.setIsBackIntegral(false);

                ApiClient client = new ApiClientImpl(request);

                ThirdRefundSaveResponse response = client.execute(request);
                log.info(response.getBody());

                if(!"200".equals(response.getCode())){
                    String ids = "";
                    for (int j = 0 ; j<batchList.size();j++){
                        ReFund reFund = batchList.get(j);
                        Long id = reFund.getId();

                        if(j == (batchList.size()-1)){
                            ids = ids+String.valueOf(id);
                        }else {
                            ids = ids+String.valueOf(id)+",";
                        }
                    }
                    TransBtnRefundFail transBtnRefundFail  = new TransBtnRefundFail();
                    transBtnRefundFail.setIds(ids);
                    transBtnRefundFail.setMessage(response.getMsg()+","+response.getRequestId());

                    transBtnRefundFailService.save(transBtnRefundFail);
                }
            }
        }

        /*






        */
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
        storeIdMap.put(100150188L,101130568L);
        storeIdMap.put(100172217L,101242273L);
        storeIdMap.put(100172216L,101242274L);
        return storeIdMap;
    }
}
