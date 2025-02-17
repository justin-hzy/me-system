package com.me.modules.fl.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.DmsConfig;
import com.me.common.utils.DmsUtil;
import com.me.modules.fl.dto.*;
import com.me.modules.fl.entity.*;
import com.me.modules.fl.pojo.*;
import com.me.modules.fl.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dms")
@Slf4j
@AllArgsConstructor
public class CallBackController {

    /**
     * 模拟缓存服务
     */
    private static final Map<String,String> SYSTEM_CACHE = new HashMap<>();

    private static final String APPID = "62acf88c-55d0-465c-b08d-99cd36271231";

    private FlOrderFormTableMainService flOrderFormTableMainService;

    private FlReOrderFormTableMainService flReOrderFormTableMainService;

    private DmsConfig dmsConfig;

    private FlCBErrorLogService flCBErrorLogService;

    private FlTrfOrderFormTableMainService trfOrderFormTableMainService;

    private FlPurInFormTableMainService flPurInFormTableMainService;

    private FlRePurOrderFormTableMainService flRePurOrderFormTableMainService;

    private FlSetTableMainService flSetTableMainService;

    private FlReOrderSubMainService flReOrderSubMainService;

    private FlReOrderSubDtl1Service flReOrderSubDtl1Service;

    private FlSetDismantleReqLogService flSetDismantleReqLogService;

    private FlDismantleReqLogService flDismantleReqLogService;

    private FlTransCodeReqLogService flTransCodeReqLogService;

    private FlTransCodeMainService flTransCodeMainService;


    /*销售订单-回传接口*/
    @PostMapping("getOrderCallBack")
    public String getOrderCB(@RequestBody GetOrderCBDto getOrderCBDto, HttpServletRequest request){

        //log.info("Client IP:"+request.getRemoteAddr());

        log.info("reqDto="+getOrderCBDto.toString());

        /*富仑单号(订单号)*/
        String name = getOrderCBDto.getName();

        /*物流信息*/
        List<Shipping> shippings = getOrderCBDto.getShippings();

        /*明细*/
        List<Product> products = getOrderCBDto.getProducts();

        /*订单类型*/
        String status = getOrderCBDto.getStatus();

        //to do
        String internal_name = getOrderCBDto.getInternal_name();

        JSONObject jsonObject = new JSONObject();

        if("done".equals(status)){

            QueryWrapper<FlOrderFormTableMain> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("fddh",name);


            FlOrderFormTableMain FlOrderFormTableMain = flOrderFormTableMainService.getOne(queryWrapper);
            if(FlOrderFormTableMain != null){

                /*销售出库单*/
                String pO = FlOrderFormTableMain.getPO();

                JSONArray mainDataArr = new JSONArray();

                jsonObject.put("requestId", Integer.valueOf(FlOrderFormTableMain.getRequestid()));

                String tracking_number = "物流号为空";

                String shipping_type = "物流公司代码为空";


                if(shippings.size()>0){
                    Shipping shipping = shippings.get(0);
                    /*物流号*/
                    tracking_number = shipping.getTracking_number();
                    /*物流公司代码*/
                    shipping_type = shipping.getShipping_type();
                }

                if("hct".equals(shipping_type)){
                    shipping_type = "新竹物流";
                }else if ("tcawt".equals(shipping_type)){
                    shipping_type = "黑貓物流";
                }else if ("dpex".equals(shipping_type)){
                    shipping_type = "迪比翼";
                }else if ("ecpay-seven-cod".equals(shipping_type)){
                    shipping_type = "綠界711-超取代收";
                }else if ("ecpay-seven-prepaid".equals(shipping_type)){
                    shipping_type = "綠界711-超取已付款";
                }else {
                    shipping_type = "專車";
                }

                JSONObject mainData1 = new JSONObject();
                mainData1.put("fieldName","wldh");
                mainData1.put("fieldValue",tracking_number);

                JSONObject mainData2 = new JSONObject();
                mainData2.put("fieldName","wlgs");
                mainData2.put("fieldValue",shipping_type);

                JSONObject mainData3 = new JSONObject();
                mainData3.put("fieldName","xsckdh");
                mainData3.put("fieldValue",name);

                JSONObject mainData4 = new JSONObject();
                mainData4.put("fieldName","zt");
                mainData4.put("fieldValue","1");

                JSONObject mainData5 = new JSONObject();
                //
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String todayString = today.format(formatter);

                mainData5.put("fieldName","chrq");
                mainData5.put("fieldValue",todayString);

                mainDataArr.add(mainData1);
                mainDataArr.add(mainData2);
                mainDataArr.add(mainData3);
                mainDataArr.add(mainData4);
                mainDataArr.add(mainData5);

                jsonObject.put("mainData",mainDataArr);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                for(int i=0;i<products.size();i++){
                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String shipped_quantity = products.get(i).getShipped_quantity();
                    Integer quantity = 0 ;
                    if(shipped_quantity != null){
                        quantity = Integer.valueOf(shipped_quantity);
                    }

                    workflowRequestTableFields1.put("fieldName","cksl");
                    workflowRequestTableFields1.put("fieldValue",quantity);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","xsckdh");
                    workflowRequestTableFields2.put("fieldValue",name);

                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","chrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    String sku = products.get(i).getSku();
                    workflowRequestTableFields4.put("fieldName","hpbh");
                    workflowRequestTableFields4.put("fieldValue",sku);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                //DmsUtil.testRestful("http://39.108.136.182:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());
                try{
                    DmsUtil.testRegist(dmsConfig.getIp());
                    DmsUtil.testGetoken(dmsConfig.getIp());
                    DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
                }catch (Exception e){
                    log.info("订单回传异常，数据进入中间表");
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("requestid", FlOrderFormTableMain.getRequestid());
                    FlCBErrorLog resultObj = flCBErrorLogService.getOne(queryWrapper1);
                    if(resultObj == null){
                        FlCBErrorLog flCBErrorLog = new FlCBErrorLog();
                        flCBErrorLog.setRequestId(FlOrderFormTableMain.getRequestid());
                        flCBErrorLog.setMessage("提交失败");
                        flCBErrorLog.setError(e.getMessage());
                        flCBErrorLog.setParams(jsonObject.toJSONString());
                        //1:成功 2:失败
                        flCBErrorLog.setStatus("2");
                        /*类型 1:订单回传 2:退单回传*/
                        flCBErrorLog.setType("1");

//                        LocalDate today = LocalDate.now();
                        LocalDate yesterday = today.minusDays(1);

//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String yesterdayString = yesterday.format(formatter);

                        flCBErrorLog.setCreateTime(yesterdayString);
                        flCBErrorLogService.save(flCBErrorLog);
                    }
                    e.printStackTrace();
                }
            }
        }else if("canceled".equals(status) || "terminated".equals(status)){
            log.info("name="+name+"订单取消");
            //销售订单，富仑单方面取消，执行以下逻辑
            QueryWrapper<FlOrderFormTableMain> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("fddh",name);

            FlOrderFormTableMain FlOrderFormTableMain = flOrderFormTableMainService.getOne(queryWrapper);
            if(FlOrderFormTableMain != null){
                /*销售出库单*/
                String pO = FlOrderFormTableMain.getPO();

                JSONArray mainDataArr = new JSONArray();

                jsonObject.put("requestId", Integer.valueOf(FlOrderFormTableMain.getRequestid()));

                JSONObject mainData1 = new JSONObject();
                mainData1.put("fieldName","ddsfqx");
                mainData1.put("fieldValue","1");

                mainDataArr.add(mainData1);
                jsonObject.put("mainData",mainDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }
        }
        return "success";
    }

    /*销售退货单-回传接口*/
    @PostMapping("getReturnOrdersCB")
    public String getReturnOrdersCB(@RequestBody GetReturnOrderCBDto reqDto){

        log.info(reqDto.toString());

        //退货单号
        String title = reqDto.getTitle();

        //状态
        String status = reqDto.getStatus();

        JSONObject jsonObject = new JSONObject();

        if("stored".equals(status)){

            QueryWrapper<FlReOrderFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("khddh",title);

            FlReOrderFormTableMain flReOrderFormTableMain = flReOrderFormTableMainService.getOne(queryWrapper);

            if(flReOrderFormTableMain != null){
                Integer requestId = flReOrderFormTableMain.getRequestid();
                log.info("requestId="+requestId);

                jsonObject.put("requestId", requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                //退货明细
                List<ReturnOrderItem> returnOrderItems = reqDto.getItems();
                for(ReturnOrderItem returnOrderItem : returnOrderItems){

                    String  received_pcs = returnOrderItem.getReceived_pcs();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = returnOrderItem.getSku();
                    workflowRequestTableFields1.put("fieldName","hptxm");
                    workflowRequestTableFields1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","rksl");
                    if(received_pcs != null){
                        workflowRequestTableFields2.put("fieldValue",received_pcs);
                    }else {
                        workflowRequestTableFields2.put("fieldValue",0);
                    }


                    //获取当时日期
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);
                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rkrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    //明细id
                    String identifier = returnOrderItem.getIdentifier();
                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","mxid");
                    workflowRequestTableFields4.put("fieldValue",identifier);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getReturnOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);


                log.info(jsonObject.toJSONString());


                //DmsUtil.testRestful("http://39.108.136.182:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());
                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("退单回传-数据错误");
            }
        }else {
            log.info("当前状态不是入库状态，暂不执行提交");
        }
        return "success";
    }

    /*销售退货单2.0-回传接口*/
    @PostMapping("getNewReOrdersCB")
    public String getNewReOrdersCB(@RequestBody GetReturnOrderCBDto dto){

        log.info(dto.toString());

        //退货单号
        String title = dto.getTitle();

        //状态
        String status = dto.getStatus();

        if("stored".equals(status)){
            QueryWrapper<FlReOrderSubMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);

            FlReOrderSubMain flReOrderSubMain = flReOrderSubMainService.getOne(queryWrapper);

            if(flReOrderSubMain != null){
                Integer requestId = flReOrderSubMain.getRequestid();
                log.info("requestId="+requestId);
                int id = flReOrderSubMain.getId();
                List<ReturnOrderItem> items = dto.getItems();

                Map<String,Integer> resMap = new HashMap<>();

                //汇总退货数量
                for(ReturnOrderItem item : items){
                    String sku = item.getSku();
                    String receivedPcs = item.getReceived_pcs();

                    Integer receivedNum = Integer.valueOf(receivedPcs);


                    if(resMap.containsKey(sku)){
                        Integer currentReceive = resMap.get(sku);

                        currentReceive = currentReceive + receivedNum;

                        resMap.put(sku,currentReceive);

                    }else {
                        resMap.put(sku,receivedNum);
                    }
                }

                //匹配明细1表数据，查看是否存在漏传的数据
                QueryWrapper<FlReOrderSubDtl1> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("mainid",id);

                List<FlReOrderSubDtl1> flReOrderSubDtl1s = flReOrderSubDtl1Service.list(queryWrapper1);
                for (FlReOrderSubDtl1 flReOrderSubDtl1 : flReOrderSubDtl1s){
                    String hptxm = flReOrderSubDtl1.getHptxm();

                    if (!resMap.containsKey(hptxm)){
                        resMap.put(hptxm,0);
                    }
                }


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requestId",requestId);
                JSONArray workflowRequestTableRecordsArr = new JSONArray();
                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                for (String key : resMap.keySet()){
                    Integer receivedNum = resMap.get(key);
                    String sku = key;

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    workflowRequestTableFields1.put("fieldName","hptxm");
                    workflowRequestTableFields1.put("fieldValue",sku);


                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","rksl");

                    if(receivedNum != null){
                        workflowRequestTableFields2.put("fieldValue",receivedNum);
                    }else {
                        workflowRequestTableFields2.put("fieldValue",0);
                    }

                    //获取当时日期
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);
                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rkrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","khddh");
                    workflowRequestTableFields4.put("fieldValue",title);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);

                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getNewReturnOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("销售退货回传-数据错误");
            }
        }else if("canceled".equals(status) || "terminated".equals(status)) {
            log.info("取消dms单据，单据编号:"+title);
            QueryWrapper<FlReOrderSubMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);

            FlReOrderSubMain flReOrderSubMain = flReOrderSubMainService.getOne(queryWrapper);

            if(flReOrderSubMain != null){
                Integer requestId = flReOrderSubMain.getRequestid();
                log.info("requestId="+requestId);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requestId",requestId);

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                List<ReturnOrderItem> items = dto.getItems();

                for (ReturnOrderItem item : items){
                    Integer recordOrder = 0;
                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    String sku = item.getSku();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    workflowRequestTableFields1.put("fieldName","hptxm");
                    workflowRequestTableFields1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","rksl");
                    workflowRequestTableFields2.put("fieldValue",0);

                    //获取当时日期
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);
                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rkrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getNewReturnOrderDetailTable2());

                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }

        }else {
            log.info("当前状态不是入库状态，暂不执行提交");
        }
        return "success";
    }



    /*仓内调拨单-回传接口*/
    @PostMapping("getTrfOrderCallBack")
    public String getTrfOrdersCB(@RequestBody GetInnerTrfOrdersCBDto reqDto){
        log.info("reqDto="+reqDto.toString());

        /*调拨单号*/
        String title = reqDto.getTitle();

        //状态
        String status = reqDto.getStatus();

        JSONObject jsonObject = new JSONObject();

        if("done".equals(status)){
            QueryWrapper<FlTrfOrderFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlTrfOrderFormTableMain trfOrderFormTableMain = trfOrderFormTableMainService.getOne(queryWrapper);

            if(trfOrderFormTableMain != null){
                String requestId = trfOrderFormTableMain.getRequestid();
                log.info("requestId="+requestId);

                jsonObject.put("requestId", requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                //退货明细
                List<InnerTrfCBItem> returnOrderItems = reqDto.getItems();

                for(InnerTrfCBItem returnOrderItem : returnOrderItems){
                    String transferred_quantity = returnOrderItem.getTransferred_quantity();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = returnOrderItem.getSku();
                    workflowRequestTableFields1.put("fieldName","hpbh");
                    workflowRequestTableFields1.put("fieldValue",sku);


                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","cksl");
                    workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rksl");
                    workflowRequestTableFields3.put("fieldValue",transferred_quantity);

                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","fckrq");
                    workflowRequestTableFields4.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields5 = new JSONObject();
                    workflowRequestTableFields5.put("fieldName","fckrq");
                    workflowRequestTableFields5.put("fieldValue",todayString);



                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields5);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);

                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getTrfOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("调拨单回传-数据错误");
            }
        }else{
            log.info("当前状态不是完成状态，暂不执行提交");
        }
        return "success";
    }

    /*寄售订单-回传接口*/
    @PostMapping("getConOrderCB")
    public String getConOrderCB(@RequestBody GetOrderCBDto reqDto){

        log.info("reqDto="+reqDto.toString());

        /*调拨单号*/
        String title = reqDto.getName();

        //状态
        String status = reqDto.getStatus();

        JSONObject jsonObject = new JSONObject();

        if("done".equals(status)){
            QueryWrapper<FlTrfOrderFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlTrfOrderFormTableMain trfOrderFormTableMain = trfOrderFormTableMainService.getOne(queryWrapper);

            if(trfOrderFormTableMain != null){
                String requestId = trfOrderFormTableMain.getRequestid();
                log.info("requestId="+requestId);

                jsonObject.put("requestId", requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                //订单明细
                List<Product> products = reqDto.getProducts();

                for(Product product : products){
                    String transferred_quantity = product.getShipped_quantity();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = product.getSku();
                    workflowRequestTableFields1.put("fieldName","hpbh");
                    workflowRequestTableFields1.put("fieldValue",sku);


                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","cksl");
                    workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rksl");
                    workflowRequestTableFields3.put("fieldValue",transferred_quantity);

                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","fckrq");
                    workflowRequestTableFields4.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields5 = new JSONObject();
                    workflowRequestTableFields5.put("fieldName","fckrq");
                    workflowRequestTableFields5.put("fieldValue",todayString);


                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields5);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getTrfOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("调拨单回传-数据错误");
            }
        }else{
            log.info("当前状态不是完成状态，暂不执行提交");
        }
        return "success";
    }

    /*寄售进仓单-回传接口*/
    @PostMapping("getReConOrderCB")
    public String getReConOrderCB(@RequestBody GetReConOrderCBDto getReConOrderCBDto){
        log.info("getReConOrderCBDto="+getReConOrderCBDto.toString());
        //进仓单号
        String title = getReConOrderCBDto.getTitle();

        //状态
        String status = getReConOrderCBDto.getStatus();

        if("done".equals(status)){
            //
            QueryWrapper<FlTrfOrderFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlTrfOrderFormTableMain trfOrderFormTableMain = trfOrderFormTableMainService.getOne(queryWrapper);

            JSONObject jsonObject = new JSONObject();

            if(trfOrderFormTableMain != null){
                String requestId = trfOrderFormTableMain.getRequestid();
                log.info("requestId="+requestId);

                jsonObject.put("requestId", requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                List<ReConsOrderItem> reConsOrderItems = getReConOrderCBDto.getItems();

                for (ReConsOrderItem reConsOrderItem : reConsOrderItems){


                    String transferred_quantity = reConsOrderItem.getReceived_pcs();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = reConsOrderItem.getSku();
                    workflowRequestTableFields1.put("fieldName","hpbh");
                    workflowRequestTableFields1.put("fieldValue",sku);


                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","cksl");
                    workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","rksl");
                    workflowRequestTableFields3.put("fieldValue",transferred_quantity);

                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","fckrq");
                    workflowRequestTableFields4.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields5 = new JSONObject();
                    workflowRequestTableFields5.put("fieldName","fckrq");
                    workflowRequestTableFields5.put("fieldValue",todayString);


                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields5);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getTrfOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("调拨单回传-数据错误");
            }
        }else {
            log.info("当前状态不是完成状态，暂不执行提交");
        }

        return "success";
    }


    /*采购入库进仓单-回传接口*/
    @PostMapping("getPurchaseInCB")
    public String getPurInCB(@RequestBody GetPurInCBDto reqDto){

        log.info("reqDto="+ reqDto.toString());

        //进仓单
        String title = reqDto.getTitle();

        /*订单类型*/
        String status = reqDto.getStatus();

        if("done".equals(status)){
            QueryWrapper<FlPurInFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlPurInFormTableMain purInFormTableMain = flPurInFormTableMainService.getOne(queryWrapper);

            JSONObject jsonObject = new JSONObject();

            if(purInFormTableMain != null){
                String requestId = purInFormTableMain.getRequestId();
                log.info("requestId="+requestId);
                jsonObject.put("requestId",requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                //采购入库明细
                List<PurchaseInItem> purchaseInItems  = reqDto.getItems();

                for (PurchaseInItem purchaseInItem : purchaseInItems){

                    String received_pcs = purchaseInItem.getReceived_pcs();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = purchaseInItem.getSku();
                    workflowRequestTableFields1.put("fieldName","wlbm");
                    workflowRequestTableFields1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","rksl");
                    workflowRequestTableFields2.put("fieldValue",received_pcs);

                    //获取当时日期
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);
                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    log.info("todayString="+todayString);
                    workflowRequestTableFields3.put("fieldName","rkrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    String batch = purchaseInItem.getBatch();
                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","pc");
                    workflowRequestTableFields4.put("fieldValue",batch);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);

                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getPurInDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("采购回传-数据错误");
            }
        }else {
            log.info("当前状态不是完成状态，暂不执行提交");
        }
        return "success";
    }

    @PostMapping("getRePurchaseCB")
    public String getRePurchaseCB(@RequestBody GetOrderCBDto reqDto){

        log.info("reqDto="+ reqDto.toString());

        /*采购退单*/
        String title = reqDto.getName();

        //状态
        String status = reqDto.getStatus();

        JSONObject jsonObject = new JSONObject();

        if("done".equals(status)){

            //物流单号
            String trackingNumber = "物流单号为空";

            //物流公司
            String shippingType = "物流公司为空";

            if(reqDto.getShippings().size()>0){
                trackingNumber = reqDto.getShippings().get(0).getTracking_number();
            }

            if(reqDto.getShippings().size()>0){
                shippingType = reqDto.getShippings().get(0).getShipping_type();
            }


            QueryWrapper<FlRePurOrderFormTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlRePurOrderFormTableMain flRePurOrderFormTableMain = flRePurOrderFormTableMainService.getOne(queryWrapper);

            if(flRePurOrderFormTableMain != null){

                String requestId = flRePurOrderFormTableMain.getRequestId();
                log.info("requestId="+requestId);

                jsonObject.put("requestId", requestId);

                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                JSONArray workflowRequestTableRecordsArr = new JSONArray();

                //订单明细
                List<Product> products = reqDto.getProducts();

                for(Product product : products){

                    String transferred_quantity = product.getShipped_quantity();

                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();
                    String sku = product.getSku();
                    workflowRequestTableFields1.put("fieldName","wlbm");
                    workflowRequestTableFields1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","cksl");
                    workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                    //获取当时日期
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayString = today.format(formatter);
                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    log.info("todayString="+todayString);
                    workflowRequestTableFields3.put("fieldName","ckrq");
                    workflowRequestTableFields3.put("fieldValue",todayString);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","wldh");
                    workflowRequestTableFields4.put("fieldValue",trackingNumber);


                    JSONObject workflowRequestTableFields5 = new JSONObject();
                    workflowRequestTableFields5.put("fieldName","wlgs");
                    workflowRequestTableFields5.put("fieldValue",shippingType);

                    workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields4);
                    workflowRequestTableFieldsArr.add(workflowRequestTableFields5);

                    workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                    workflowRequestTableRecords.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
                }

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getRePurDtlTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());
                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }else {
                log.info("采购回传-数据错误");
            }
        }else {
            log.info("当前状态不是完成状态，暂不执行提交");
        }
        return "success";
    }

    /*组装拆卸订单-回传接口*/
    @PostMapping("getSetDismantleCB")
    public String getSetDismantleCB(@RequestBody GetProcessCBDto dto){

        String title = dto.getTitle();

        String status = dto.getStatus();

        if("done".equals(status)){

            QueryWrapper<FlSetTableMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlSetTableMain flSetTableMain = flSetTableMainService.getOne(queryWrapper);

            if(flSetTableMain != null){
                String requestId = flSetTableMain.getRequestId();
                log.info("requestId="+requestId);

                List<ProcessItem> items = dto.getItems();

                Map<String,Integer> resMap = new HashMap<>();

                //汇总回传数据
                for(ProcessItem item : items){
                    String sku = item.getSku();
                    Integer receivedPcs = item.getReceived_pcs();

                    if(resMap.containsKey(sku)){
                        Integer currentReceive = resMap.get(sku);

                        currentReceive = currentReceive + receivedPcs;

                        resMap.put(sku,currentReceive);
                    }else {
                        resMap.put(sku,receivedPcs);
                    }
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requestId",requestId);


                JSONArray mainDataArr = new JSONArray();
                JSONObject mainData1 = new JSONObject();

                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String todayString = today.format(formatter);


                mainData1.put("fieldName","sjwcsj");
                mainData1.put("fieldValue",todayString);

                mainDataArr.add(mainData1);

                jsonObject.put("mainData",mainDataArr);


                JSONArray workflowRequestTableRecordsArr = new JSONArray();
                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                for (String sku : resMap.keySet()){
                    Integer receivedPcs = resMap.get(sku);

                    JSONObject workflowRequestTableRecords0 = new JSONObject();
                    JSONArray workflowRequestTableFieldsArr0 = new JSONArray();
                    Integer recordOrder = 0;

                    JSONObject workflowRequestTableFields0_1 = new JSONObject();

                    workflowRequestTableFields0_1.put("fieldName","spbm");
                    workflowRequestTableFields0_1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields0_2 = new JSONObject();
                    workflowRequestTableFields0_2.put("fieldName","sjrksl");
                    workflowRequestTableFields0_2.put("fieldValue",receivedPcs);

                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_1);
                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_2);

                    workflowRequestTableRecords0.put("workflowRequestTableFields",workflowRequestTableFieldsArr0);
                    workflowRequestTableRecords0.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords0);
                }
                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getSetDtlTable3());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);
                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }
        }
        return "success";
    }


    /*转码订单-回传接口*/
    @PostMapping("getTransCodeCB")
    public String getTransCodeCB(@RequestBody GetProcessCBDto dto){

        String title = dto.getTitle();

        String status = dto.getStatus();

        if("done".equals(status)){
            QueryWrapper<FlTransCodeMain> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("lcbh",title);
            FlTransCodeMain flTransCodeMain = flTransCodeMainService.getOne(queryWrapper);

            if (flTransCodeMain != null){
                String requestId = flTransCodeMain.getRequestid();
                log.info("requestId="+requestId);

                List<ProcessItem> items = dto.getItems();

                //取消汇总逻辑
                //Map<String,Integer> resMap = new HashMap<>();

                //汇总回传数据
                /*for(ProcessItem item : items){
                    String sku = item.getSku();
                    Integer receivedPcs = item.getReceived_pcs();

                    if(resMap.containsKey(sku)){
                        Integer currentReceive = resMap.get(sku);

                        currentReceive = currentReceive + receivedPcs;

                        resMap.put(sku,currentReceive);
                    }else {
                        resMap.put(sku,receivedPcs);
                    }
                }*/

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("requestId",requestId);


                JSONArray mainDataArr = new JSONArray();
                JSONObject mainData1 = new JSONObject();

                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String todayString = today.format(formatter);


                mainData1.put("fieldName","rkrq");
                mainData1.put("fieldValue",todayString);

                mainDataArr.add(mainData1);

                jsonObject.put("mainData",mainDataArr);

                JSONArray workflowRequestTableRecordsArr = new JSONArray();
                JSONArray detailDataArr = new JSONArray();
                JSONObject detailData = new JSONObject();

                for (ProcessItem item : items){

                    Integer receivedPcs = item.getReceived_pcs();

                    String sku = item.getSku();

                    String batch = item.getBatch();
                    String expirationDate = item.getExpiration_date();

                    JSONObject workflowRequestTableRecords0 = new JSONObject();
                    JSONArray workflowRequestTableFieldsArr0 = new JSONArray();
                    Integer recordOrder = 0;

                    JSONObject workflowRequestTableFields0_1 = new JSONObject();

                    workflowRequestTableFields0_1.put("fieldName","hpbhtw");
                    workflowRequestTableFields0_1.put("fieldValue",sku);

                    JSONObject workflowRequestTableFields0_2 = new JSONObject();
                    workflowRequestTableFields0_2.put("fieldName","fhcrksl");
                    workflowRequestTableFields0_2.put("fieldValue",receivedPcs);

                    JSONObject workflowRequestTableFields0_3 = new JSONObject();
                    workflowRequestTableFields0_3.put("fieldName","pc");
                    workflowRequestTableFields0_3.put("fieldValue",batch);

                    JSONObject workflowRequestTableFields0_4 = new JSONObject();
                    workflowRequestTableFields0_4.put("fieldName","xq");
                    workflowRequestTableFields0_4.put("fieldValue",expirationDate);

                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_1);
                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_2);
                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_3);
                    workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_4);

                    workflowRequestTableRecords0.put("workflowRequestTableFields",workflowRequestTableFieldsArr0);
                    workflowRequestTableRecords0.put("recordOrder",recordOrder);

                    workflowRequestTableRecordsArr.add(workflowRequestTableRecords0);
                }
                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getTranCodeDtlTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);
                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }
        }
        return "success";
    }




    @GetMapping("getTest")
    public String getTest(){
        log.info("hello world!");
        return "hello world!";
    }



    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
