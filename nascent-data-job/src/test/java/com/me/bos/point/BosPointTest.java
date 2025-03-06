package com.me.bos.point;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.core.JsonResult;
import com.me.modules.bos.point.service.BosMemberService;
import com.me.modules.bos.point.service.BosPointService;
import com.me.modules.nascent.point.entity.PointLog;
import com.me.modules.nascent.point.entity.PointSum;
import com.me.modules.nascent.point.service.PointLogService;
import com.me.modules.nascent.point.service.PointSumService;
import com.me.modules.nascent.store.entity.OffStoreInfo;
import com.me.modules.nascent.store.service.OffStoreInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
@Slf4j
public class BosPointTest {

    @Autowired
    BosPointService bosPointService;

    @Autowired
    PointLogService pointLogService;

    @Autowired
    BosMemberService bosMemberService;

    @Autowired
    OffStoreInfoService offStoreInfoService;

    @Autowired
    PointSumService pointSumService;

    @Test
    public void putBosPointTest(){

        //初始化线下店铺中间表信息
        List<OffStoreInfo> offStoreInfos = offStoreInfoService.list();
        //
        List<String> offProviderGuids = new ArrayList<>();

        for (OffStoreInfo offStoreInfo : offStoreInfos){
            offProviderGuids.add(offStoreInfo.getProviderGuid());
        }
        log.info(offProviderGuids.size()+"");

        //查询积分中间表
        QueryWrapper<PointLog> onlinePointLogQuery = new QueryWrapper<>();
        onlinePointLogQuery.notIn("provider_guid",offProviderGuids);
        onlinePointLogQuery.eq("is_bos","0");
        List<PointLog> onlinePointLogs = pointLogService.list(onlinePointLogQuery);


        QueryWrapper<PointLog> notFromPointLogQuery = new QueryWrapper<>();
        notFromPointLogQuery.isNull("provider_guid");
        notFromPointLogQuery.eq("is_bos","0");
        List<PointLog> notFromPointLogs = pointLogService.list(notFromPointLogQuery);

        List<PointLog> pointLogs = new ArrayList<>();
        pointLogs.addAll(onlinePointLogs);
        pointLogs.addAll(notFromPointLogs);

        /*List<String> list = new ArrayList<>();
        for (PointLog pointLog : onlinePointLogs){
            if (!list.contains(pointLog.getProviderGUID())){
                list.add(pointLog.getProviderGUID());
            }
        }

        for (PointLog pointLog : notFromPointLogs){
            if (!list.contains(pointLog.getProviderGUID())){
                list.add(pointLog.getProviderGUID());
            }
        }
        log.info(list.toString());
        */



        JsonResult countJS = bosMemberService.getBosMemberCount();

        if("200".equals(countJS.getCode())){
            Integer count = (Integer) countJS.getData();
            log.info("伯俊线下会员总记录数:"+count);
            JsonResult offMembersJS = bosMemberService.getBosOffMembers(count);
            if ("200".equals(offMembersJS.getCode())) {
                List<String> mobilList = (List<String>) offMembersJS.getData();
                log.info("查询伯俊线下会员总人数为"+mobilList.size());


                for (PointLog pointLog : pointLogs){
                    String nasOuid = pointLog.getNasOuid();

                    if(mobilList.contains(nasOuid)){
                        if("13761522707".equals(nasOuid)){
                            log.info("积分="+pointLog.getPoint());
                            JsonResult result = bosPointService.putBosPoint(pointLog);
                            if("200".equals(result.getCode())){
                                JSONObject dataJson = (JSONObject) result.getData();
                                Integer objectId = dataJson.getInteger("objectId");
                                log.info("objectId="+objectId);

                                //提交操作
                                bosPointService.submitBosPoint(objectId);

                                //更新状态
                                UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                                pointLogUpdate.eq("id",pointLog.getId());
                                pointLogUpdate.set("is_bos","1");
                                pointLogService.update(pointLogUpdate);
                            }else {
                                //更新状态 伯俊调整积分失败
                                UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                                pointLogUpdate.eq("id",pointLog.getId());
                                pointLogUpdate.set("is_bos","3");
                                pointLogService.update(pointLogUpdate);
                            }
                        }
                    }else {
                        //更新状态 数据异常跟伯俊数据不匹配 例如 南讯为线下会员， 伯俊不是线上会员
                        UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                        pointLogUpdate.eq("id",pointLog.getId());
                        pointLogUpdate.set("is_bos","2");
                        pointLogService.update(pointLogUpdate);
                    }
                }


            }
        }




        /*
        if("200".equals(countJS.getCode())){
            Integer count = (Integer) countJS.getData();

            JsonResult offMembersJS = bosMemberService.getBosOffMembers(count);
            if ("200".equals(offMembersJS.getCode())){
                List<String> mobilList = (List<String>) offMembersJS.getData();

                //log.info("mobilList="+mobilList.toString());

                QueryWrapper<PointLog> pointLogQuery = new QueryWrapper();
                pointLogQuery.eq("is_bos",0);

                List<PointLog> pointLogs = pointLogService.list(pointLogQuery);

                for (PointLog pointLog : pointLogs){
                    String nasOuid = pointLog.getNasOuid();
                    if(mobilList.contains(nasOuid)){
                        if("13350341669".equals(nasOuid)){
                            JsonResult result = bosPointService.putBosPoint(pointLog);
                            if("200".equals(result.getCode())){
                                JSONObject dataJson = (JSONObject) result.getData();
                                Integer objectId = dataJson.getInteger("objectId");
                                log.info("objectId="+objectId);

                                //提交操作
                                bosPointService.submitBosPoint(objectId);

                                //更新状态
                                UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                                pointLogUpdate.eq("id",pointLog.getId());
                                pointLogUpdate.set("is_bos","1");
                                pointLogService.update(pointLogUpdate);
                            }else {
                                //更新状态 伯俊调整积分失败
                                UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                                pointLogUpdate.eq("id",pointLog.getId());
                                pointLogUpdate.set("is_bos","3");
                                pointLogService.update(pointLogUpdate);
                            }
                        }
                    }else {
                        //更新状态 数据异常跟伯俊数据不匹配 例如 南讯为线下会员， 伯俊不是线上会员
                        UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                        pointLogUpdate.eq("id",pointLog.getId());
                        pointLogUpdate.set("is_bos","2");
                        pointLogService.update(pointLogUpdate);
                    }
                }
            }
        }*/
    }


    @Test
    void initOnlinePoint(){

        JsonResult countJS = bosMemberService.getBosMemberCount();

        List<Map<String,String>> pointList = null;

        if("200".equals(countJS.getCode())) {
            Integer count = (Integer) countJS.getData();
            log.info("伯俊会员总记录数:" + count);
            JsonResult offPointJS = bosMemberService.getBosOffPoint(count);
            if ("200".equals(offPointJS.getCode())) {
                pointList = (List<Map<String,String>>) offPointJS.getData();
                log.info("伯俊POS会员总记录数:" + pointList.size());
                //log.info(pointList.toString());
            }
        }



        //初始化 point_sum表数据
        /*while (iterator.hasNext()){
            Map<String,String> pointMap = iterator.next();
            String cardNo = pointMap.get("cardNo");
            PointSum pointSum = new PointSum();
            pointSum.setNasouid(cardNo);
            pointSumService.save(pointSum);
        }*/



        QueryWrapper<PointSum> pointSumQuery = new QueryWrapper<>();
        pointSumQuery.eq("is_bos","0");
        List<PointSum> pointSums = pointSumService.list(pointSumQuery);
        if (CollUtil.isNotEmpty(pointSums)) {

            for (PointSum pointSum : pointSums) {
                BigDecimal nascentPoint = pointSum.getPoint();
                String nasouid = pointSum.getNasouid();



                if(nascentPoint != null ){

                    int compareZeroResult = nascentPoint.compareTo(BigDecimal.ZERO);

                    if (compareZeroResult>0){
                        for (Map<String,String> bosPointMap : pointList){
                            //log.info(bosPointMap.toString());



                            String cardNo = bosPointMap.get("cardNo");
                            if(bosPointMap.get("point") != null){
                                BigDecimal bosPoint = new BigDecimal(bosPointMap.get("point"));

                                if (bosPoint != null){
                                    if(nasouid.equals(cardNo)){
                                        //log.info("nasouid="+nasouid+",cardNo="+cardNo);
                                        int compareResult = nascentPoint.compareTo(bosPoint);
                                        if(compareResult <0){
                                            log.info(cardNo+"的南讯积分小于伯俊积分");
                                        }else if(compareResult>0){
                                            log.info(cardNo+"的南讯积分大于伯俊积分，可初始化");

                                            BigDecimal addBosPoint = nascentPoint.subtract(bosPoint);

                                            PointLog pointLog  = new PointLog();
                                            pointLog.setPoint(addBosPoint);
                                            pointLog.setType(0);
                                            pointLog.setNasOuid(pointSum.getNasouid());

                                            log.info("将要同步的积分为:"+addBosPoint);

                                            UpdateWrapper<PointSum> pointSumUpdate = new UpdateWrapper<>();
                                            pointSumUpdate.set("add_point",addBosPoint);
                                            pointSumUpdate.eq("nas_ouid",nasouid);
                                            pointSumService.update(pointSumUpdate);

                                            /*JsonResult result = bosPointService.putBosPoint(pointLog);
                                            if("200".equals(result.getCode())){
                                                JSONObject dataJson = (JSONObject) result.getData();
                                                Integer objectId = dataJson.getInteger("objectId");
                                                log.info("objectId="+objectId);

                                                //提交操作
                                                bosPointService.submitBosPoint(objectId);

                                                //更新状态
                                                UpdateWrapper<PointSum> pointSumUpdate = new UpdateWrapper<>();
                                                pointSumUpdate.eq("nas_ouid",nasouid);
                                                pointSumUpdate.set("is_bos","1");
                                                pointSumService.update(pointSumUpdate);
                                            }else {
                                                //更新状态 伯俊调整积分失败
                                                UpdateWrapper<PointSum> pointSumUpdate = new UpdateWrapper<>();
                                                pointSumUpdate.eq("nas_ouid",nasouid);
                                                pointSumUpdate.set("is_bos","2");
                                                pointSumService.update(pointSumUpdate);
                                            }*/

                                        }else {
                                            log.info(cardNo+"的南讯积分等于伯俊积分");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }






        /*while (iterator.hasNext()){
            Map<String,String> pointMap = iterator.next();
            log.info(pointMap.toString());

            String cardNo = pointMap.get("cardNo");
            BigDecimal bosPoint = new BigDecimal(pointMap.get("point"));





            List<PointSum> pointSums = pointSumService.list();
            if (CollUtil.isNotEmpty(pointSums)){

                for (PointSum pointSum : pointSums){

                }




                int compareResult = nascentPoint.compareTo(bosPoint);
                if(compareResult <0){
                    log.info(cardNo+"的南讯积分小于伯俊积分");
                }else if(compareResult>0){
                    BigDecimal addBosPoint = nascentPoint.subtract(bosPoint);

                    PointLog pointLog  = new PointLog();
                    pointLog.setPoint(addBosPoint);
                    pointLog.setType(0);
                    pointLog.setNasOuid(pointSum.getNasouid());

                    JsonResult result = bosPointService.putBosPoint(pointLog);
                    if("200".equals(result.getCode())){
                        JSONObject dataJson = (JSONObject) result.getData();
                        Integer objectId = dataJson.getInteger("objectId");
                        log.info("objectId="+objectId);

                        //提交操作
                        bosPointService.submitBosPoint(objectId);

                        //更新状态
                        UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                        pointLogUpdate.eq("id",pointLog.getId());
                        pointLogUpdate.set("is_bos","1");
                        pointLogService.update(pointLogUpdate);
                    }else {
                        //更新状态 伯俊调整积分失败
                        UpdateWrapper<PointLog> pointLogUpdate = new UpdateWrapper<>();
                        pointLogUpdate.eq("id",pointLog.getId());
                        pointLogUpdate.set("is_bos","3");
                        pointLogService.update(pointLogUpdate);
                    }

                }else {
                    log.info(cardNo+"的南讯积分等于伯俊积分");
                }
            }else {

            }
        }*/
    }
}
