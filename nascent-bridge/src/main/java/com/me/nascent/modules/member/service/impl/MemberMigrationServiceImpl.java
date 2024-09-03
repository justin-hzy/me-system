package com.me.nascent.modules.member.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.nascent.common.config.NascentConfig;
import com.me.nascent.modules.member.entity.PureCardReceiveInfo;
import com.me.nascent.modules.member.entity.PureMember;
import com.me.nascent.modules.member.service.MemberMigrationService;
import com.me.nascent.modules.member.service.PureCardReceiveInfoService;
import com.me.nascent.modules.member.service.PureMemberService;
import com.me.nascent.modules.member.service.TransMemberService;
import com.me.nascent.modules.reorder.entity.ReFund;
import com.me.nascent.modules.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CardReceiveInfo;
import com.nascent.ecrp.opensdk.domain.customer.CustomerSaveInfo;
import com.nascent.ecrp.opensdk.request.customer.BatchCustomerSaveRequest;
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

@Service
@AllArgsConstructor
@Slf4j
public class MemberMigrationServiceImpl implements MemberMigrationService {

    private TransMemberService transMemberService;

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PureMemberService pureMemberService;

    private PureCardReceiveInfoService pureCardReceiveInfoService;

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
}
