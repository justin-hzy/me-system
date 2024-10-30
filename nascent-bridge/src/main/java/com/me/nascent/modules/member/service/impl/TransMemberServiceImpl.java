package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.*;
import com.me.nascent.modules.member.service.*;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.*;
import com.nascent.ecrp.opensdk.domain.customer.wxFansStatus.BaseWxFansStatusVo;
import com.nascent.ecrp.opensdk.request.customer.ActivateCustomerListSyncRequest;
import com.nascent.ecrp.opensdk.request.customer.MemberQueryRequest;
import com.nascent.ecrp.opensdk.request.customer.ShopActiveCustomerListSyncRequest;
import com.nascent.ecrp.opensdk.response.customer.ActivateCustomerListSyncResponse;
import com.nascent.ecrp.opensdk.response.customer.MemberQueryResponse;
import com.nascent.ecrp.opensdk.response.customer.ShopActiveCustomerListSyncResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TransMemberServiceImpl implements TransMemberService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberService pureMemberService;

    private PureCardReceiveInfoService pureCardReceiveInfoService;

    private PureFansStatusService pureFansStatusService;

    private PureMemberNickInfoService pureMemberNickInfoService;

    private ZaMemberService zaMemberService;

    private ZaCardReceiveInfoService zaCardReceiveInfoService;

    private ZaFansStatusService zaFansStatusService;

    private ZaMemberNickInfoService zaMemberNickInfoService;

    private ShopActiveCustomerService shopActiveCustomerService;

    private ShopActiveCustomerNickService shopActiveCustomerNickService;

    private ShopActiveCustomerWeChatService shopActiveCustomerWeChat;

    private ShopActiveCustomerCardService shopActiveCustomerCardService;

    private MemberTongService memberTongService;

    @Override
    public void TransPureMemberByRange(Date startDate,Date endDate) throws Exception {

        Long nextId = 0L;
        boolean flag = true;

        while(flag){
            Map resMap =  savePureMember(nextId,startDate,endDate);
            startDate = (Date) resMap.get("updateTime");
            nextId = (Long) resMap.get("nextId");
            flag = (boolean) resMap.get("isNext");
            log.info("startDate="+startDate);
            log.info("nextId="+nextId);
            log.info("flag="+flag);
        }


    }

    @Override
    public void TransZaMemberByRange(Date startDate, Date endDate) throws Exception {
        Long nextId = 0L;
        boolean flag = true;

        while(flag){
            Map resMap =  saveZaMember(nextId,startDate,endDate);
            startDate = (Date) resMap.get("updateTime");
            nextId = (Long) resMap.get("nextId");
            flag = (boolean) resMap.get("isNext");
            log.info("startDate="+startDate);
            log.info("nextId="+nextId);
            log.info("flag="+flag);
        }
    }

    @Override
    public void transPureStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception {
        Long nextId = 0L;
        boolean flag = true;

        while(flag){
            Map resMap = savePureStoreMember(nextId,startDate,endDate,shopId);
            startDate = (Date) resMap.get("updateTime");
            nextId = (Long) resMap.get("nextId");
            flag = (boolean) resMap.get("isNext");
            log.info("startDate="+startDate);
            log.info("nextId="+nextId);
            log.info("flag="+flag);
        }
    }

    @Override
    public Map<String, Object> transMemberTong(Date start, Date end, Integer pageNo) throws Exception {


        MemberQueryRequest request = new MemberQueryRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setStartTime(start);
        request.setEndTime(end);
        request.setPageSize(200);
        request.setPageNo(pageNo);
        /*泊美官方旗舰店  za姬芮官方旗舰店 */
        request.setSellerNick("泊美官方旗舰店");
        request.setPlatform(1);
        /* pcode-206261	泊美积分体系 pcode-206256	Za线上积分体系 pcode-206258 Za姬芮线下积分体系*/
        request.setIntegralAccount("pcode-206261");

        QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
        tokenQuery.eq("name","nascent");
        List<Token> tokens = tokenService.list(tokenQuery);
        request.setAccessToken(tokens.get(0).getToken());

        ApiClient client = new ApiClientImpl(request);
        MemberQueryResponse response = client.execute(request);

        Map<String,Object> respMap = new HashMap<>();


        if ("200".equals(response.getCode())){
            if (response.getResult().size()>0){

                List<MemberQueryInfo> memberQueryInfos = response.getResult();

                List<MemberTong> insertList = new ArrayList<>();
                for (MemberQueryInfo memberQueryInfo : memberQueryInfos){

                    QueryWrapper<MemberTong> memberTongQuery = new QueryWrapper<>();
                    memberTongQuery.eq("ouid",memberQueryInfo.getOuid())
                            .eq("sellerNick",memberQueryInfo.getSellerNick());

                    List<MemberTong> existMemberTongs = memberTongService.list(memberTongQuery);
                    if(CollUtil.isEmpty(existMemberTongs)){
                        MemberTong memberTong = new MemberTong();
                        BeanUtils.copyProperties(memberQueryInfo,memberTong);
                        insertList.add(memberTong);
                    }
                }

                if(CollUtil.isNotEmpty(insertList)){
                    memberTongService.saveBatch(insertList);
                }


                pageNo = pageNo + 1;
                respMap.put("isNext",true);
                respMap.put("pageNo",pageNo);
            }else {
                respMap.put("isNext",false);
            }
        }else {
            log.info(response.getBody());
            respMap.put("isNext",true);
            respMap.put("pageNo",pageNo);
        }
        return respMap;
    }


    private void saveMemberTong(Date start,Date end) throws Exception {

    }

    private Map<String, Object> savePureMember(Long nextId,Date startDate,Date endDate) throws Exception {
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

            List<PureMember> insertPureMembers = new ArrayList<>();

            List<PureMember> updatePureMembers = new ArrayList<>();

            Boolean isNext = false;
            Date updateTime = null;

            if (CollUtil.isNotEmpty(systemCustomerInfos)){

                isNext = true;

                for (SystemCustomerInfo systemCustomerInfo : systemCustomerInfos){
                    Long id = systemCustomerInfo.getId();
                    nextId = id;
                    updateTime = systemCustomerInfo.getUpdateTime();
                    QueryWrapper<PureMember> memberQuery = new QueryWrapper<>();
                    memberQuery.eq("id",String.valueOf(id));
                    PureMember existPureMember = pureMemberService.getOne(memberQuery);

                    PureMember pureMember = new PureMember();
                    BeanUtils.copyProperties(systemCustomerInfo, pureMember);

                    /*insertMembers.add(member);*/
                    if(existPureMember != null){
                        //更新逻辑
                        updatePureMembers.add(pureMember);
                    }else {
                        //新增逻辑
                        insertPureMembers.add(pureMember);

                        List<CustomerCardReceiveInfo> customerCardReceiveInfos = systemCustomerInfo.getCardReceiveInfoList();

                        List<PureCardReceiveInfo> insertPureCardReceiveInfos = new ArrayList<>();

                        if(CollUtil.isNotEmpty(customerCardReceiveInfos)){

                            for (CustomerCardReceiveInfo customerCardReceiveInfo : customerCardReceiveInfos){
                                PureCardReceiveInfo pureCardReceiveInfo = new PureCardReceiveInfo();
                                BeanUtils.copyProperties(customerCardReceiveInfo, pureCardReceiveInfo);
                                pureCardReceiveInfo.setMainId(id);
                                insertPureCardReceiveInfos.add(pureCardReceiveInfo);
                            }

                            pureCardReceiveInfoService.saveBatch(insertPureCardReceiveInfos);
                        }

                        List<BaseWxFansStatusVo> baseWxFansStatusVos = systemCustomerInfo.getFansStatusVos();

                        if(CollUtil.isNotEmpty(baseWxFansStatusVos)){

                            List<PureFansStatus> insertPureFansStatusList = new ArrayList<>();
                            for (BaseWxFansStatusVo baseWxFansStatusVo : baseWxFansStatusVos){
                                PureFansStatus pureFansStatus = new PureFansStatus();
                                BeanUtils.copyProperties(baseWxFansStatusVo, pureFansStatus);
                                pureFansStatus.setMainId(id);

                                insertPureFansStatusList.add(pureFansStatus);
                            }

                            pureFansStatusService.saveBatch(insertPureFansStatusList);
                        }



                        List<NickInfo> nickInfos = systemCustomerInfo.getNickInfoList();
                        if (CollUtil.isNotEmpty(nickInfos)){

                            List<PureMemberNickInfo> pureMemberNickInfos = new ArrayList<>();

                            for (NickInfo nickInfo : nickInfos){
                                PureMemberNickInfo pureMemberNickInfo = new PureMemberNickInfo();
                                BeanUtils.copyProperties(nickInfo, pureMemberNickInfo);
                                pureMemberNickInfo.setMainId(id);
                                pureMemberNickInfos.add(pureMemberNickInfo);
                            }

                            if(pureMemberNickInfos.size()>0){
                                pureMemberNickInfoService.saveBatch(pureMemberNickInfos);
                            }
                        }
                    }


                }



                if(insertPureMembers.size()>0){
                    pureMemberService.saveBatch(insertPureMembers);
                }

                if(updatePureMembers.size()>0){
                    pureMemberService.saveOrUpdateBatch(updatePureMembers);

                }
            }

            resMap.put("nextId",nextId);
            resMap.put("updateTime",updateTime);
            resMap.put("isNext",isNext);
            return resMap;
        }

    }

    private Map<String, Object> saveZaMember(Long nextId,Date startDate,Date endDate) throws Exception {
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
        request.setViewId(100000386L);
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
            log.info(response.getBody());

            List<SystemCustomerInfo> systemCustomerInfos = response.getResult();

            List<ZaMember> insertZaMembers = new ArrayList<>();

            List<ZaMember> updateZaMembers = new ArrayList<>();

            Boolean isNext = false;
            Date updateTime = null;

            if (CollUtil.isNotEmpty(systemCustomerInfos)){

                isNext = true;

                for (SystemCustomerInfo systemCustomerInfo : systemCustomerInfos){
                    Long id = systemCustomerInfo.getId();
                    nextId = id;
                    updateTime = systemCustomerInfo.getUpdateTime();
                    QueryWrapper<ZaMember> memberQuery = new QueryWrapper<>();
                    memberQuery.eq("id",String.valueOf(id));
                    ZaMember existZaMember = zaMemberService.getOne(memberQuery);

                    ZaMember zaMember = new ZaMember();
                    BeanUtils.copyProperties(systemCustomerInfo, zaMember);

                    /*insertMembers.add(member);*/
                    if(existZaMember != null){
                        //更新逻辑
                        updateZaMembers.add(zaMember);
                    }else {
                        //新增逻辑
                        insertZaMembers.add(zaMember);

                        List<CustomerCardReceiveInfo> customerCardReceiveInfos = systemCustomerInfo.getCardReceiveInfoList();

                        List<ZaCardReceiveInfo> insertZaCardReceiveInfos = new ArrayList<>();

                        if(CollUtil.isNotEmpty(customerCardReceiveInfos)){

                            for (CustomerCardReceiveInfo customerCardReceiveInfo : customerCardReceiveInfos){
                                ZaCardReceiveInfo zaCardReceiveInfo = new ZaCardReceiveInfo();
                                BeanUtils.copyProperties(customerCardReceiveInfo, zaCardReceiveInfo);
                                zaCardReceiveInfo.setMainId(id);
                                insertZaCardReceiveInfos.add(zaCardReceiveInfo);
                            }

                            zaCardReceiveInfoService.saveBatch(insertZaCardReceiveInfos);
                        }

                        List<BaseWxFansStatusVo> baseWxFansStatusVos = systemCustomerInfo.getFansStatusVos();

                        if(CollUtil.isNotEmpty(baseWxFansStatusVos)){

                            List<ZaFansStatus> insertZaFansStatusList = new ArrayList<>();
                            for (BaseWxFansStatusVo baseWxFansStatusVo : baseWxFansStatusVos){
                                ZaFansStatus zaFansStatus = new ZaFansStatus();
                                BeanUtils.copyProperties(baseWxFansStatusVo, zaFansStatus);
                                zaFansStatus.setMainId(id);

                                insertZaFansStatusList.add(zaFansStatus);
                            }

                            zaFansStatusService.saveBatch(insertZaFansStatusList);
                        }



                        List<NickInfo> nickInfos = systemCustomerInfo.getNickInfoList();
                        if (CollUtil.isNotEmpty(nickInfos)){

                            List<ZaMemberNickInfo> zaMemberNickInfos = new ArrayList<>();

                            for (NickInfo nickInfo : nickInfos){
                                ZaMemberNickInfo zaMemberNickInfo = new ZaMemberNickInfo();
                                BeanUtils.copyProperties(nickInfo, zaMemberNickInfo);
                                zaMemberNickInfo.setMainId(id);
                                zaMemberNickInfos.add(zaMemberNickInfo);
                            }

                            if(zaMemberNickInfos.size()>0){
                                zaMemberNickInfoService.saveBatch(zaMemberNickInfos);
                            }
                        }
                    }


                }



                if(insertZaMembers.size()>0){
                    zaMemberService.saveBatch(insertZaMembers);
                }

                if(updateZaMembers.size()>0){
                    zaMemberService.saveOrUpdateBatch(updateZaMembers);
                }
            }

            resMap.put("nextId",nextId);
            resMap.put("updateTime",updateTime);
            resMap.put("isNext",isNext);
            return resMap;
        }

    }

    private Map<String, Object> savePureStoreMember(Long nextId,Date startDate,Date endDate,Long shopId) throws Exception {
        Map resMap = new HashMap();
        ShopActiveCustomerListSyncRequest request = new ShopActiveCustomerListSyncRequest();

        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());

        QueryWrapper<Token> tokenQuery = new QueryWrapper<>();
        tokenQuery.eq("name","nascent");
        List<Token> tokens = tokenService.list(tokenQuery);
        request.setAccessToken(tokens.get(0).getToken());
        request.setStartTime(startDate);
        request.setEndTime(endDate);
        request.setNextId(nextId);
        request.setPageSize(50);
        request.setShopId(shopId);
        ApiClient client = new ApiClientImpl(request);
        ShopActiveCustomerListSyncResponse response = client.execute(request);

        if("60001".equals(response.getCode())){
            UpdateWrapper<Token> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("name","nascent").set("token",tokenService.getToken());
            tokenService.update(updateWrapper);
            resMap.put("nextId",nextId);
            resMap.put("updateTime",startDate);
            resMap.put("isNext",true);
            return resMap;
        }else if("200".equals(response.getCode())){
            log.info(response.getBody());
            List<ShopActiveCustomerInfo> systemCustomerInfos = response.getResult();

            Boolean isNext = false;
            Date updateTime = null;

            if(CollUtil.isNotEmpty(systemCustomerInfos)){
                isNext = true;
                List<ShopActiveCustomer> shopActiveCustomerInfos = new ArrayList<>();
                for (ShopActiveCustomerInfo shopActiveCustomerInfo : systemCustomerInfos){
                    Long id = shopActiveCustomerInfo.getId();
                    nextId = id;
                    updateTime = shopActiveCustomerInfo.getUpdateTime();
                    ShopActiveCustomer shopActiveCustomer = new ShopActiveCustomer();
                    BeanUtils.copyProperties(shopActiveCustomerInfo,shopActiveCustomer);
                    shopActiveCustomer.setShopId(shopId);

                    QueryWrapper<ShopActiveCustomer> shopActiveCustomerQuery = new QueryWrapper<>();
                    shopActiveCustomerQuery.eq("id",id);
                    ShopActiveCustomer existObj = shopActiveCustomerService.getOne(shopActiveCustomerQuery);
                    if (existObj != null){
                        UpdateWrapper<ShopActiveCustomerNick> shopActiveCustomerNickUpdate = new UpdateWrapper<>();
                        shopActiveCustomerNickUpdate.eq("mainid",id);
                        shopActiveCustomerNickService.remove(shopActiveCustomerNickUpdate);

                        List<NickPlatform> nickPlatforms = shopActiveCustomerInfo.getNickInfoList();

                        if(CollUtil.isNotEmpty(nickPlatforms)){
                            List<ShopActiveCustomerNick> shopActiveCustomerNicks = new ArrayList<>();
                            for (NickPlatform nickPlatform : nickPlatforms){

                                if(shopId.equals(100149661L) && nickPlatform.getPlatform().equals(111)){
                                    ShopActiveCustomerNick shopActiveCustomerNick = new ShopActiveCustomerNick();
                                    BeanUtils.copyProperties(nickPlatform,shopActiveCustomerNick);
                                    shopActiveCustomerNick.setMainId(id);
                                    shopActiveCustomerNicks.add(shopActiveCustomerNick);
                                }
                            }

                            shopActiveCustomerNickService.saveBatch(shopActiveCustomerNicks);
                        }

                    }else {
                        shopActiveCustomerInfos.add(shopActiveCustomer);

                        List<NickPlatform> nickPlatforms = shopActiveCustomerInfo.getNickInfoList();

                        if(CollUtil.isNotEmpty(nickPlatforms)){
                            List<ShopActiveCustomerNick> shopActiveCustomerNicks = new ArrayList<>();
                            for (NickPlatform nickPlatform : nickPlatforms){
                                if(shopId.equals(100149661L) && "111".equals(nickPlatform.getPlatform())){
                                    ShopActiveCustomerNick shopActiveCustomerNick = new ShopActiveCustomerNick();
                                    BeanUtils.copyProperties(nickPlatform,shopActiveCustomerNick);
                                    shopActiveCustomerNick.setMainId(id);
                                    shopActiveCustomerNicks.add(shopActiveCustomerNick);
                                }
                            }

                            shopActiveCustomerNickService.saveBatch(shopActiveCustomerNicks);
                        }


                        List<CustomerWechatInfo> customerWechatInfos = shopActiveCustomerInfo.getCustomerWechatInfoList();
                        if (CollUtil.isNotEmpty(customerWechatInfos)){
                            List<ShopActiveCustomerWeChat> shopActiveCustomerWeChats = new ArrayList<>();
                            for (CustomerWechatInfo customerWechatInfo : customerWechatInfos){
                                ShopActiveCustomerWeChat shopActiveCustomerWeChat = new ShopActiveCustomerWeChat();
                                BeanUtils.copyProperties(customerWechatInfo,shopActiveCustomerWeChat);
                                shopActiveCustomerWeChat.setMainId(id);

                                shopActiveCustomerWeChats.add(shopActiveCustomerWeChat);
                            }

                            shopActiveCustomerWeChat.saveBatch(shopActiveCustomerWeChats);
                        }

                        List<ShopActiveCustomerCardInfo> shopActiveCustomerCardInfos = shopActiveCustomerInfo.getCustomerCardInfoList();
                        if (CollUtil.isNotEmpty(shopActiveCustomerCardInfos)){
                            List<ShopActiveCustomerCard> shopActiveCustomerCards = new ArrayList<>();
                            for (ShopActiveCustomerCardInfo shopActiveCustomerCardInfo : shopActiveCustomerCardInfos){
                                ShopActiveCustomerCard shopActiveCustomerCard = new ShopActiveCustomerCard();
                                BeanUtils.copyProperties(shopActiveCustomerCardInfo,shopActiveCustomerCard);
                                shopActiveCustomerCard.setMainId(id);
                                shopActiveCustomerCards.add(shopActiveCustomerCard);
                            }
                            shopActiveCustomerCardService.saveBatch(shopActiveCustomerCards);
                        }
                    }
                }

                shopActiveCustomerService.saveBatch(shopActiveCustomerInfos);


            }
            resMap.put("nextId", nextId);
            resMap.put("updateTime", updateTime);
            resMap.put("isNext", isNext);
        }
        return resMap;
    }

}
