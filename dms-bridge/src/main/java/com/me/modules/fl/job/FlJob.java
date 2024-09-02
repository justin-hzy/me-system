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

    private FlTransCodeReqLogService flTransCodeReqLogService;

    private FlChannelLockReqLogService flChannelLockReqLogService;

    private FlSetDismantleReqLogService flSetDismantleReqLogService;

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

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void transCodeSend(){
        log.info("转码单-请求发送开始!");

        QueryWrapper<FlTransCodeReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","0");

        List<FlTransCodeReqLog> logs = flTransCodeReqLogService.list(queryWrapper);

        for (FlTransCodeReqLog reqLog : logs){
            String requestId = reqLog.getRequestId();

            String apiId = reqLog.getApiId();

            String params = reqLog.getParams();
            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理加工单入库返回信息
            String code = apiRes.getString("code");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = dateFormat.format(new Date()).toString();
            if("0".equals(code)){

                UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                updateWrapper.set("updateTime",timeStr);

                flTransCodeReqLogService.update(updateWrapper);

                log.info("执行成功!");
            }else {
                String message = apiRes.getString(("message"));
                UpdateWrapper<FlTransCodeReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status 1:成功 2:失败
                updateWrapper.set("status","2");
                updateWrapper.set("message",message);
                flTransCodeReqLogService.update(updateWrapper);
                log.info("接口执行异常!");
            }
        }


    }

    @Elk
    @Scheduled(cron = "0 */2 * * * *")
    public void SetDismantleSend(){
        log.info("组套拆卸单-发送开始!");
        QueryWrapper<FlSetDismantleReqLog> setDismantleLog = new QueryWrapper<>();
        setDismantleLog.eq("status","0");

        List<FlSetDismantleReqLog> logs = flSetDismantleReqLogService.list(setDismantleLog);

        for (FlSetDismantleReqLog reqLog : logs) {
            String requestId = reqLog.getRequestId();
            String apiId = reqLog.getApiId();

            String params = reqLog.getParams();
            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理加工单入库返回信息
            String code = apiRes.getString("code");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStr = dateFormat.format(new Date()).toString();
            if("0".equals(code)){

                UpdateWrapper<FlSetDismantleReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预 (组装拆卸/转码 4:提交流程 5:提交流程失败)
                updateWrapper.set("status","1");
                updateWrapper.set("message","执行成功");
                updateWrapper.set("updateTime",timeStr);

                flSetDismantleReqLogService.update(updateWrapper);

                log.info("执行成功!");
            }else {
                String message = apiRes.getString(("message"));
                UpdateWrapper<FlSetDismantleReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status  0: 未请求  1:请求成功 2:请求失败 3: 三次执行失败，需要人工干预
                updateWrapper.set("status","2");
                updateWrapper.set("message",message);
                flSetDismantleReqLogService.update(updateWrapper);
            }
        }
    }

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
