package com.me.modules.nascent.point.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.NascentConfig;
import com.me.modules.nascent.point.entity.PointLog;
import com.me.modules.nascent.point.service.PointLogService;
import com.me.modules.nascent.point.service.TransPointLogService;
import com.me.modules.nascent.token.service.TokenService;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClient;
import com.nascent.ecrp.opensdk.core.executeClient.ApiClientImpl;
import com.nascent.ecrp.opensdk.domain.customer.CustomerNickPlatformInfo;
import com.nascent.ecrp.opensdk.domain.point.PointLogDetailInfo;
import com.nascent.ecrp.opensdk.request.point.PointLogInfoQueryRequest;
import com.nascent.ecrp.opensdk.response.point.PointLogInfoQueryResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class TransPointLogServiceImpl implements TransPointLogService {

    private NascentConfig nascentConfig;

    private TokenService tokenService;

    private PointLogService pointLogService;


    @Override
    public Map<String,Object> transPointLog(String startDateStr, String endDateStr, Long nextId) throws Exception {

        Long oriNextId= nextId;

        HashMap<String,Object> respMap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startTime = sdf.parse(startDateStr);

        Date endTime = sdf.parse(endDateStr);

        String accessToken = tokenService.getBtnToken();

        PointLogInfoQueryRequest request = new PointLogInfoQueryRequest();
        request.setServerUrl(nascentConfig.getBtnServerUrl());
        request.setAppKey(nascentConfig.getBtnAppKey());
        request.setAppSecret(nascentConfig.getBtnAppSerect());
        request.setGroupId(nascentConfig.getBtnGroupID());
        request.setAccessToken(accessToken);
        request.setPageSize(1000);

        request.setStartTime(startTime);
        request.setEndTime(endTime);

        request.setStartId(nextId);
        request.setIntegralAccount("pcode-216062");
        //request.setProviderGUID(providerGUID);
        request.setIsReturnCount(false);
        ApiClient client = new ApiClientImpl(request);
        log.info("client="+client.toString());
        PointLogInfoQueryResponse response = client.execute(request);
            //log.info("body="+response.getBody());
            log.info("requestid="+response.getRequestId()+",msg="+response.getMsg());
        if(response.getSuccess()){
            //log.info("requestId="+response.getRequestId());

            List<PointLogDetailInfo> pointLogDetailInfos = response.getResult();

            List<PointLog> pointLogs = new ArrayList<>();

            if(CollUtil.isNotEmpty(pointLogDetailInfos)){
                respMap.put("isNext",true);
                for (PointLogDetailInfo pointLogDetailInfo : pointLogDetailInfos){

                    nextId = pointLogDetailInfo.getNextId();

                    String providerGUID = pointLogDetailInfo.getProviderGUID();

                    List<CustomerNickPlatformInfo> customerNickPlatformInfos = pointLogDetailInfo.getNickPlatformList();

                    if(CollUtil.isNotEmpty(customerNickPlatformInfos)){
                        for (CustomerNickPlatformInfo customerNickPlatformInfo : customerNickPlatformInfos){
                            Integer platForm = customerNickPlatformInfo.getPlatform();
                            if(platForm == 0){
                                log.info("nasOuid="+customerNickPlatformInfo.getNasOuid()+",point="+pointLogDetailInfo.getPoint()+",createTime="+pointLogDetailInfo.getCreateTime());

                                QueryWrapper<PointLog> pointLogQuery = new QueryWrapper<>();
                                pointLogQuery.lambda().eq(PointLog::getId,pointLogDetailInfo.getId());

                                List<PointLog> existLogs = pointLogService.list(pointLogQuery);

                                if(CollUtil.isEmpty(existLogs)){
                                    PointLog pointLog = new PointLog();
                                    pointLog.setId(pointLogDetailInfo.getId());
                                    Integer type = pointLogDetailInfo.getType();
                                    pointLog.setType(type);
                                    pointLog.setPoint(pointLogDetailInfo.getPoint());
                                    pointLog.setNasOuid(customerNickPlatformInfo.getNasOuid());
                                    pointLog.setPointCreateTime(pointLogDetailInfo.getCreateTime());
                                    pointLog.setRequestId(response.getRequestId());
                                    pointLog.setProviderGUID(providerGUID);
                                    pointLog.setIsBos("0");

                                    pointLogService.save(pointLog);
                                }
                            }
                        }
                    }else {
                            log.info("没有昵称");
                    }


                /*if("713212".equals(nick) || "邢庆连".equals(nick) || "1@#CLXK+2fhRc7r2upXluqmVA8QCC5KYWYwsDXOjEdjpVGitnkB3ev35+ZjpHj5dyYNTZgS".equals(nick) || "13785013750".equals(nick) || "60336092613".equals(nascentUserID)){
                    log.info("nick="+nick);
                    log.info("point="+pointLogDetailInfo.getPoint());
                    log.info("线下积分找到，南讯id = "+pointLogDetailInfo.getNascentUserID()+",providerGUID = "+providerGUID+",guid="+guid+",payGuid="+payGuid+",id="+id);

                    for (CustomerNickPlatformInfo customerNickPlatformInfo : customerNickPlatformInfos){
                        log.info("nasOuid="+customerNickPlatformInfo.getNasOuid()+",platform="+customerNickPlatformInfo.getPlatform()+",outNick="+customerNickPlatformInfo.getOutNick());
                    }
                }*/
                }
            }else {
                respMap.put("isNext",false);
            }


            //log.info(pointLogs.toString());
            //pointLogService.saveBatch(pointLogs);

            respMap.put("nextId",nextId);
        }else {
            respMap.put("isNext",true);
            respMap.put("nextId",oriNextId);
        }
        return respMap;
    }
}
