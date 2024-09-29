package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.*;
import com.me.nascent.modules.member.mapper.ShopActiveCustomerMapper;
import com.me.nascent.modules.member.service.*;
import com.me.nascent.modules.member.vo.QueryMemberVo;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.CustomerSaveInfo;
import com.nascent.ecrp.opensdk.domain.customer.WechatInfo;
import com.nascent.ecrp.opensdk.request.customer.BatchCustomerSaveRequest;
import com.nascent.ecrp.opensdk.request.customer.MemberQueryRequest;
import com.nascent.ecrp.opensdk.response.customer.BatchCustomerSaveResponse;
import com.nascent.ecrp.opensdk.response.refund.ThirdRefundSaveResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class MemberMigrationServiceImpl implements MemberMigrationService {

    private TransMemberService transMemberService;

    private NascentConfig nascentConfig;

    private TokenService tokenService;



    private MemberTongService memberTongService;

    private ShopActiveCustomerService shopActiveCustomerService;

    private ShopActiveCustomerMapper shopActiveCustomerMapper;

    private ShopActiveCustomerNickService shopActiveCustomerNickService;

    private ShopActiveCustomerCardService shopActiveCustomerCardService;

    private ShopActiveCustomerWeChatService shopActiveCustomerWeChatService;

    @Override
    public void transPureMemberByRange(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

            transMemberService.TransPureMemberByRange(startDate,endDateOfWeek);

            startDate = endDateOfWeek;  // 修改为1分钟
            //System.out.println("下一个开启时间:" + startDate);
        }
            /*Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

            transMemberService.TransMemberByRange(startDate,endDateOfWeek);

            startDate = endDateOfWeek; // 修改为1分钟*/
    }

    @Override
    public void transZaMemberByRange(Date startDate, Date endDate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() + 6 * 24 * 60 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

            transMemberService.TransZaMemberByRange(startDate,endDateOfWeek);

            startDate = endDateOfWeek;  // 修改为1分钟
            //System.out.println("下一个开启时间:" + startDate);
        }
    }

    @Override
    public void transStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (startDate.before(endDate)) {
            Date endDateOfWeek = new Date(startDate.getTime() + 1 * 24 * 60 * 60 * 1000); // 修改为30分钟
            if (endDateOfWeek.after(endDate)) {
                endDateOfWeek = endDate;
            }

            String startStr = sdf.format(startDate);
            String endStr = sdf.format(endDateOfWeek);
            System.out.println("同步订单数据: " + startStr + " 到 " + endStr);

            transMemberService.transPureStoreMemberByRange(startDate,endDateOfWeek,shopId);

            startDate = endDateOfWeek;  // 修改为1分钟
            //System.out.println("下一个开启时间:" + startDate);
        }
    }

    @Override
    public void transMemberTong(Date start,Date end) throws Exception {

        boolean isNext = true;
        Integer pageNo = 1;
        while (isNext){
            Map<String,Object> respMap = transMemberService.transMemberTong(start,end,pageNo);

            isNext = (boolean) respMap.get("isNext");
            if (isNext == true){
                pageNo = (Integer) respMap.get("pageNo");
            }
        }
    }

    @Override
    public void putOnLineShopActiveCustomer() throws Exception {
        BatchCustomerSaveRequest request = new BatchCustomerSaveRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());

        List<Long> shopIds = new ArrayList<>();
        /*泊美 抖音*/
        shopIds.add(100149663L);

        //泊美有赞
        //shopIds.add(100150166L);
        //会员中心 平台id 19
//        shopIds.add(100156928L);
//        shopIds.add(100150083L);
        //淘宝 100149660
        //shopIds.add(100149660L);
        //抖店 100149661 平台id 111 改执行sql
        //shopIds.add(100149661L);
        //有赞 100150165 平台 id 11
        //shopIds.add(100150165L);

        List<QueryMemberVo> queryMemberVos = shopActiveCustomerMapper.queryMemberVos(shopIds);

        Map<Long,List<QueryMemberVo>> mapList = new LinkedHashMap<>();
        for (QueryMemberVo queryMemberVo : queryMemberVos){
            Long shopId = queryMemberVo.getShopId();
            if(mapList.containsKey(shopId)){
                List<QueryMemberVo> queryMemberVoList = mapList.get(shopId);
                queryMemberVoList.add(queryMemberVo);
            }else {
                List<QueryMemberVo> queryMemberVoList = new ArrayList<>();
                queryMemberVoList.add(queryMemberVo);
                mapList.put(shopId,queryMemberVoList);
            }
        }

        Set<Long> keys = mapList.keySet();
        for (Long key : keys){
            List<QueryMemberVo> queryMemberVoList = mapList.get(key);
            /*for (QueryMemberVo queryMemberVo : queryMemberVoList){
                if (!key.equals(queryMemberVo.getShopId())){
                    log.info("算法错误");
                }
            }*/

            int batchSize = 100; // 每次处理的数据量
            int totalSize = queryMemberVoList.size(); // 总数据量

            int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

            for (int i = 0; i < loopCount; i++) {
                int start = i * batchSize; // 开始索引
                int end = Math.min((i + 1) * batchSize, totalSize);

                List<QueryMemberVo> batchList = queryMemberVoList.subList(start, end);
                log.info("batchList=" + batchList.toString());

                List<CustomerSaveInfo> customerSaveInfos = new ArrayList<>();
                for (QueryMemberVo queryMemberVo : batchList){
                    /*if (!key.equals(queryMemberVo.getShopId())){
                        log.info("算法错误");
                    }*/

                    CustomerSaveInfo customerSaveInfo = new CustomerSaveInfo();
                    BeanUtils.copyProperties(queryMemberVo,customerSaveInfo);
                    customerSaveInfo.setBindMobile("");
                    customerSaveInfo.setMobile("");
                    if (customerSaveInfo.getCustomerName().length()>100){
                        customerSaveInfo.setCustomerName("");
                    }

                    QueryWrapper<ShopActiveCustomerCard> shopActiveCustomerCardQuery = new QueryWrapper<>();
                    shopActiveCustomerCardQuery.eq("mainid",queryMemberVo.getId());
                    List<ShopActiveCustomerCard> shopActiveCustomerCards = shopActiveCustomerCardService.list(shopActiveCustomerCardQuery);

                    if(CollUtil.isNotEmpty(shopActiveCustomerCards)){
                        List<CardReceiveInfo> cardReceiveInfos = new ArrayList<>();
                        for (ShopActiveCustomerCard shopActiveCustomerCard : shopActiveCustomerCards){
                            CardReceiveInfo cardReceiveInfo = new CardReceiveInfo();
                            cardReceiveInfo.setCardReceivePlatform(shopActiveCustomerCard.getCardReceivePlatform());
                            cardReceiveInfo.setCardType(shopActiveCustomerCard.getCardType());
                            cardReceiveInfo.setCardReceiveTime(shopActiveCustomerCard.getCardReceiveTime());

                            cardReceiveInfos.add(cardReceiveInfo);
                        }
                        customerSaveInfo.setCardReceiveInfoList(cardReceiveInfos);
                    }

                    QueryWrapper<ShopActiveCustomerWeChat> shopActiveCustomerWeChatQuery = new QueryWrapper<>();
                    shopActiveCustomerWeChatQuery.eq("mainid",queryMemberVo.getId());
                    List<ShopActiveCustomerWeChat> shopActiveCustomerWeChats = shopActiveCustomerWeChatService.list(shopActiveCustomerWeChatQuery);

                    if(CollUtil.isNotEmpty(shopActiveCustomerWeChats)){

                        List<WechatInfo> wechatInfos = new ArrayList<>();
                        for (ShopActiveCustomerWeChat shopActiveCustomerWeChat : shopActiveCustomerWeChats){

                            WechatInfo wechatInfo = new WechatInfo();

                            wechatInfo.setWxAccountId(shopActiveCustomerWeChat.getWxAccountId());
                            wechatInfo.setWxOpenId(shopActiveCustomerWeChat.getWxOpenId());
                            wechatInfo.setWxType(shopActiveCustomerWeChat.getWxType());
                            wechatInfos.add(wechatInfo);
                        }
                        customerSaveInfo.setWechatInfoList(wechatInfos);
                    }

                    customerSaveInfos.add(customerSaveInfo);
                }

                request.setCustomerSaveList(customerSaveInfos);

                Map<Long,Long> storeIdMap = storeIdMap();
                Long shopId = storeIdMap.get(key);

                if (shopId != null){
                    request.setShopId(shopId);
                }else {
                    log.info("悦江店铺id="+key+",找不到对应的贝泰妮店铺id");
                }
                ApiClient apiClient = new ApiClientImpl(request);


                BatchCustomerSaveResponse response = apiClient.execute(request);
                if ("200".equals(response.getCode())){
                    log.info(response.getMsg());
                }else if("500".equals(response.getCode())){
                    log.info(response.getBody());
                    boolean flag = true;
                    while (flag){
                        BatchCustomerSaveResponse response1 = apiClient.execute(request);
                        if("200".equals(response1.getCode())){
                            flag = false;
                        }else {
                            log.info(response.getBody());
                        }
                    }
                }else {
                    log.info(response.getBody());
                }
                log.info("执行下次循环");
            }
        }

    }

    @Override
    public void putOffLineShopActiveCustomer(List<Long> shopIds) throws Exception {
        BatchCustomerSaveRequest request = new BatchCustomerSaveRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(tokenService.getBtnToken());


        List<QueryMemberVo> queryMemberVos = shopActiveCustomerMapper.queryOffLineMemberVos(shopIds);

        Map<Long,List<QueryMemberVo>> mapList = new LinkedHashMap<>();
        for (QueryMemberVo queryMemberVo : queryMemberVos){
            Long shopId = queryMemberVo.getShopId();
            if(mapList.containsKey(shopId)){
                List<QueryMemberVo> queryMemberVoList = mapList.get(shopId);
                queryMemberVoList.add(queryMemberVo);
            }else {
                List<QueryMemberVo> queryMemberVoList = new ArrayList<>();
                queryMemberVoList.add(queryMemberVo);
                mapList.put(shopId,queryMemberVoList);
            }
        }

        Set<Long> keys = mapList.keySet();
        for (Long key : keys){
            List<QueryMemberVo> queryMemberVoList = mapList.get(key);

            int batchSize = 100; // 每次处理的数据量
            int totalSize = queryMemberVoList.size(); // 总数据量

            int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

            for (int i = 0; i < loopCount; i++) {
                int start = i * batchSize; // 开始索引
                int end = Math.min((i + 1) * batchSize, totalSize);

                List<QueryMemberVo> batchList = queryMemberVoList.subList(start, end);
                log.info("batchList=" + batchList.toString());

                List<CustomerSaveInfo> customerSaveInfos = new ArrayList<>();
                for (QueryMemberVo queryMemberVo : batchList){

                    CustomerSaveInfo customerSaveInfo = new CustomerSaveInfo();
                    BeanUtils.copyProperties(queryMemberVo,customerSaveInfo);
                    customerSaveInfo.setBindMobile("");
                    customerSaveInfo.setMobile("");
                    if (customerSaveInfo.getCustomerName().length()>100){
                        customerSaveInfo.setCustomerName("");
                    }

                    QueryWrapper<ShopActiveCustomerCard> shopActiveCustomerCardQuery = new QueryWrapper<>();
                    shopActiveCustomerCardQuery.eq("mainid",queryMemberVo.getId());
                    List<ShopActiveCustomerCard> shopActiveCustomerCards = shopActiveCustomerCardService.list(shopActiveCustomerCardQuery);

                    if(CollUtil.isNotEmpty(shopActiveCustomerCards)){
                        List<CardReceiveInfo> cardReceiveInfos = new ArrayList<>();
                        for (ShopActiveCustomerCard shopActiveCustomerCard : shopActiveCustomerCards){
                            CardReceiveInfo cardReceiveInfo = new CardReceiveInfo();
                            cardReceiveInfo.setCardReceivePlatform(shopActiveCustomerCard.getCardReceivePlatform());
                            cardReceiveInfo.setCardType(shopActiveCustomerCard.getCardType());
                            cardReceiveInfo.setCardReceiveTime(shopActiveCustomerCard.getCardReceiveTime());

                            cardReceiveInfos.add(cardReceiveInfo);
                        }
                        customerSaveInfo.setCardReceiveInfoList(cardReceiveInfos);
                    }

                    QueryWrapper<ShopActiveCustomerWeChat> shopActiveCustomerWeChatQuery = new QueryWrapper<>();
                    shopActiveCustomerWeChatQuery.eq("mainid",queryMemberVo.getId());
                    List<ShopActiveCustomerWeChat> shopActiveCustomerWeChats = shopActiveCustomerWeChatService.list(shopActiveCustomerWeChatQuery);

                    if(CollUtil.isNotEmpty(shopActiveCustomerWeChats)){

                        List<WechatInfo> wechatInfos = new ArrayList<>();
                        for (ShopActiveCustomerWeChat shopActiveCustomerWeChat : shopActiveCustomerWeChats){

                            WechatInfo wechatInfo = new WechatInfo();

                            wechatInfo.setWxAccountId(shopActiveCustomerWeChat.getWxAccountId());
                            wechatInfo.setWxOpenId(shopActiveCustomerWeChat.getWxOpenId());
                            wechatInfo.setWxType(shopActiveCustomerWeChat.getWxType());
                            wechatInfos.add(wechatInfo);
                        }
                        customerSaveInfo.setWechatInfoList(wechatInfos);
                    }

                    customerSaveInfos.add(customerSaveInfo);
                }

                request.setCustomerSaveList(customerSaveInfos);

                Map<Long,Long> storeIdMap = storeIdMap();
                Long shopId = storeIdMap.get(key);

                if (shopId != null){
                    request.setShopId(shopId);
                }else {
                    log.info("悦江店铺id="+key+",找不到对应的贝泰妮店铺id");
                }
                ApiClient apiClient = new ApiClientImpl(request);


                BatchCustomerSaveResponse response = apiClient.execute(request);
                if ("200".equals(response.getCode())){
                    log.info(response.getMsg());
                }else if("500".equals(response.getCode())){
                    log.info(response.getBody());
                    boolean flag = true;
                    while (flag){
                        BatchCustomerSaveResponse response1 = apiClient.execute(request);
                        if("200".equals(response1.getCode())){
                            flag = false;
                        }else {
                            log.info(response.getBody());
                        }
                    }
                }else {
                    log.info(response.getBody());
                }
                log.info("执行下次循环");
            }
        }
    }

    @Override
    public void putMemberTong() throws Exception {

        /*tokenService.getBtnToken();*/


        BatchCustomerSaveRequest request = new BatchCustomerSaveRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());

        QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
        tokenQuery.eq("name","btn");
        Token token = tokenService.getOne(tokenQuery);
        request.setAccessToken(token.getToken());

        QueryWrapper<MemberTong> memberTongQuery = new QueryWrapper<>();
        memberTongQuery.isNotNull("ouid").eq("sellerNick","za姬芮官方旗舰店");
        List<MemberTong> memberTongs = memberTongService.list(memberTongQuery);

        int batchSize = 100; // 每次处理的数据量
        int totalSize = memberTongs.size(); // 总数据量

        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize);

            List<MemberTong> batchList = memberTongs.subList(start, end);

            List<CustomerSaveInfo> customerSaveInfos = new ArrayList<>();
            for (MemberTong memberTong :  batchList){
                CustomerSaveInfo customerSaveInfo = new CustomerSaveInfo();
                customerSaveInfo.setSubPlatform(1);
                customerSaveInfo.setPlatform(1);
                customerSaveInfo.setNasOuid(memberTong.getOuid());
                customerSaveInfos.add(customerSaveInfo);
            }

            if (CollUtil.isNotEmpty(customerSaveInfos)){
                request.setCustomerSaveList(customerSaveInfos);
                /*泊美 101130619L Za 101130616L*/
                request.setShopId(101130616L);

                ApiClient client = new ApiClientImpl(request);
                BatchCustomerSaveResponse response = client.execute(request);
                if ("200".equals(response.getCode())){
                    log.info(response.getMsg());
                }else {
                    log.info(response.getBody());
                }
                log.info("进入下个循环");
            }

        }
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
