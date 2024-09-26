package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.MemberTong;
import com.me.nascent.modules.member.entity.PureCardReceiveInfo;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.service.*;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.token.entity.Token;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.CustomerSaveInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class MemberMigrationServiceImpl implements MemberMigrationService {

    private TransMemberService transMemberService;

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberService pureMemberService;

    private PureCardReceiveInfoService pureCardReceiveInfoService;

    private MemberTongService memberTongService;

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
    public void transPureStoreMemberByRange(Date startDate, Date endDate,Long shopId) throws Exception {
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
    public void putPureMember() throws Exception {
        BatchCustomerSaveRequest request = new BatchCustomerSaveRequest();
        request.setServerUrl(nascentConfig.getServerUrl());
        request.setAppKey(nascentConfig.getAppKey());
        request.setAppSecret(nascentConfig.getAppSerect());
        request.setGroupId(nascentConfig.getGroupID());
        request.setAccessToken(tokenService.getToken());

        List<PureMember> pureMembers = pureMemberService.list();

        int batchSize = 100; // 每次处理的数据量
        int totalSize = pureMembers.size(); // 总数据量

        int loopCount = (int) Math.ceil((double) totalSize / batchSize); // 需要循环的次数

        for (int i = 0; i < loopCount; i++) {
            int start = i * batchSize; // 开始索引
            int end = Math.min((i + 1) * batchSize, totalSize);

            List<PureMember> batchList = pureMembers.subList(start, end);
            log.info("batchList=" + batchList.toString());

            List<CustomerSaveInfo> customerSaveInfos = new ArrayList<>();

            for (PureMember pureMember :  batchList){
                CustomerSaveInfo customerSaveInfo = new CustomerSaveInfo();
                BeanUtils.copyProperties(pureMember,customerSaveInfo);

                //List<CardReceiveInfo> cardReceiveInfos = customerSaveInfo.getCardReceiveInfoList();

                List<CardReceiveInfo> cardReceiveInfos = new ArrayList<>();

                QueryWrapper<PureCardReceiveInfo> pureCardReceiveInfoQuery = new QueryWrapper<>();
                pureCardReceiveInfoQuery.eq("mainid",pureMember.getId());


                List<PureCardReceiveInfo> pureCardReceiveInfos  = pureCardReceiveInfoService.list(pureCardReceiveInfoQuery);
                if (CollUtil.isNotEmpty(pureCardReceiveInfos)){
                    for (PureCardReceiveInfo pureCardReceiveInfo : pureCardReceiveInfos){
                        CardReceiveInfo cardReceiveInfo = new CardReceiveInfo();
                        BeanUtils.copyProperties(pureCardReceiveInfo,cardReceiveInfo);
                        cardReceiveInfos.add(cardReceiveInfo);
                    }
                }
                customerSaveInfos.add(customerSaveInfo);
            }

            ApiClient client = new ApiClientImpl(request);
            BatchCustomerSaveResponse response = client.execute(request);

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
}
