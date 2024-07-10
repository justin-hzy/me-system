package com.me.modules.fl.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.annotation.Elk;
import com.me.modules.fl.entity.*;
import com.me.modules.fl.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
@AllArgsConstructor
public class FlJob {


    private FlReOrderReqLogService flReOrderReqLogService;

    private FlOrderReqLogService flOrderReqLogService;

    private FuLunHttpService fuLunHttpService;

    private FlTrfOrderReqLogService flTrfOrderReqLogService;

    private FlConsOrderReqLogService flConsOrderReqLogService;

    private FlReConsOrderReqLogService flReConsOrderReqLogService;

    private FlPurInOrderReqLogService flPurInOrderReqLogService;

    private FlRePurOrderReqLogService flRePurOrderReqLogService;

//    private FlSetOrderReqLogService flSetOrderReqLogService;
//
//    private FlDismantleReqLogService flDismantleReqLogService;
//
//    private FuLunInterfaceService fuLunInterfaceService;
//
//    private FlTransCodeReqLogService flTransCodeReqLogService;

    private FlChannelLockReqLogService flChannelLockReqLogService;

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void orderSend(){
        log.info("执行 销售出库单请求发送 开始!");

        QueryWrapper<FlOrderReqLog> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ne("status",1).ne("status",3);
        List<FlOrderReqLog> flOrderReqLogs = flOrderReqLogService.list(queryWrapper1);

        for(FlOrderReqLog flOrderReqLog :flOrderReqLogs) {

            String requestId = flOrderReqLog.getRequestId();
            String apiId = flOrderReqLog.getApiId();
            String params = flOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            //該值為True以有庫存為主，若訂單無庫存則刪除該訂單，預設為false
            JSONObject obj = JSONObject.parseObject(params);
            obj.put("require_stock","false");
            params = obj.toJSONString();
            //

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理返回信息
            String code = apiRes.getString("code");

            if("0".equals(code) || "1".equals(code)){
                UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                flOrderReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                Integer failCount = flOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flOrderReqLogService.update(updateWrapper);
                }
                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flOrderReqLogService.update(updateWrapper);
                }
                log.info("接口执行异常!");
            }
        }
        log.info("执行 销售出库单请求发送 结束!");
    }

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void returnOrderSend(){
        log.info("执行 销售退货单请求发送 开始!");
        QueryWrapper<FlReOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1);
        List<FlReOrderReqLog> flReOrderReqLogs = flReOrderReqLogService.list(queryWrapper);

        for(FlReOrderReqLog flReOrderReqLog : flReOrderReqLogs){
            String requestId = flReOrderReqLog.getRequestId();
            String apiId = flReOrderReqLog.getApiId();
            String params = flReOrderReqLog.getParams();

            //测试需要,测试后注释掉相关代码
            JSONObject obj = JSONObject.parseObject(params);
            //obj.put("sender_name","测试");
            params = obj.toJSONString();
            //测试需要,测试后注释掉相关代码

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);


            //处理返回信息
            String code = apiRes.getString("code");
            if("0".equals(code)){
                UpdateWrapper<FlReOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status 1:成功
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                flReOrderReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                Integer failCount = flReOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlReOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status 1:成功 2:失败
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flReOrderReqLogService.update(updateWrapper);
                    log.info("接口执行异常!");
                }
                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlReOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status 1:成功 2:失败
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flReOrderReqLogService.update(updateWrapper);
                    log.info("接口执行异常!");
                }
            }
        }
        log.info("执行 销售退货单请求发送 结束!");
    }

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void transferOrderSend(){
        log.info("执行 仓内调拨单请求发送 开始!");
        QueryWrapper<FlTrfOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);

        List<FlTrfOrderReqLog> FlTrfOrderReqLogs = flTrfOrderReqLogService.list(queryWrapper);

        for(FlTrfOrderReqLog flTrfOrderReqLog : FlTrfOrderReqLogs){
            String requestId = flTrfOrderReqLog.getRequestId();
            String apiId = flTrfOrderReqLog.getApiId();
            String params = flTrfOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //JSONObject apiRes = new JSONObject();

            //处理返回信息
            String code = apiRes.getString("code");

            if("0".equals(code)){
                UpdateWrapper<FlTrfOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");

                flTrfOrderReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                Integer failCount = flTrfOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlTrfOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status 1:成功 2:失败
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flTrfOrderReqLogService.update(updateWrapper);
                    log.info("接口执行异常!");
                }
                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlTrfOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status 1:成功 2:失败
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flTrfOrderReqLogService.update(updateWrapper);
                    log.info("接口执行异常!");
                }

            }
        }
        log.info("执行 仓内调拨单请求发送 结束!");
    }


    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void consOrderSend(){
        log.info("执行 寄售单请求发送 开始!");
        QueryWrapper<FlConsOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);
        List<FlConsOrderReqLog> flConsOrderReqLogs = flConsOrderReqLogService.list(queryWrapper);
        for (FlConsOrderReqLog flConsOrderReqLog : flConsOrderReqLogs){
            String requestId = flConsOrderReqLog.getRequestId();
            String apiId = flConsOrderReqLog.getApiId();
            String params = flConsOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            //测试需要,测试后注释掉相关代码
            JSONObject obj = JSONObject.parseObject(params);
            //obj.put("customer_name","测试");
            params = obj.toJSONString();
            //测试需要,测试后注释掉相关代码

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理返回信息
            String code = apiRes.getString("code");
            if("0".equals(code)){
                UpdateWrapper<FlConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                flConsOrderReqLogService.update(updateWrapper);
            }else {
                Integer failCount = flConsOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flConsOrderReqLogService.update(updateWrapper);
                }
                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flConsOrderReqLogService.update(updateWrapper);
                }
                log.info("接口执行异常!");
            }
        }
        log.info("执行 寄售单请求发送 结束!");
    }

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void reConsOrderSend(){
        log.info("执行 寄售退货单请求发送 开始!");
        QueryWrapper<FlReConsOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);
        List<FlReConsOrderReqLog> flReConsOrderReqLogs = flReConsOrderReqLogService.list(queryWrapper);
        for (FlReConsOrderReqLog flReConsOrderReqLog : flReConsOrderReqLogs){
            String requestId = flReConsOrderReqLog.getRequestId();
            String apiId = flReConsOrderReqLog.getApiId();
            String params = flReConsOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            //测试需要,测试后注释掉相关代码
            JSONObject obj = JSONObject.parseObject(params);
            //obj.put("vendor","测试");
            params = obj.toJSONString();
            //测试需要,测试后注释掉相关代码

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);
            String code = apiRes.getString("code");
            if("0".equals(code)){
                UpdateWrapper<FlReConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                flReConsOrderReqLogService.update(updateWrapper);
            }else {
                Integer failCount = flReConsOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlReConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flReConsOrderReqLogService.update(updateWrapper);
                }

                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlReConsOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flReConsOrderReqLogService.update(updateWrapper);
                }
                log.info("接口执行异常!");
            }
        }
        log.info("执行 寄售退货单请求发送 结束!");
    }


    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void purInOrderSend(){
        log.info("执行 采购单请求发送 开始!");
        QueryWrapper<FlPurInOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);

        List<FlPurInOrderReqLog> flPurInOrderReqLogs = flPurInOrderReqLogService.list(queryWrapper);

        for(FlPurInOrderReqLog flPurInOrderReqLog : flPurInOrderReqLogs){
            String requestId = flPurInOrderReqLog.getRequestId();
            String apiId = flPurInOrderReqLog.getApiId();
            String params = flPurInOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);
            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理返回信息
            String code = apiRes.getString("code");

            if("0".equals(code)){
                UpdateWrapper<FlPurInOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");

                flPurInOrderReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                Integer failCount = flPurInOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }
                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlPurInOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flPurInOrderReqLogService.update(updateWrapper);
                }

                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlPurInOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);
                    flPurInOrderReqLogService.update(updateWrapper);
                }
                log.info("接口执行异常!");
            }
        }
        log.info("执行 采购单请求发送 结束!");
    }


    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void rePurOrderSend(){
        log.info("采购退回单请求发送开始!");
        QueryWrapper<FlRePurOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);

        List<FlRePurOrderReqLog> flRePurOrderReqLogs = flRePurOrderReqLogService.list(queryWrapper);
        for (FlRePurOrderReqLog flRePurOrderReqLog : flRePurOrderReqLogs){
            String requestId = flRePurOrderReqLog.getRequestId();
            String apiId = flRePurOrderReqLog.getApiId();
            String params = flRePurOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);
            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理返回信息
            String code = apiRes.getString("code");

            if("0".equals(code)){
                UpdateWrapper<FlRePurOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String now = dateFormat.format(new Date());
                updateWrapper.set("updateTime",now);

                flRePurOrderReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                Integer failCount = flRePurOrderReqLog.getFailCount();
                if(failCount == null){
                    failCount = 1;
                }{
                    failCount = failCount + 1;
                }

                if(failCount==3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlRePurOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","3");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String now = dateFormat.format(new Date());
                    updateWrapper.set("updateTime",now);

                    flRePurOrderReqLogService.update(updateWrapper);
                }

                if(failCount<3){
                    String message = apiRes.getString(("message"));
                    UpdateWrapper<FlRePurOrderReqLog> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("requestId",requestId);
                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                    updateWrapper.set("status","2");
                    updateWrapper.set("message",message);
                    updateWrapper.set("fail_count",failCount);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String now = dateFormat.format(new Date());
                    updateWrapper.set("updateTime",now);

                    flRePurOrderReqLogService.update(updateWrapper);
                }
            }
        }
        log.info("采购退回单请求发送结束!");
    }

//    @Elk
//    //@Scheduled(cron = "0 */2 * * * *")
//    public void setSonOrderSend(){
//        log.info("组套单-发送开始!");
//        QueryWrapper<FlSetOrderReqLog> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne("son_status",1).ne("son_status",3).ne("son_status",4).ne("son_status",5);
//        queryWrapper.ne("fr_status",1).ne("fr_status",3).ne("fr_status",4).ne("fr_status",5);
//
//        List<FlSetOrderReqLog> flSetOrderReqLogs = flSetOrderReqLogService.list(queryWrapper);
//
//        for (FlSetOrderReqLog flSetOrderReqLog : flSetOrderReqLogs){
//
//            String requestId = flSetOrderReqLog.getRequestId();
//            String apiId = flSetOrderReqLog.getApiId();
//            String sonParams = flSetOrderReqLog.getSonParams();
//
//
//            log.info("开始调用apiId为" + apiId + "的子件参数：" + sonParams);
//            log.info("组套单-发送子项单据");
//
//            QueryWrapper<FuLunInterface> queryWrapper2 = new QueryWrapper<>();
//            queryWrapper2.eq("id",apiId);
//            FuLunInterface fuLunInterface = fuLunInterfaceService.getOne(queryWrapper2);
//            String fjk = fuLunInterface.getFjk();
//            String[] parts = fjk.split("&"); // 使用&作为分隔符进行拆分
//            List<String> list = new ArrayList<>();
//
//            for (String part : parts) {
//                list.add(part);
//            }
//
//            String fjk1 = list.get(0);
//            String url1 = "https://upace-api.ibiza.com.tw"+fjk1;
//            log.info("url1="+url1);
//            JSONObject sonJson = JSONObject.parseObject(sonParams);
//            sonJson.put("customer_name","");
//            sonParams = sonJson.toJSONString();
//            JSONObject apiSonRes = fuLunHttpService.doAction1(url1,sonParams);
//
//
//            //组套父件入库单
//            String frParams = flSetOrderReqLog.getFrParams();
//            log.info("开始调用apiId为" + apiId + "的父件参数：" + frParams);
//            log.info("组套单-发送父项单据");
//            String fjk2 = list.get(1);
//            String url2 = "https://upace-api.ibiza.com.tw"+fjk2;
//            log.info("url2="+url2);
//            JSONObject apiFrRes = fuLunHttpService.doAction1(url2,frParams);
//
//
//            //处理子件出库返回信息
//            String sonCode = apiSonRes.getString("code");
//
//
//            //处理父件入库返回信息
//            String frCode = apiFrRes.getString("code");
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            String timeStr = dateFormat.format(new Date()).toString();
//
//            if("0".equals(sonCode)){
//                UpdateWrapper<FlSetOrderReqLog> sonUpdateWrapper = new UpdateWrapper<>();
//                sonUpdateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                sonUpdateWrapper.set("son_status","1");
//                sonUpdateWrapper.set("son_message","组套子件订单执行成功");
//
//
//                sonUpdateWrapper.set("updateTime",timeStr);
//
//                flSetOrderReqLogService.update(sonUpdateWrapper);
//                log.info("组套子件订单执行成功!");
//
//            }else {
//                Integer sonFailCount = flSetOrderReqLog.getSonFailCount();
//                if(sonFailCount == null){
//                    sonFailCount = 1;
//                }{
//                    sonFailCount = sonFailCount + 1;
//                }
//
//                if(sonFailCount==3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlSetOrderReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","3");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//
//
//                    updateWrapper.set("updateTime",timeStr);
//
//                    flSetOrderReqLogService.update(updateWrapper);
//                }
//
//                if(sonFailCount<3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlSetOrderReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","2");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//
//
//                    updateWrapper.set("updateTime",timeStr);
//
//                    flSetOrderReqLogService.update(updateWrapper);
//                }
//            }
//
//            if("0".equals(frCode)){
//
//                UpdateWrapper<FlSetOrderReqLog> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                updateWrapper.set("fr_status","1");
//                updateWrapper.set("fr_message","组套父件入库单执行成功");
//                updateWrapper.set("updateTime",timeStr);
//
//                flSetOrderReqLogService.update(updateWrapper);
//                log.info("组套父件入库单执行成功!");
//            }else {
//                Integer frFailCount = flSetOrderReqLog.getFrFailCount();
//                if(frFailCount == null){
//                    frFailCount = 1;
//                }{
//                    frFailCount = frFailCount + 1;
//                }
//
//                if(frFailCount==3){
//                    String message = apiFrRes.getString(("message"));
//                    UpdateWrapper<FlSetOrderReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","3");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//                    updateWrapper.set("updateTime",timeStr);
//                    flSetOrderReqLogService.update(updateWrapper);
//                }
//
//                if(frFailCount<3){
//                    String message = apiFrRes.getString(("message"));
//                    UpdateWrapper<FlSetOrderReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","2");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//                    updateWrapper.set("updateTime",timeStr);
//
//                    flSetOrderReqLogService.update(updateWrapper);
//                }
//            }
//        }
//        log.info("组套单-子件订单发送发送结束!");
//    }
//
//    @Elk
//    //@Scheduled(cron = "0 */2 * * * *")
//    public void dismantleOrderSend(){
//        log.info("拆卸单-请求发送开始!");
//        QueryWrapper<FlDismantleReqLog> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne("son_status",1).ne("son_status",3).ne("son_status",4).ne("son_status",5);
//        queryWrapper.ne("fr_status",1).ne("fr_status",3).ne("fr_status",4).ne("fr_status",5);
//
//        List<FlDismantleReqLog> flDismantleReqLogs = flDismantleReqLogService.list(queryWrapper);
//
//
//        for (FlDismantleReqLog flDismantleReqLog : flDismantleReqLogs){
//            String requestId = flDismantleReqLog.getRequestId();
//            String apiId = flDismantleReqLog.getApiId();
//            String frParams = flDismantleReqLog.getFrParams();
//
//            log.info("开始调用apiId为" + apiId + "的父件参数：" + frParams);
//            log.info("组套单-发送子项单据");
//
//
//            QueryWrapper<FuLunInterface> queryWrapper2 = new QueryWrapper<>();
//            queryWrapper2.eq("id",apiId);
//            FuLunInterface fuLunInterface = fuLunInterfaceService.getOne(queryWrapper2);
//            String fjk = fuLunInterface.getFjk();
//            String[] parts = fjk.split("&"); // 使用&作为分隔符进行拆分
//            List<String> list = new ArrayList<>();
//
//            for (String part : parts) {
//                list.add(part);
//            }
//
//
//            String fjk1 = list.get(0);
//            String url1 = "https://upace-api.ibiza.com.tw"+fjk1;
//            log.info("url1="+url1);
//
//            JSONObject apiFrRes = fuLunHttpService.doAction1(url1,frParams);
//
//            //处理父件出库返回信息
//            String frCode = apiFrRes.getString("code");
//
//
//            //拆卸子件入库单
//            String sonParams = flDismantleReqLog.getSonParams();
//
//            log.info("开始调用apiId为" + apiId + "的子件参数：" + sonParams);
//            log.info("组套单-发送子项单据");
//
//            String fjk2 = list.get(1);
//            String url2 = "https://upace-api.ibiza.com.tw"+fjk2;
//            log.info("url2="+url2);
//
//
//            JSONObject apiSonRes = fuLunHttpService.doAction1(url2,sonParams);
//
//            //处理子件入库返回信息
//            String sonCode = apiSonRes.getString("code");
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            String str = dateFormat.format(new Date()).toString();
//
//            if("0".equals(frCode)){
//                UpdateWrapper<FlDismantleReqLog> frUpdateWrapper = new UpdateWrapper<>();
//                frUpdateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                frUpdateWrapper.set("fr_status","1");
//                frUpdateWrapper.set("fr_message","拆卸订单执行成功");
//
//
//                frUpdateWrapper.set("updateTime",str);
//
//                flDismantleReqLogService.update(frUpdateWrapper);
//                log.info("组套子件订单执行成功!");
//            }else {
//                Integer frFailCount = flDismantleReqLog.getFrFailCount();
//                if(frFailCount == null){
//                    frFailCount = 1;
//                }{
//                    frFailCount = frFailCount + 1;
//                }
//
//                if(frFailCount==3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlDismantleReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","3");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//
//
//                    updateWrapper.set("updateTime",str);
//
//                    flDismantleReqLogService.update(updateWrapper);
//                }
//                if(frFailCount<3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlDismantleReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","2");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//
//                    updateWrapper.set("updateTime",str);
//
//                    flDismantleReqLogService.update(updateWrapper);
//                }
//            }
//
//            if ("0".equals(sonCode)){
//
//                UpdateWrapper<FlDismantleReqLog> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                updateWrapper.set("son_status","1");
//                updateWrapper.set("son_message","拆卸子件订单执行成功");
//                updateWrapper.set("updateTime",str);
//
//                flDismantleReqLogService.update(updateWrapper);
//                log.info("组套子件入库单执行成功!");
//            }else {
//
//                Integer sonFailCount = flDismantleReqLog.getSonFailCount();
//                if(sonFailCount == null){
//                    sonFailCount = 1;
//                }{
//                    sonFailCount = sonFailCount + 1;
//                }
//
//                if(sonFailCount==3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlDismantleReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","3");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//                    updateWrapper.set("updateTime",str);
//                    flDismantleReqLogService.update(updateWrapper);
//                }
//
//                if(sonFailCount<3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlDismantleReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","2");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//                    updateWrapper.set("updateTime",str);
//                    flDismantleReqLogService.update(updateWrapper);
//                }
//            }
//        }
//        log.info("拆卸单-子件订单发送发送结束!");
//    }
//
//    @Elk
//    //@Scheduled(cron = "0 */2 * * * *")
//    public void transCodeSend(){
//        log.info("转码单-请求发送开始!");
//
//        QueryWrapper<FlTransCodeReqLog> queryFrWrapper = new QueryWrapper<>();
//        queryFrWrapper.eq("fr_status","0");
//        queryFrWrapper.isNotNull("fr_params").isNull("son_params");
//
//
//        List<FlTransCodeReqLog> frReqLogs = flTransCodeReqLogService.list(queryFrWrapper);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        String str = dateFormat.format(new Date()).toString();
//
//        for(FlTransCodeReqLog frReqLog : frReqLogs){
//
//            String requestId = frReqLog.getRequestId();
//            String apiId = frReqLog.getApiId();
//            String frParams = frReqLog.getFrParams();
//
//
//
//            JSONObject frParamsJson = JSONObject.parseObject(frParams);
//            JSONArray products = frParamsJson.getJSONArray("products");
//
//            String note = frParamsJson.getString("note");
//
//            for(int i =0;i<products.size();i++){
//                JSONObject productJson = products.getJSONObject(i);
//
//                String productNote = productJson.getString("note");
//
//                note = note+",明细备注"+productNote;
//            }
//            frParamsJson.put("note",note);
//
//            frParams = frParamsJson.toJSONString();
//
//            log.info("开始调用apiId为" + apiId + "的父件参数：" + frParams);
//            log.info("转码单-发送父项单据");
//            JSONObject apiSonRes = fuLunHttpService.doAction(apiId,frParams);
//
//            //处理父件出库返回信息
//            String frCode = apiSonRes.getString("code");
//
//            if("0".equals(frCode)){
//                UpdateWrapper<FlTransCodeReqLog> frUpdateWrapper = new UpdateWrapper<>();
//                frUpdateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                frUpdateWrapper.set("fr_status","1");
//                frUpdateWrapper.isNotNull("fr_params").isNull("son_params");
//                frUpdateWrapper.set("fr_message","转码父项订单执行成功");
//                frUpdateWrapper.set("updateTime",str);
//
//                flTransCodeReqLogService.update(frUpdateWrapper);
//                log.info("组套子件订单执行成功!");
//            }else {
//
//
//                String message = apiSonRes.getString(("message"));
//                UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                updateWrapper.set("fr_status","3");
//                updateWrapper.isNotNull("fr_params").isNull("son_params");
//                updateWrapper.set("fr_message",message);
//
//                /*Integer frFailCount = frReqLog.getFrFailCount();
//
//                if(frFailCount == null){
//                    frFailCount = 1;
//                }{
//                    frFailCount = frFailCount + 1;
//                }
//
//                if(frFailCount==3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","3");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//
//
//                    updateWrapper.set("updateTime",str);
//
//                    flTransCodeReqLogService.update(updateWrapper);
//                }
//                if(frFailCount<3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("fr_status","2");
//                    updateWrapper.set("fr_message",message);
//                    updateWrapper.set("fr_fail_count",frFailCount);
//
//
//                    updateWrapper.set("updateTime",str);
//
//                    flTransCodeReqLogService.update(updateWrapper);
//                }*/
//            }
//        }
//
//        //处理，子项进仓单
//
//        QueryWrapper<FlTransCodeReqLog> querySonWrapper = new QueryWrapper<>();
//        querySonWrapper.eq("son_status","0");
//        querySonWrapper.isNotNull("son_params").isNull("fr_params");
//        List<FlTransCodeReqLog> flTransCodeReqLogs = flTransCodeReqLogService.list(querySonWrapper);
//
//        //log.info("flTransCodeReqLogs="+flTransCodeReqLogs.toString());
//
//        for (FlTransCodeReqLog sonReqLog : flTransCodeReqLogs){
//            String requestId = sonReqLog.getRequestId();
//            String apiId = sonReqLog.getApiId();
//            String sonParams = sonReqLog.getSonParams();
//
//            log.info("开始调用apiId为" + apiId + "的子件参数：" + sonParams);
//            log.info("转码单-发送子项单据");
//
//            JSONObject apiSonRes = fuLunHttpService.doAction(apiId,sonParams);
//
//            String sonCode = apiSonRes.getString("code");
//
//            if ("0".equals(sonCode)){
//
//                UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                updateWrapper.set("son_status","1");
//                updateWrapper.isNotNull("son_params").isNull("fr_params");
//                updateWrapper.set("son_message","拆卸子件订单执行成功");
//                updateWrapper.set("updateTime",str);
//
//                flTransCodeReqLogService.update(updateWrapper);
//                log.info("子件入库单执行成功!");
//            }else {
//
//                String message = apiSonRes.getString(("message"));
//                UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                updateWrapper.eq("requestId",requestId);
//                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                updateWrapper.set("son_status","3");
//                updateWrapper.isNotNull("son_params").isNull("fr_params");
//                updateWrapper.set("son_message",message);
//                updateWrapper.set("updateTime",str);
//                flTransCodeReqLogService.update(updateWrapper);
//
//                /*Integer sonFailCount = sonReqLog.getSonFailCount();
//                if(sonFailCount == null){
//                    sonFailCount = 1;
//                }{
//                    sonFailCount = sonFailCount + 1;
//                }
//
//                if(sonFailCount==3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","3");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//                    updateWrapper.set("updateTime",str);
//                    flTransCodeReqLogService.update(updateWrapper);
//                }
//
//                if(sonFailCount<3){
//                    String message = apiSonRes.getString(("message"));
//                    UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
//                    updateWrapper.eq("requestId",requestId);
//                    //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
//                    updateWrapper.set("son_status","2");
//                    updateWrapper.set("son_message",message);
//                    updateWrapper.set("son_fail_count",sonFailCount);
//                    updateWrapper.set("updateTime",updateWrapper.set("updateTime",str));
//                    flTransCodeReqLogService.update(updateWrapper);
//                }*/
//            }
//        }
//    }

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void channelLockSend(){
        log.info("锁库-请求发送开始!");
        QueryWrapper<FlChannelLockReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",0);
        List<FlChannelLockReqLog> flChannelLockReqLogs = flChannelLockReqLogService.list(queryWrapper);

        for (FlChannelLockReqLog flChannelLockReqLog : flChannelLockReqLogs){
            String requestId = flChannelLockReqLog.getRequestId();
            String apiId = flChannelLockReqLog.getApiId();
            String params = flChannelLockReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            JSONObject obj = JSONObject.parseObject(params);

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            String code = apiRes.getString("code");

            if("0".equals(code)){
                UpdateWrapper<FlChannelLockReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                flChannelLockReqLogService.update(updateWrapper);
                log.info("接口执行成功!");
            }else {
                String message = apiRes.getString(("message"));
                UpdateWrapper<FlChannelLockReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","2");
                updateWrapper.set("message",message);
                flChannelLockReqLogService.update(updateWrapper);
            }
        }
    }


    /*@Scheduled(cron = "* * * * * ?")
    public void task1() throws InterruptedException {
        log.info("task1");
        TimeUnit.HOURS.sleep(1);
    }*/




}
