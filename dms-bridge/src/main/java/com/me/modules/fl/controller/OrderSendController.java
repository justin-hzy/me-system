package com.me.modules.fl.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.core.JsonResult;

import com.me.modules.fl.entity.FlOrderReqLog;
import com.me.modules.fl.entity.FlReOrderReqLog;
import com.me.modules.fl.entity.FlTrfOrderReqLog;
import com.me.modules.fl.service.FlOrderReqLogService;
import com.me.modules.fl.service.FlReOrderReqLogService;
import com.me.modules.fl.service.FlTrfOrderReqLogService;
import com.me.modules.fl.service.FuLunHttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dms")
@Slf4j
@AllArgsConstructor
public class OrderSendController {

    private FlOrderReqLogService flOrderReqLogService;

    private FuLunHttpService fuLunHttpService;

    private FlReOrderReqLogService flReOrderReqLogService;

    private FlTrfOrderReqLogService flTrfOrderReqLogService;

    /*发送订单*/
    //@PostMapping("orderSend")
    public JsonResult orderSend(){
        QueryWrapper<FlOrderReqLog> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.ne("status",1).ne("status",3);
        List<FlOrderReqLog> flOrderReqLogs = flOrderReqLogService.list(queryWrapper1);

        for(FlOrderReqLog flOrderReqLog :flOrderReqLogs){

            String requestId = flOrderReqLog.getRequestId();
            String apiId = flOrderReqLog.getApiId();
            String params = flOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            //测试需要,测试后注释掉相关代码
            JSONObject obj = JSONObject.parseObject(params);
            //obj.put("customer_name","测试");

            JSONObject apiRes = fuLunHttpService.doAction(apiId,params);

            //处理返回信息
            String code = apiRes.getString("code");

            if("0".equals(code)){
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
        return JsonResult.ok();
    }


    /*发送退货单*/
    @PostMapping("returnOrderSend")
    public JsonResult returnOrderSend(){
        QueryWrapper<FlReOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1);
        List<FlReOrderReqLog> flReOrderReqLogs = flReOrderReqLogService.list(queryWrapper);

        for(FlReOrderReqLog flReOrderReqLog : flReOrderReqLogs){
            String requestId = flReOrderReqLog.getRequestId();
            String apiId = flReOrderReqLog.getApiId();
            String params = flReOrderReqLog.getParams();

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
                String message = apiRes.getString(("message"));
                UpdateWrapper<FlReOrderReqLog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("requestId",requestId);
                //status 1:成功 2:失败
                updateWrapper.set("status","2");
                updateWrapper.set("message",message);
                flReOrderReqLogService.update(updateWrapper);
                log.info("接口执行异常!");
            }
        }
        return JsonResult.ok();
    }

    /*发送仓内调拨单*/
    //@PostMapping("transferOrderSend")
    public JsonResult transferOrderSend(){
        log.info("仓内调拨开始!");
        QueryWrapper<FlTrfOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);

        List<FlTrfOrderReqLog> FlTrfOrderReqLogs = flTrfOrderReqLogService.list(queryWrapper);

        for(FlTrfOrderReqLog flTrfOrderReqLog : FlTrfOrderReqLogs){
            //String requestId = flTrfOrderReqLog.getRequestId();
            String apiId = flTrfOrderReqLog.getApiId();
            String params = flTrfOrderReqLog.getParams();

            log.info("开始调用apiId为" + apiId + "的参数：" + params);

            /*JSONObject apiRes = fuLunHttpService.doAction(apiId,params);*/

            /*JSONObject apiRes = new JSONObject();

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

            }*/
        }
        /*flTrfOrderReqLogService*/
        log.info("仓内调拨结束!");
        return JsonResult.ok();
    }

    /*@PostMapping("outerTransferOrderSend")
    public JsonResult outerTransferOrderSend(){
        log.info("跨主体调拨开始!");

        QueryWrapper<FlTrfOrderReqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status",1).ne("status",3);

        List<FlTrfOrderReqLog> FlTrfOrderReqLogs = flTrfOrderReqLogService.list(queryWrapper);

        for(FlTrfOrderReqLog flTrfOrderReqLog : FlTrfOrderReqLogs) {
            //String requestId = flTrfOrderReqLog.getRequestId();
            String apiId = flTrfOrderReqLog.getApiId();
            String params = flTrfOrderReqLog.getParams();

            //解析dms json对象
            JSONObject trfJsonObj  = JSONObject.parseObject(params);

            //获取订单单号 or 进仓单号
            String title = trfJsonObj.getString("title");

            String note = trfJsonObj.getString("note");

            JSONArray items = trfJsonObj.getJSONArray("items");


            //封装订单json对象
            JSONObject orderJsonObj = new JSONObject();
            orderJsonObj.put("name",title);
            //暂时默认物流类型是新主流
            orderJsonObj.put("shipping_type","hct");
            //设置回传地址
            orderJsonObj.put("callback_url","");
            //设置备注
            orderJsonObj.put("note",note);

            JSONArray orderProducts = new JSONArray();
            for (int i = 0;i<items.size();i++){
                JSONObject jsonProduct = new JSONObject();
                String sku = items.getJSONObject(i).getString("sku");
                String quantity = items.getJSONObject(i).getString("quantity");
                String storage_type = items.getJSONObject(i).getString("storage_type");
                jsonProduct.put("sku",sku);
                jsonProduct.put("quantity",quantity);
                jsonProduct.put("storage_type",storage_type);

                orderProducts.add(jsonProduct);
            }

            orderJsonObj.put("products",orderProducts);

            String orderParams  = orderJsonObj.toJSONString();
            log.info("orderParams="+orderParams);
            //JSONObject apiRes = fuLunHttpService.doAction("1",orderParams);




            //


            //封装进仓单json对象
            JSONObject receiptJsonObj = new JSONObject();
            receiptJsonObj.put("title",title);
            //暂时当天到货
            receiptJsonObj.put("est_date","");
            receiptJsonObj.put("callback_url","");

            JSONArray receiptItems = new JSONArray();
            for (int i = 0;i<items.size();i++){
                JSONObject jsonItem = new JSONObject();
                String sku = items.getJSONObject(i).getString("sku");
                String quantity = items.getJSONObject(i).getString("quantity");
                String storage_type = items.getJSONObject(i).getString("storage_type");
                jsonItem.put("sku",sku);
                jsonItem.put("quantity",quantity);
                jsonItem.put("storage_type",storage_type);

                receiptItems.add(jsonItem);
            }
            receiptJsonObj.put("items",receiptItems);

            String receiptParams = receiptJsonObj.toJSONString();

            log.info("receiptParams="+receiptParams);


            //log.info("开始调用apiId为" + apiId + "的参数：" + params);
        }
        log.info("跨主体调拨结束!");
        return JsonResult.ok();
    }*/


    //@GetMapping("/test")
    public String test(){
        String apiId = "1";
        String params = "{\"callback_url\":\"http://58.63.47.138:8084/weaverSystem/dms/getOrderCallBack\",\"delivery_date\":\"2024-03-29\",\"address\":\"桃園市平鎮區南東路57-1號\",\"phone\":\"034399888\",\"name\":\"P2023032905\",\"shipping_type\":\"hct\",\"channel\":\"大润发\",\"customer_name\":\"测试\",\"products\":[{\"storage_type\":\"B1\",\"quantity\":\"1\",\"sku\":\"4711401210962-11111\"}]}";
        JSONObject apiRes = fuLunHttpService.doAction(apiId,params);
        String code = apiRes.getString("code");
        if("0".equals(code)){
//            UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("requestId",requestId);
//            //status 1:成功
//            updateWrapper.set("status","1");
//            updateWrapper.set("message","执行成功");
//            flOrderReqLogService.update(updateWrapper);
            log.info("接口执行成功!");
        }else {
//            String message = apiRes.getString(("message"));
//            UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("requestId",requestId);
//            //status 1:成功
//            updateWrapper.set("status","2");
//            updateWrapper.set("message",message);
//            flOrderReqLogService.update(updateWrapper);
            log.info("接口执行异常!");
        }

        return "success";
    }

    @GetMapping("returnOrderTest")
    public String returnOrderTet(){
        String apiId= "2";
        String params = "{\"sender_name\":\"测试\",\"token\":\"DMSTW04-2024040102\",\"products\":[{\"storage_type\":\"B1\",\"identifier\":\"4\",\"quantity\":\"1\",\"sku\":\"4711401210962-11111\"}]}";
        JSONObject apiRes = fuLunHttpService.doAction(apiId,params);
        String code = apiRes.getString("code");
        if("0".equals(code)){
//            UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("requestId",requestId);
//            //status 1:成功
//            updateWrapper.set("status","1");
//            updateWrapper.set("message","执行成功");
//            flOrderReqLogService.update(updateWrapper);
            log.info("接口执行成功!");
        }else {
//            String message = apiRes.getString(("message"));
//            UpdateWrapper<FlOrderReqLog> updateWrapper = new UpdateWrapper<>();
//            updateWrapper.eq("requestId",requestId);
//            //status 1:成功
//            updateWrapper.set("status","2");
//            updateWrapper.set("message",message);
//            flOrderReqLogService.update(updateWrapper);
            log.info("接口执行异常!");
        }

        return "success";
    }

    @GetMapping("/hello")
    public String hello(){
        log.info("hello world");
        return "success";
    }
}
