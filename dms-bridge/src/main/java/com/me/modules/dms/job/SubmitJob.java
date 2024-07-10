//package com.me.modules.dms.job;
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.me.common.annotation.Elk;
//import com.me.common.config.DmsConfig;
//import com.me.common.utils.DmsUtil;
//import com.me.modules.fl.entity.FlDismantleReqLog;
//import com.me.modules.fl.entity.FlSetOrderReqLog;
//import com.me.modules.fl.entity.FlTransCodeReqLog;
//import com.me.modules.fl.service.FlDismantleReqLogService;
//import com.me.modules.fl.service.FlSetOrderReqLogService;
//import com.me.modules.fl.service.FlTransCodeReqLogService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.List;
//
//@Configuration
//@Slf4j
//@AllArgsConstructor
////@EnableScheduling
//public class SubmitJob {
//
//    private FlSetOrderReqLogService flSetOrderReqLogService;
//
//    private FlDismantleReqLogService flDismantleReqLogService;
//
//    private FlTransCodeReqLogService flTransCodeReqLogService;
//
//    private DmsConfig dmsConfig;
//
//    @Elk
//    @Scheduled(cron = "0 */2 * * * *")
//    public void submitSet(){
//        log.info("组套提交流程开始!");
//        QueryWrapper logQueryWrapper = new QueryWrapper();
//        logQueryWrapper.eq("son_status",1);
//        logQueryWrapper.eq("fr_status",1);
//        logQueryWrapper.isNotNull("fr_ret_params");
//        logQueryWrapper.isNotNull("son_ret_params");
//        logQueryWrapper.ne("fr_ret_params","");
//        logQueryWrapper.ne("son_ret_params","");
//
//        List<FlSetOrderReqLog> logs = flSetOrderReqLogService.list(logQueryWrapper);
//
//        if(logs.size()>0){
//            for(FlSetOrderReqLog reqLog : logs){
//                String sonRetParams = reqLog.getSonRetParams();
//                String frRetParams = reqLog.getFrRetParams();
//
//                if(StrUtil.isNotEmpty(sonRetParams) & StrUtil.isNotEmpty(frRetParams)){
//                    JSONArray detailDataArr = new JSONArray();
//                    JSONObject detailData = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//
//                    String requestId = reqLog.getRequestId();
//
//                    jsonObject.put("requestId",requestId);
//
//                    JSONArray workflowRequestTableRecordsArr = new JSONArray();
//
//                    JSONObject frRetJson = JSONObject.parseObject(frRetParams);
//                    JSONArray frProducts = frRetJson.getJSONArray("items");
//
//                    JSONObject sonRetJson = JSONObject.parseObject(sonRetParams);
//
//                    JSONArray sonProducts = sonRetJson.getJSONArray("products");
//
//                    //父项明细
//                    for (int i =0;i<frProducts.size();i++){
//
//                        JSONObject workflowRequestTableRecords0 = new JSONObject();
//
//                        JSONArray workflowRequestTableFieldsArr0 = new JSONArray();
//                        Integer recordOrder = 0;
//
//                        JSONObject workflowRequestTableFields0_1 = new JSONObject();
//                        JSONObject frProduct = frProducts.getJSONObject(i);
//                        String sku = frProduct.getString("sku");
//
//                        workflowRequestTableFields0_1.put("fieldName","spbm");
//                        workflowRequestTableFields0_1.put("fieldValue",sku);
//
//
//                        JSONObject workflowRequestTableFields0_2 = new JSONObject();
//                        //父项入库数量
//                        String shippedQuantity = frProduct.getString("received_pcs");
//
//                        workflowRequestTableFields0_2.put("fieldName","sjrksl");
//                        workflowRequestTableFields0_2.put("fieldValue",shippedQuantity);
//
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_1);
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_2);
//
//                        workflowRequestTableRecords0.put("workflowRequestTableFields",workflowRequestTableFieldsArr0);
//                        workflowRequestTableRecords0.put("recordOrder",recordOrder);
//
//                        workflowRequestTableRecordsArr.add(workflowRequestTableRecords0);
//                    }
//
//
//                    //子项明细
//                    for(int i = 0;i<sonProducts.size();i++){
//                        JSONObject workflowRequestTableRecords1 = new JSONObject();
//                        JSONArray workflowRequestTableFieldsArr1 = new JSONArray();
//
//                        Integer recordOrder = 0;
//
//                        JSONObject workflowRequestTableFields1_1 = new JSONObject();
//
//                        JSONObject sonProduct = sonProducts.getJSONObject(i);
//
//                        String sku = sonProduct.getString("sku");
//                        workflowRequestTableFields1_1.put("fieldName","spbm");
//                        workflowRequestTableFields1_1.put("fieldValue",sku);
//
//                        JSONObject workflowRequestTableFields1_2 = new JSONObject();
//                        String shippedQuantity = sonProduct.getString("shipped_quantity");
//                        workflowRequestTableFields1_2.put("fieldName","sjcksl");
//                        workflowRequestTableFields1_2.put("fieldValue",shippedQuantity);
//
//                        workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_1);
//                        workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_2);
//
//                        workflowRequestTableRecords1.put("workflowRequestTableFields",workflowRequestTableFieldsArr1);
//                        workflowRequestTableRecords1.put("recordOrder",recordOrder);
//
//                        workflowRequestTableRecordsArr.add(workflowRequestTableRecords1);
//                    }
//
//                    detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
//                    detailData.put("tableDBName",dmsConfig.getSetDtlTable3());
//                    detailDataArr.add(detailData);
//
//                    jsonObject.put("detailData",detailDataArr);
//
//                    log.info(jsonObject.toJSONString());
//
//                    try {
//                        DmsUtil.testRegist(dmsConfig.getIp());
//                        DmsUtil.testGetoken(dmsConfig.getIp());
//                        String reStr = DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
//
//                        JSONObject reJson = JSONObject.parseObject(reStr);
//
//                        String code = reJson.getString("code");
//
//                        if("SUCCESS".equals(code)){
//                            UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                            logUpdateWrapper.eq("requestId",requestId);
//                            logUpdateWrapper.set("fr_status","4");
//                            logUpdateWrapper.set("son_status","4");
//                            logUpdateWrapper.set("fr_message","提交成功");
//                            logUpdateWrapper.set("son_message","提交成功");
//                            flSetOrderReqLogService.update(logUpdateWrapper);
//                        }else {
//                            UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                            logUpdateWrapper.eq("requestId",requestId);
//                            logUpdateWrapper.set("fr_status","5");
//                            logUpdateWrapper.set("son_status","5");
//                            logUpdateWrapper.set("fr_message","提交不成功");
//                            logUpdateWrapper.set("son_message","提交不成功");
//
//                            flSetOrderReqLogService.update(logUpdateWrapper);
//                        }
//
//
//
//                    }catch (Exception e){
//                        UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                        logUpdateWrapper.eq("requestId",requestId);
//                        logUpdateWrapper.set("fr_status","5");
//                        logUpdateWrapper.set("son_status","5");
//                        logUpdateWrapper.set("fr_message","提交不成功");
//                        logUpdateWrapper.set("son_message","提交不成功");
//
//                        flSetOrderReqLogService.update(logUpdateWrapper);
//                    }
//                }else {
//                    log.info("数据不全暂不提交数据");
//                }
//            }
//        }else {
//            log.info("没有可提交的数据，暂不提交组套流程");
//        }
//
//
//        log.info("组套提交流程结束!");
//    }
//
//    @Elk
//    @Scheduled(cron = "0 */2 * * * *")
//    public void submitDismantle(){
//        log.info("拆卸提交流程开始!");
//        QueryWrapper logQueryWrapper = new QueryWrapper();
//        logQueryWrapper.eq("son_status",1);
//        logQueryWrapper.eq("fr_status",1);
//        logQueryWrapper.isNotNull("fr_ret_params");
//        logQueryWrapper.isNotNull("son_ret_params");
//        logQueryWrapper.ne("fr_ret_params","");
//        logQueryWrapper.ne("son_ret_params","");
//
//        List<FlDismantleReqLog> logs = flDismantleReqLogService.list(logQueryWrapper);
//
//        if(logs.size()>0){
//            for (FlDismantleReqLog reqLog: logs){
//                String sonRetParams = reqLog.getSonRetParams();
//                String frRetParams = reqLog.getFrRetParams();
//
//                if(StrUtil.isNotEmpty(sonRetParams) & StrUtil.isNotEmpty(frRetParams)){
//
//                    JSONArray detailDataArr = new JSONArray();
//                    JSONObject detailData = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//
//                    String requestId = reqLog.getRequestId();
//
//                    jsonObject.put("requestId",requestId);
//
//                    JSONArray workflowRequestTableRecordsArr = new JSONArray();
//
//                    JSONObject frRetJson = JSONObject.parseObject(frRetParams);
//                    JSONArray frProducts = frRetJson.getJSONArray("products");
//
//                    JSONObject sonRetJson = JSONObject.parseObject(sonRetParams);
//
//                    JSONArray sonProducts = sonRetJson.getJSONArray("items");
//
//                    //父项明细
//                    for (int i =0;i<frProducts.size();i++){
//
//                        JSONObject workflowRequestTableRecords0 = new JSONObject();
//
//                        JSONArray workflowRequestTableFieldsArr0 = new JSONArray();
//                        Integer recordOrder = 0;
//
//                        JSONObject workflowRequestTableFields0_1 = new JSONObject();
//                        JSONObject frProduct = frProducts.getJSONObject(i);
//                        String sku = frProduct.getString("sku");
//
//                        workflowRequestTableFields0_1.put("fieldName","spbm");
//                        workflowRequestTableFields0_1.put("fieldValue",sku);
//
//
//                        JSONObject workflowRequestTableFields0_2 = new JSONObject();
//                        //父项入库数量
//                        String shippedQuantity = frProduct.getString("shipped_quantity");
//
//                        workflowRequestTableFields0_2.put("fieldName","sjcksl");
//                        workflowRequestTableFields0_2.put("fieldValue",shippedQuantity);
//
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_1);
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_2);
//
//                        workflowRequestTableRecords0.put("workflowRequestTableFields",workflowRequestTableFieldsArr0);
//                        workflowRequestTableRecords0.put("recordOrder",recordOrder);
//
//                        workflowRequestTableRecordsArr.add(workflowRequestTableRecords0);
//                    }
//
//                    //子项明细
//                    for(int i = 0;i<sonProducts.size();i++){
//                        JSONObject workflowRequestTableRecords1 = new JSONObject();
//                        JSONArray workflowRequestTableFieldsArr1 = new JSONArray();
//
//                        Integer recordOrder = 0;
//
//                        JSONObject workflowRequestTableFields1_1 = new JSONObject();
//
//                        JSONObject sonProduct = sonProducts.getJSONObject(i);
//
//                        String sku = sonProduct.getString("sku");
//                        workflowRequestTableFields1_1.put("fieldName","spbm");
//                        workflowRequestTableFields1_1.put("fieldValue",sku);
//
//                        JSONObject workflowRequestTableFields1_2 = new JSONObject();
//                        String shippedQuantity = sonProduct.getString("received_pcs");
//                        workflowRequestTableFields1_2.put("fieldName","sjrksl");
//                        workflowRequestTableFields1_2.put("fieldValue",shippedQuantity);
//
//                        workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_1);
//                        workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_2);
//
//                        workflowRequestTableRecords1.put("workflowRequestTableFields",workflowRequestTableFieldsArr1);
//                        workflowRequestTableRecords1.put("recordOrder",recordOrder);
//
//                        workflowRequestTableRecordsArr.add(workflowRequestTableRecords1);
//
//                    }
//
//                    detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
//                    detailData.put("tableDBName",dmsConfig.getSetDtlTable3());
//                    detailDataArr.add(detailData);
//
//                    jsonObject.put("detailData",detailDataArr);
//
//                    log.info(jsonObject.toJSONString());
//
//                    try {
//                        DmsUtil.testRegist(dmsConfig.getIp());
//                        DmsUtil.testGetoken(dmsConfig.getIp());
//                        String reStr = DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
//
//                        JSONObject reJson = JSONObject.parseObject(reStr);
//
//                        String code = reJson.getString("code");
//
//                        if("SUCCESS".equals(code)){
//                            UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                            logUpdateWrapper.eq("requestId",requestId);
//                            logUpdateWrapper.set("fr_status","4");
//                            logUpdateWrapper.set("son_status","4");
//                            logUpdateWrapper.set("fr_message","提交成功");
//                            logUpdateWrapper.set("son_message","提交成功");
//                            flDismantleReqLogService.update(logUpdateWrapper);
//                        }else{
//                            UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                            logUpdateWrapper.eq("requestId",requestId);
//                            logUpdateWrapper.set("fr_status","5");
//                            logUpdateWrapper.set("son_status","5");
//                            logUpdateWrapper.set("fr_message","提交不成功");
//                            logUpdateWrapper.set("son_message","提交不成功");
//
//                            flDismantleReqLogService.update(logUpdateWrapper);
//                        }
//                    }catch (Exception e){
//                        UpdateWrapper logUpdateWrapper = new UpdateWrapper();
//                        logUpdateWrapper.eq("requestId",requestId);
//                        logUpdateWrapper.set("fr_status","5");
//                        logUpdateWrapper.set("son_status","5");
//                        logUpdateWrapper.set("fr_message","提交不成功");
//                        logUpdateWrapper.set("son_message","提交不成功");
//
//                        flDismantleReqLogService.update(logUpdateWrapper);
//                    }
//                }
//            }
//        }else {
//            log.info("没有可提交的数据，暂不提交拆卸流程");
//        }
//
//
//
//        log.info("拆卸提交流程结束!");
//    }
//
//    @Elk
//    @Scheduled(cron = "0 */2 * * * *")
//    public void submitTransCode(){
//        log.info("转码提交流程开始!");
//        QueryWrapper logQueryWrapper = new QueryWrapper();
//        logQueryWrapper.eq("fr_status",1);
//        logQueryWrapper.isNotNull("fr_ret_params");
//        logQueryWrapper.isNull("son_ret_params");
//        logQueryWrapper.isNotNull("fr_params");
//        logQueryWrapper.isNull("son_params");
//
//        List<FlTransCodeReqLog> frLogs = flTransCodeReqLogService.list(logQueryWrapper);
//
//
//        if(frLogs.size()>0){
//            for(FlTransCodeReqLog reqFrLog : frLogs){
//
//                String requestId = reqFrLog.getRequestId();
//
//                String frRetParams = reqFrLog.getFrRetParams();
//
//                if(StrUtil.isNotEmpty(frRetParams)){
//
//                    JSONArray detailDataArr = new JSONArray();
//                    JSONObject detailData = new JSONObject();
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("requestId",requestId);
//
//                    JSONArray workflowRequestTableRecordsArr = new JSONArray();
//
//                    JSONObject frRetJson = JSONObject.parseObject(frRetParams);
//                    JSONArray frProducts = frRetJson.getJSONArray("products");
//
//                    Integer recordOrder = 0;
//
//                    //父项明细
//                    for (int i =0;i<frProducts.size();i++){
//
//                        JSONObject workflowRequestTableRecords0 = new JSONObject();
//
//                        JSONArray workflowRequestTableFieldsArr0 = new JSONArray();
//
//
//                        //父项 sku
//                        JSONObject workflowRequestTableFields0_1 = new JSONObject();
//                        JSONObject frProduct = frProducts.getJSONObject(i);
//                        String sku = frProduct.getString("sku");
//
//                        workflowRequestTableFields0_1.put("fieldName","hpbh");
//                        workflowRequestTableFields0_1.put("fieldValue",sku);
//
//                        //父项出库数量
//                        JSONObject workflowRequestTableFields0_2 = new JSONObject();
//
//                        String shippedQuantity = frProduct.getString("shipped_quantity");
//
//                        workflowRequestTableFields0_2.put("fieldName","cksl");
//                        workflowRequestTableFields0_2.put("fieldValue",shippedQuantity);
//
//
//                        //父项出库仓库
//                        JSONObject workflowRequestTableFields0_3 = new JSONObject();
//                        String productStorageType = frProduct.getString("product_storage_type");
//                        workflowRequestTableFields0_3.put("fieldName","ck");
//                        workflowRequestTableFields0_3.put("fieldValue",productStorageType);
//
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_1);
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_2);
//                        workflowRequestTableFieldsArr0.add(workflowRequestTableFields0_3);
//
//                        workflowRequestTableRecords0.put("workflowRequestTableFields",workflowRequestTableFieldsArr0);
//                        workflowRequestTableRecords0.put("recordOrder",recordOrder);
//
//                        workflowRequestTableRecordsArr.add(workflowRequestTableRecords0);
//                    }
//
//
//                    QueryWrapper<FlTransCodeReqLog> logSonQuery = new QueryWrapper();
//
//                    logSonQuery.eq("requestId",requestId)
//                            .eq("son_status",1)
//                            .isNull("fr_params")
//                            .isNotNull("son_params");
//
//
//                    FlTransCodeReqLog sonLog = flTransCodeReqLogService.getOne(logSonQuery);
//
//                    if(sonLog != null) {
//                        String sonRetParams = sonLog.getSonRetParams();
//
//                        JSONObject sonRetJson = JSONObject.parseObject(sonRetParams);
//
//                        JSONArray sonProducts = sonRetJson.getJSONArray("items");
//
//
//                        //子项明细
//                        for (int j = 0; j < sonProducts.size(); j++) {
//                            JSONObject workflowRequestTableRecords1 = new JSONObject();
//                            JSONArray workflowRequestTableFieldsArr1 = new JSONArray();
//
//                            JSONObject workflowRequestTableFields1_1 = new JSONObject();
//
//                            JSONObject sonProduct = sonProducts.getJSONObject(j);
//
//                            String sonSku = sonProduct.getString("sku");
//                            workflowRequestTableFields1_1.put("fieldName", "hpbh");
//                            workflowRequestTableFields1_1.put("fieldValue", sonSku);
//
//                            JSONObject workflowRequestTableFields1_2 = new JSONObject();
//                            String receivedPcs = sonProduct.getString("received_pcs");
//                            workflowRequestTableFields1_2.put("fieldName", "rksl");
//                            workflowRequestTableFields1_2.put("fieldValue", receivedPcs);
//
//                            //子项入库仓库
//                            JSONObject workflowRequestTableFields1_3 = new JSONObject();
//                            String storageType = sonProduct.getString("storage_type");
//                            workflowRequestTableFields1_3.put("fieldName","ck");
//                            workflowRequestTableFields1_3.put("fieldValue",storageType);
//
//
//
//                            workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_1);
//                            workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_2);
//                            workflowRequestTableFieldsArr1.add(workflowRequestTableFields1_3);
//
//                            workflowRequestTableRecords1.put("workflowRequestTableFields", workflowRequestTableFieldsArr1);
//                            workflowRequestTableRecords1.put("recordOrder", recordOrder);
//
//                            workflowRequestTableRecordsArr.add(workflowRequestTableRecords1);
//                        }
//
//                        detailData.put("workflowRequestTableRecords", workflowRequestTableRecordsArr);
//                        detailData.put("tableDBName", dmsConfig.getTranCodeDtlTable2());
//                        detailDataArr.add(detailData);
//
//                        jsonObject.put("detailData", detailDataArr);
//
//                        log.info(jsonObject.toJSONString());
//
//                        try {
//                                DmsUtil.testRegist(dmsConfig.getIp());
//                                DmsUtil.testGetoken(dmsConfig.getIp());
//                                String reStr = DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
//
//                                JSONObject reJson = JSONObject.parseObject(reStr);
//
//                                String code = reJson.getString("code");
//
//                                if("SUCCESS".equals(code)){
//                                    UpdateWrapper<FlTransCodeReqLog> logFrUpdate = new UpdateWrapper();
//                                    logFrUpdate.eq("requestId",requestId)
//                                            .isNotNull("fr_params")
//                                            .set("fr_status","4")
//                                            .set("fr_message","提交成功");
//                                    flTransCodeReqLogService.update(logFrUpdate);
//
//                                    UpdateWrapper<FlTransCodeReqLog> sonFrUpdate = new UpdateWrapper();
//                                    sonFrUpdate.eq("requestId",requestId)
//                                            .isNotNull("son_params")
//                                            .set("son_status","4")
//                                            .set("son_message","提交成功");
//                                    flTransCodeReqLogService.update(sonFrUpdate);
//                                }else {
//                                    UpdateWrapper<FlTransCodeReqLog> logFrUpdate = new UpdateWrapper();
//                                    logFrUpdate.eq("requestId",requestId)
//                                            .isNotNull("fr_params")
//                                            .set("fr_status","5")
//                                            .set("fr_message","提交不成功");
//                                    flTransCodeReqLogService.update(logFrUpdate);
//
//                                    UpdateWrapper<FlTransCodeReqLog> sonFrUpdate = new UpdateWrapper();
//                                    sonFrUpdate.eq("requestId",requestId)
//                                            .isNotNull("son_params")
//                                            .set("son_status","5")
//                                            .set("son_message","提交不成功");
//                                    flTransCodeReqLogService.update(sonFrUpdate);
//                                }
//                            }catch (Exception e){
//                                UpdateWrapper<FlTransCodeReqLog> logFrUpdate = new UpdateWrapper();
//                                logFrUpdate.eq("requestId",requestId)
//                                        .isNotNull("fr_params")
//                                        .set("fr_status","5")
//                                        .set("fr_message","提交不成功");
//                                flTransCodeReqLogService.update(logFrUpdate);
//
//                                UpdateWrapper<FlTransCodeReqLog> sonFrUpdate = new UpdateWrapper();
//                                sonFrUpdate.eq("requestId",requestId)
//                                        .isNotNull("son_params")
//                                        .set("son_status","5")
//                                        .set("son_message","提交不成功");
//                                flTransCodeReqLogService.update(sonFrUpdate);
//                            }
//
//                    }else {
//                        log.info("缺失子项参数，暂不提交转码流程");
//                    }
//                }else {
//                    log.info("缺失父项参数，暂不提交转码流程");
//                }
//            }
//        }else {
//            log.info("没有可提交的数据，暂不提交转码流程");
//        }
//        log.info("转码提交流程结束!");
//    }
//}
