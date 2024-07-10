package com.me.modules.bi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.config.ByteNewConfig;
import com.me.common.exception.BusinessException;
import com.me.modules.bi.entity.*;
import com.me.modules.bi.service.*;
import com.me.modules.http.service.ByteNewHttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class TransByteNewDataServiceImpl implements TransByteNewDataService {

    private ByteNewHttpService byteNewHttpService;

    private IssueTypeService issueTypeService;

    private ShopService shopService;

    private WareHouseService wareHouseService;

    private ProcessingResultService processingResultService;

    private StoreAfSalesExceptionService storeAfSalesExceptionService;

    private ByteNewConfig byteNewConfig;

    private StoreAfSalesExceptionProductService storeAfSalesExceptionProductService;

    private StoreAfSalesExceptionSubOrderService storeAfSalesExceptionSubOrderService;

    private ByteNewService byteNewService;

    private ByteNewUserService byteNewUserService;

    private AllergyReactionOrderService allergyReactionOrderService;

    private AllergicSymptomsService allergicSymptomsService;

    private AllergyReactionOrderProductService allergyReactionOrderProductService;

    private ReissueReasonService reissueReasonService;

    private ReissueOrderService reissueOrderService;

    private ByteNewEumService byteNewEumService;

    private ReissueOrderProductService reissueOrderProductService;



    //获取枚举
    final String app_key = "3331225291";
    final String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
    //359 仓库售后异常等级表  558 过敏订单 1349 补发单
    final String project_id = "359";
    final String app_secret = "71dc01248f11697674f3e610971c3013";



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Integer> transStoreAfSalesException() {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", project_id);
        //params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("columnResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        Map<String,Integer> map = new HashMap<>();

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<StoreAfSalesExceptionSubOrder> storeAfSalesExceptionSubOrderList = new ArrayList<>();
        List<StoreAfSalesException> insertStoreAfSalesExceptionList = new ArrayList<>();
        List<StoreAfSalesException> updateStoreAfSalesExceptionList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            StoreAfSalesException storeAfSalesException = new StoreAfSalesException();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        storeAfSalesException.setCreator(byteNewUser.getNick());
                    }else {
                        storeAfSalesException.setCreator(dataElement.get(key).toString());
                    }
                }
                if("3".equals(key)){
                    storeAfSalesException.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    storeAfSalesException.setModifyTime(dataElement.get(key).toString());
                }
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        storeAfSalesException.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        storeAfSalesException.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        storeAfSalesException.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        storeAfSalesException.setTaskStatus("暂停中");
                    }
                }
                //仓库 数组 需要db数据库初始化
                if("456".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<WareHouse> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(WareHouse::getId,value);
                    List<WareHouse> wareHouseList = wareHouseService.list(queryWrapper);
                    if(wareHouseList.size()>0){
                        String wareHouseName = wareHouseList.get(0).getWareHouseName();
                        if(StrUtil.isNotEmpty(wareHouseName)){
                            storeAfSalesException.setWarehouse(wareHouseName);
                        }else {
                            storeAfSalesException.setWarehouse("未匹配到数据");
                        }
                    }else {
                        storeAfSalesException.setWarehouse("未匹配到数据");
                    }
                }
                if("8".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        storeAfSalesException.setTaskFinisher(byteNewUser.getNick());
                    }else {
                        storeAfSalesException.setTaskFinisher(dataElement.get(key).toString());
                    }
                }
                if("457".equals(key)){
                    storeAfSalesException.setWaybillNumber(dataElement.get(key).toString());
                }
                if("1737".equals(key)){
                    storeAfSalesException.setCompensationAmount(new BigDecimal(dataElement.get(key).toString()));
                }
                if("9".equals(key)){
                    storeAfSalesException.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("458".equals(key)){
                    storeAfSalesException.setActualPaymentAmount(new BigDecimal(dataElement.get(key).toString()));
                }
                if("459".equals(key)){
                    String value = dataElement.get(key).toString();
                    List<String> ids = Arrays.asList(value.split(","));
                    LambdaQueryWrapper<IssueType> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(IssueType::getId,ids);
                    List<IssueType> issueTypeList = issueTypeService.list(queryWrapper);
                    String issueTypeStr = "";
                    for(int i1 =0;i1<issueTypeList.size();i1++){
                        issueTypeStr = issueTypeStr + issueTypeList.get(i1).getDescription() + "-";
                        if(i1 == issueTypeList.size()-1){
                            issueTypeStr = issueTypeStr.substring(0, issueTypeStr.length() - 1);
                        }
                    }
                    storeAfSalesException.setIssueType(issueTypeStr);
                }
                //处理结果 数组 需要数据初始化
                if("460".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<ProcessingResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(ProcessingResult::getId,dataElement.get(key).toString());
                    List<ProcessingResult> processingResults = processingResultService.list(queryWrapper);
                    if(processingResults.size()>0){
                        for(ProcessingResult processingResult : processingResults){
                            if(processingResult.getId().equals(dataElement.get(key).toString())){
                                storeAfSalesException.setProcessingResult(processingResult.getDescription());
                            }else{
                                storeAfSalesException.setProcessingResult("未匹配到数据");
                            }
                        }
                    }else {
                        storeAfSalesException.setProcessingResult("未匹配到数据");
                    }
                }
                if("461".equals(key)){
                    storeAfSalesException.setDescription(dataElement.get(key).toString());
                }
                if("9".equals(key)){
                    storeAfSalesException.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("462".equals(key)){
                    storeAfSalesException.setImage(dataElement.get(key).toString());
                }
                //店铺 需要配置到数据库
                if("373".equals(key)){
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                storeAfSalesException.setShop(shop.getShopName());
                            }else{
                                storeAfSalesException.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        storeAfSalesException.setShop("未匹配到数据");
                    }
                }
                if("374".equals(key)){
                    storeAfSalesException.setOrderNumber(dataElement.get(key).toString());
                }
                if("375".equals(key)){
                    storeAfSalesException.setBuyerNickname(dataElement.get(key).toString());
                }
                if("377".equals(key)){
                    storeAfSalesException.setRecipientName(dataElement.get(key).toString());
                }
                if("378".equals(key)){
                    storeAfSalesException.setRecipientPhone(dataElement.get(key).toString());
                }
                if("379".equals(key)){
                    storeAfSalesException.setShippingAddress(dataElement.get(key).toString());
                }
                if("1953".equals(key)){
                    storeAfSalesException.setComplaintProductBatchNumber(dataElement.get(key).toString());
                }
                if("1946".equals(key)){
                    storeAfSalesException.setOrderNote(dataElement.get(key).toString());
                }
                if("590".equals(key)){
                    storeAfSalesException.setProductInfoTable(dataElement.get(key).toString());
                }
                if("483".equals(key)){
                    if("3365648318390452102".equals(dataElement.get(key).toString())){
                        System.out.println(storeAfSalesException);
                    }
                    storeAfSalesException.setSubOrder(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    storeAfSalesException.setId(dataElement.get(key).toString());
                }
            }

            QueryWrapper<StoreAfSalesException> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",storeAfSalesException.getId()).eq("is_delete","0");;
            StoreAfSalesException existStoreAfSalesException =  storeAfSalesExceptionService.getOne(queryWrapper);
            if(existStoreAfSalesException != null){
                if(!existStoreAfSalesException.getModifyTime().equals(storeAfSalesException.getModifyTime())) {
                    updateStoreAfSalesExceptionList.add(storeAfSalesException);
                }
                //updateStoreAfSalesExceptionList.add(storeAfSalesException);
            }else{
                storeAfSalesException.setIsDelete("0");
                insertStoreAfSalesExceptionList.add(storeAfSalesException);
            }
        }



        //插入数据
        storeAfSalesExceptionService.saveBatch(insertStoreAfSalesExceptionList);
       /* System.out.println("insertStoreAfSalesExceptionList="+insertStoreAfSalesExceptionList.toString());
        System.out.println("updateStoreAfSalesExceptionList="+updateStoreAfSalesExceptionList.toString());*/
        //System.out.println("ProductInfoList="+ProductInfoList.toString());

        //遍历仓库售后异常list 组装商品信息
        for(StoreAfSalesException storeAfSalesException :insertStoreAfSalesExceptionList){
            String productInfoTable = storeAfSalesException.getProductInfoTable();
            if(StrUtil.isNotEmpty(productInfoTable)){
                JSONArray jsonArray = JSON.parseArray(productInfoTable);
                for(int j=0;j<jsonArray.size();j++){
                    StoreAfSalesExceptionProduct storeAfSalesExceptionProduct = new StoreAfSalesExceptionProduct();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("606".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setProductName(value1);
                        }
                        if("607".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setBrand(value1);
                        }
                        if("605".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setMerchantCode(value1);
                        }
                        if("1913".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setQuantity(value1);
                        }
                    }
                    storeAfSalesExceptionProduct.setId(storeAfSalesException.getId());
                    storeAfSalesExceptionProductInfoList.add(storeAfSalesExceptionProduct);
                }
            }


            String subOrder = storeAfSalesException.getSubOrder();
            if(StrUtil.isNotEmpty(subOrder)){
                JSONArray jsonArray = JSON.parseArray(subOrder);
                for(int j=0;j<jsonArray.size();j++){
                    StoreAfSalesExceptionSubOrder storeAfSalesExceptionSubOrderObj = new StoreAfSalesExceptionSubOrder();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("464".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setProductTitle(value1);
                        }
                        if("466".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setPurchaseQuantity(value1);
                        }
                        if("475".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setActualPayment(value1);
                        }
                        if("470".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setMerchantCode(value1);
                        }
                    }
                    storeAfSalesExceptionSubOrderObj.setId(storeAfSalesException.getId());
//                    System.out.println("subOrderObj="+subOrderObj);
                    storeAfSalesExceptionSubOrderList.add(storeAfSalesExceptionSubOrderObj);
                }
            }
        }

        if(storeAfSalesExceptionSubOrderList.size()>0){
            storeAfSalesExceptionSubOrderService.saveBatch(storeAfSalesExceptionSubOrderList);
        }
        if(storeAfSalesExceptionProductInfoList.size()>0){
            storeAfSalesExceptionProductService.saveBatch(storeAfSalesExceptionProductInfoList);
        }

        //更新数据
        for(StoreAfSalesException storeAfSalesException : updateStoreAfSalesExceptionList){
            UpdateWrapper<StoreAfSalesException> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",storeAfSalesException.getId()).eq("is_delete","0")
                    .set("task_status",storeAfSalesException.getTaskStatus())
                    .set("processing_result",storeAfSalesException.getProcessingResult())
                    .set("modify_time",storeAfSalesException.getModifyTime())
                    .set("task_finisher",storeAfSalesException.getTaskFinisher())
                    .set("task_finish_time",storeAfSalesException.getTaskFinishTime())
                    .set("issue_type",storeAfSalesException.getIssueType())
                    .set("warehouse",storeAfSalesException.getWarehouse());




            storeAfSalesExceptionService.update(updateWrapper);
        }

        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }


    //循环查询
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transStoreAfSalesException(String pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("columnResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<StoreAfSalesExceptionSubOrder> storeAfSalesExceptionSubOrderList = new ArrayList<>();
        List<StoreAfSalesException> insertStoreAfSalesExceptionList = new ArrayList<>();
        List<StoreAfSalesException> updateStoreAfSalesExceptionList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            StoreAfSalesException storeAfSalesException = new StoreAfSalesException();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        storeAfSalesException.setCreator(byteNewUser.getNick());
                    }else {
                        storeAfSalesException.setCreator(dataElement.get(key).toString());
                    }
                }
                if("3".equals(key)){
                    storeAfSalesException.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    storeAfSalesException.setModifyTime(dataElement.get(key).toString());
                }
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        storeAfSalesException.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        storeAfSalesException.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        storeAfSalesException.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        storeAfSalesException.setTaskStatus("暂停中");
                    }
                }
                //仓库 数组 需要db数据库初始化
                if("456".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<WareHouse> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(WareHouse::getId,value);
                    List<WareHouse> wareHouseList = wareHouseService.list(queryWrapper);
                    if(wareHouseList.size()>0){
                        String wareHouseName = wareHouseList.get(0).getWareHouseName();
                        if(StrUtil.isNotEmpty(wareHouseName)){
                            storeAfSalesException.setWarehouse(wareHouseName);
                        }else {
                            storeAfSalesException.setWarehouse("未匹配到数据");
                        }
                    }else {
                        storeAfSalesException.setWarehouse("未匹配到数据");
                    }
                }
                if("8".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        storeAfSalesException.setTaskFinisher(byteNewUser.getNick());
                    }else {
                        storeAfSalesException.setTaskFinisher(dataElement.get(key).toString());
                    }
                }
                if("457".equals(key)){
                    storeAfSalesException.setWaybillNumber(dataElement.get(key).toString());
                }
                if("1737".equals(key)){
                    storeAfSalesException.setCompensationAmount(new BigDecimal(dataElement.get(key).toString()));
                }
                if("9".equals(key)){
                    storeAfSalesException.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("458".equals(key)){
                    storeAfSalesException.setActualPaymentAmount(new BigDecimal(dataElement.get(key).toString()));
                }
                if("459".equals(key)){
                    String value = dataElement.get(key).toString();
                    List<String> ids = Arrays.asList(value.split(","));
                    LambdaQueryWrapper<IssueType> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.in(IssueType::getId,ids);
                    List<IssueType> issueTypeList = issueTypeService.list(queryWrapper);
                    String issueTypeStr = "";
                    for(int i1 =0;i1<issueTypeList.size();i1++){
                        issueTypeStr = issueTypeStr + issueTypeList.get(i1).getDescription() + "-";
                        if(i1 == issueTypeList.size()-1){
                            issueTypeStr = issueTypeStr.substring(0, issueTypeStr.length() - 1);
                        }
                    }
                    storeAfSalesException.setIssueType(issueTypeStr);
                }
                //处理结果 数组 需要数据初始化
                if("460".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<ProcessingResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(ProcessingResult::getId,dataElement.get(key).toString());
                    List<ProcessingResult> processingResults = processingResultService.list(queryWrapper);
                    if(processingResults.size()>0){
                        for(ProcessingResult processingResult : processingResults){
                            if(processingResult.getId().equals(dataElement.get(key).toString())){
                                storeAfSalesException.setProcessingResult(processingResult.getDescription());
                            }else{
                                storeAfSalesException.setProcessingResult("未匹配到数据");
                            }
                        }
                    }else {
                        storeAfSalesException.setProcessingResult("未匹配到数据");
                    }

                }
                if("461".equals(key)){
                    storeAfSalesException.setDescription(dataElement.get(key).toString());
                }
                if("9".equals(key)){
                    storeAfSalesException.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("462".equals(key)){
                    storeAfSalesException.setImage(dataElement.get(key).toString());
                }
                //店铺 需要配置到数据库
                if("373".equals(key)){
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                storeAfSalesException.setShop(shop.getShopName());
                            }else{
                                storeAfSalesException.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        storeAfSalesException.setShop("未匹配到数据");
                    }
                }
                if("374".equals(key)){
                    storeAfSalesException.setOrderNumber(dataElement.get(key).toString());
                }
                if("375".equals(key)){
                    storeAfSalesException.setBuyerNickname(dataElement.get(key).toString());
                }
                if("377".equals(key)){
                    storeAfSalesException.setRecipientName(dataElement.get(key).toString());
                }
                if("378".equals(key)){
                    storeAfSalesException.setRecipientPhone(dataElement.get(key).toString());
                }
                if("379".equals(key)){
                    storeAfSalesException.setShippingAddress(dataElement.get(key).toString());
                }
                if("1953".equals(key)){
                    storeAfSalesException.setComplaintProductBatchNumber(dataElement.get(key).toString());
                }
                if("1946".equals(key)){
                    storeAfSalesException.setOrderNote(dataElement.get(key).toString());
                }
                if("590".equals(key)){
                    storeAfSalesException.setProductInfoTable(dataElement.get(key).toString());
                }
                if("483".equals(key)){
                    if("3365648318390452102".equals(dataElement.get(key).toString())){
                        System.out.println(storeAfSalesException);
                    }
                    storeAfSalesException.setSubOrder(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    storeAfSalesException.setId(dataElement.get(key).toString());
                }
            }

            QueryWrapper<StoreAfSalesException> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",storeAfSalesException.getId()).eq("is_delete","0");
            StoreAfSalesException existStoreAfSalesException =  storeAfSalesExceptionService.getOne(queryWrapper);
            if(existStoreAfSalesException != null){
                if(!existStoreAfSalesException.getModifyTime().equals(storeAfSalesException.getModifyTime())) {
                    updateStoreAfSalesExceptionList.add(storeAfSalesException);
                }
                //updateStoreAfSalesExceptionList.add(storeAfSalesException);
            }else{
                storeAfSalesException.setIsDelete("0");
                insertStoreAfSalesExceptionList.add(storeAfSalesException);
            }
        }



        //插入数据
        storeAfSalesExceptionService.saveBatch(insertStoreAfSalesExceptionList);
        //System.out.println("insertStoreAfSalesExceptionList="+insertStoreAfSalesExceptionList.toString());
        //System.out.println("updateStoreAfSalesExceptionList="+updateStoreAfSalesExceptionList.toString());
        //System.out.println("ProductInfoList="+ProductInfoList.toString());

        //遍历仓库售后异常list 组装商品信息
        for(StoreAfSalesException storeAfSalesException :insertStoreAfSalesExceptionList){
            String productInfoTable = storeAfSalesException.getProductInfoTable();
            if(StrUtil.isNotEmpty(productInfoTable)){
                JSONArray jsonArray = JSON.parseArray(productInfoTable);
                for(int j=0;j<jsonArray.size();j++){
                    StoreAfSalesExceptionProduct storeAfSalesExceptionProduct = new StoreAfSalesExceptionProduct();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("606".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setProductName(value1);
                        }
                        if("607".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setBrand(value1);
                        }
                        if("605".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setMerchantCode(value1);
                        }
                        if("1913".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionProduct.setQuantity(value1);
                        }
                    }
                    storeAfSalesExceptionProduct.setId(storeAfSalesException.getId());
                    //System.out.println("product="+product);
                    storeAfSalesExceptionProductInfoList.add(storeAfSalesExceptionProduct);
                }
            }


            String subOrder = storeAfSalesException.getSubOrder();
            if(StrUtil.isNotEmpty(subOrder)){
                JSONArray jsonArray = JSON.parseArray(subOrder);
                for(int j=0;j<jsonArray.size();j++){
                    StoreAfSalesExceptionSubOrder storeAfSalesExceptionSubOrderObj = new StoreAfSalesExceptionSubOrder();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("464".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setProductTitle(value1);
                        }
                        if("466".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setPurchaseQuantity(value1);
                        }
                        if("475".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setActualPayment(value1);
                        }
                        if("470".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            storeAfSalesExceptionSubOrderObj.setMerchantCode(value1);
                        }
                    }
                    storeAfSalesExceptionSubOrderObj.setId(storeAfSalesException.getId());
//                    System.out.println("subOrderObj="+subOrderObj);
                    storeAfSalesExceptionSubOrderList.add(storeAfSalesExceptionSubOrderObj);
                }
            }
        }

        if(storeAfSalesExceptionSubOrderList.size()>0){
            storeAfSalesExceptionSubOrderService.saveBatch(storeAfSalesExceptionSubOrderList);
        }
        if(storeAfSalesExceptionProductInfoList.size()>0){
            storeAfSalesExceptionProductService.saveBatch(storeAfSalesExceptionProductInfoList);
        }

        //更新数据
        for(StoreAfSalesException storeAfSalesException : updateStoreAfSalesExceptionList){
            UpdateWrapper<StoreAfSalesException> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",storeAfSalesException.getId()).eq("is_delete","0")
                    .set("task_status",storeAfSalesException.getTaskStatus())
                    .set("processing_result",storeAfSalesException.getProcessingResult())
                    .set("modify_time",storeAfSalesException.getModifyTime())
                    .set("task_finisher",storeAfSalesException.getTaskFinisher())
                    .set("task_finish_time",storeAfSalesException.getTaskFinishTime())
                    .set("issue_type",storeAfSalesException.getIssueType())
                    .set("warehouse",storeAfSalesException.getWarehouse());

            storeAfSalesExceptionService.update(updateWrapper);
        }
        /*JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Integer> transAllergyReactionOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", "558");
        //params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("columnResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        Map<String,Integer> map = new HashMap<>();

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");


        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<AllergyReactionOrder> insertAllergyReactionOrderList = new ArrayList<>();
        List<AllergyReactionOrder> updateAllergyReactionOrderList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            AllergyReactionOrder allergyReactionOrder = new AllergyReactionOrder();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        allergyReactionOrder.setCreator(byteNewUser.getNick());
                    }else {
                        allergyReactionOrder.setCreator(dataElement.get(key).toString());
                    }
                }
                if("3".equals(key)){
                    allergyReactionOrder.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    allergyReactionOrder.setModifyTime(dataElement.get(key).toString());
                }
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        allergyReactionOrder.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        allergyReactionOrder.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        allergyReactionOrder.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        allergyReactionOrder.setTaskStatus("暂停中");
                    }
                }

                //处理结果 数组 需要数据初始化 需要检查 跟仓库异常等级表的处理结果数据是否一样
                if("656".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<ProcessingResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(ProcessingResult::getId,dataElement.get(key).toString());
                    List<ProcessingResult> processingResults = processingResultService.list(queryWrapper);
                    if(processingResults.size()>0){
                        for(ProcessingResult processingResult : processingResults){
                            if(processingResult.getId().equals(dataElement.get(key).toString())){
                                allergyReactionOrder.setProcessingResult(processingResult.getDescription());
                            }else{
                                allergyReactionOrder.setProcessingResult("未匹配到数据");
                            }
                        }
                    }else {
                        allergyReactionOrder.setProcessingResult("未匹配到数据");
                    }

                }

                //店铺 需要配置到数据库
                if("567".equals(key)){
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                allergyReactionOrder.setShop(shop.getShopName());
                            }else{
                                allergyReactionOrder.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        allergyReactionOrder.setShop("未匹配到数据");
                    }
                }
                if("568".equals(key)){
                    allergyReactionOrder.setOrderNumber(dataElement.get(key).toString());
                }
                if("569".equals(key)){
                    allergyReactionOrder.setBuyerNickname(dataElement.get(key).toString());
                }
                if("1915".equals(key)){
                    allergyReactionOrder.setCompensationAmount(dataElement.get(key).toString());
                }
                if("571".equals(key)){
                    allergyReactionOrder.setPaymentTime(dataElement.get(key).toString());
                }
                if("655".equals(key)){
                    String value = dataElement.get(key).toString();
                    if(value != null){
                        String allergicSymptoms = "";
                        //判断 value是否有逗号，如果有逗号证明过敏症状有多个需要转换成数组
                        if(value.contains(",")){
                            List<String> valueList = Arrays.asList(value.split(","));
                            for(String element : valueList){
                                QueryWrapper<AllergicSymptoms> queryWrapper = new QueryWrapper();
                                queryWrapper.eq("id",element);
                                AllergicSymptoms obj = allergicSymptomsService.getOne(queryWrapper);
                                allergicSymptoms = allergicSymptoms + obj.getAllergicSymptoms()+",";
                            }
                            int lastIndex = allergicSymptoms.lastIndexOf(",");
                            if (lastIndex != -1) {
                                allergicSymptoms = allergicSymptoms.substring(0, lastIndex) + allergicSymptoms.substring(lastIndex + 1);
                            }
                            allergyReactionOrder.setAllergicSymptoms(allergicSymptoms);
                        }else {
                            QueryWrapper<AllergicSymptoms> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("id",dataElement.get(key).toString());
                            AllergicSymptoms obj = allergicSymptomsService.getOne(queryWrapper);
                            if(obj!=null){
                                allergyReactionOrder.setAllergicSymptoms(obj.getAllergicSymptoms());
                            }else {
                                allergyReactionOrder.setAllergicSymptoms("匹配数据失败");
                            }
                        }
                    }
                }
                if("657".equals(key)){
                    allergyReactionOrder.setAbnormalRemarks(dataElement.get(key).toString());
                }
                if("658".equals(key)){
                    allergyReactionOrder.setAllergicImages(dataElement.get(key).toString());
                }
                if("640".equals(key)){
                    allergyReactionOrder.setProductInfoTable(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    allergyReactionOrder.setId(dataElement.get(key).toString());
                }
            }

            QueryWrapper<AllergyReactionOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",allergyReactionOrder.getId());
            AllergyReactionOrder existAllergyReactionOrder =  allergyReactionOrderService.getOne(queryWrapper);
            if(existAllergyReactionOrder != null){
                if(!existAllergyReactionOrder.getModifyTime().equals(allergyReactionOrder.getModifyTime())){
                    updateAllergyReactionOrderList.add(allergyReactionOrder);
                }
                //updateAllergyReactionOrderList.add(allergyReactionOrder);
            }else{
                allergyReactionOrder.setIsDelete("0");
                insertAllergyReactionOrderList.add(allergyReactionOrder);
            }
        }

        //插入数据
        allergyReactionOrderService.saveBatch(insertAllergyReactionOrderList);
        /*System.out.println("size1="+insertAllergyReactionOrderList.size());
        System.out.println("size2="+dataResultArray.size());*/
        //System.out.println("updateStoreAfSalesExceptionList="+updateAllergyReactionOrderList.toString());
        //System.out.println("ProductInfoList="+ProductInfoList.toString());

        //遍历仓库售后异常list 组装商品信息
        List<AllergyReactionOrderProduct> allergyReactionOrderProductList = new ArrayList<>();
        for(AllergyReactionOrder allergyReactionOrder :insertAllergyReactionOrderList){
            String productInfoTable = allergyReactionOrder.getProductInfoTable();
            if(StrUtil.isNotEmpty(productInfoTable)){
                JSONArray jsonArray = JSON.parseArray(productInfoTable);
                for(int j=0;j<jsonArray.size();j++){
                    AllergyReactionOrderProduct allergyReactionOrderProduct = new AllergyReactionOrderProduct();
                    allergyReactionOrderProduct.setId(allergyReactionOrder.getId());
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("627".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setProductName(value1);
                        }
                        if("628".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setBrand(value1);
                        }
                        if("626".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setMerchantCode(value1);
                        }
                    }
//                    if("977".equals(allergyReactionOrder.getId())){
//                        System.out.println(allergyReactionOrder.getId());
//                    }
                    allergyReactionOrderProductList.add(allergyReactionOrderProduct);
                }
            }

        }

        if(allergyReactionOrderProductList.size()>0){
            allergyReactionOrderProductService.saveBatch(allergyReactionOrderProductList);
        }


        //更新数据
        for(AllergyReactionOrder allergyReactionOrder : updateAllergyReactionOrderList){
            UpdateWrapper<AllergyReactionOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",allergyReactionOrder.getId())
                    .set("task_status",allergyReactionOrder.getTaskStatus())
                    .set("processing_result",allergyReactionOrder.getProcessingResult())
                    .set("modify_time",allergyReactionOrder.getModifyTime())
                    .set("allergic_symptoms",allergyReactionOrder.getAllergicSymptoms())
                    .set("shop",allergyReactionOrder.getShop());

            allergyReactionOrderService.update(updateWrapper);
        }

        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }

    @Override
    public void transAllergyReactionOrder(String pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", "558");
        params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        //System.out.println("dataResponse="+dataResponse);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("columnResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        Map<String,Integer> map = new HashMap<>();

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");


        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<AllergyReactionOrder> insertAllergyReactionOrderList = new ArrayList<>();
        List<AllergyReactionOrder> updateAllergyReactionOrderList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            AllergyReactionOrder allergyReactionOrder = new AllergyReactionOrder();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        allergyReactionOrder.setCreator(byteNewUser.getNick());
                    }else {
                        allergyReactionOrder.setCreator(dataElement.get(key).toString());
                    }
                }
                if("3".equals(key)){
                    allergyReactionOrder.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    allergyReactionOrder.setModifyTime(dataElement.get(key).toString());
                }
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        allergyReactionOrder.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        allergyReactionOrder.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        allergyReactionOrder.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        allergyReactionOrder.setTaskStatus("暂停中");
                    }
                }

                //处理结果 数组 需要数据初始化 需要检查 跟仓库异常等级表的处理结果数据是否一样
                if("656".equals(key)){
                    String value = dataElement.get(key).toString();
                    LambdaQueryWrapper<ProcessingResult> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(ProcessingResult::getId,dataElement.get(key).toString());
                    List<ProcessingResult> processingResults = processingResultService.list(queryWrapper);
                    if(processingResults.size()>0){
                        for(ProcessingResult processingResult : processingResults){
                            if(processingResult.getId().equals(dataElement.get(key).toString())){
                                allergyReactionOrder.setProcessingResult(processingResult.getDescription());
                            }else{
                                allergyReactionOrder.setProcessingResult("未匹配到数据");
                            }
                        }
                    }else {
                        allergyReactionOrder.setProcessingResult("未匹配到数据");
                    }

                }

                //店铺 需要配置到数据库
                if("567".equals(key)){
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                allergyReactionOrder.setShop(shop.getShopName());
                            }else{
                                allergyReactionOrder.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        allergyReactionOrder.setShop("未匹配到数据");
                    }
                }
                if("568".equals(key)){
                    allergyReactionOrder.setOrderNumber(dataElement.get(key).toString());
                }
                if("569".equals(key)){
                    allergyReactionOrder.setBuyerNickname(dataElement.get(key).toString());
                }
                if("1915".equals(key)){
                    allergyReactionOrder.setCompensationAmount(dataElement.get(key).toString());
                }
                if("571".equals(key)){
                    allergyReactionOrder.setPaymentTime(dataElement.get(key).toString());
                }
                if("655".equals(key)){
                    String value = dataElement.get(key).toString();
                    if(value != null){
                        String allergicSymptoms = "";
                        //判断 value是否有逗号，如果有逗号证明过敏症状有多个需要转换成数组
                        if(value.contains(",")){
                            List<String> valueList = Arrays.asList(value.split(","));
                            for(String element : valueList){
                                QueryWrapper<AllergicSymptoms> queryWrapper = new QueryWrapper();
                                queryWrapper.eq("id",element);
                                AllergicSymptoms obj = allergicSymptomsService.getOne(queryWrapper);
                                allergicSymptoms = allergicSymptoms + obj.getAllergicSymptoms()+",";
                            }
                            int lastIndex = allergicSymptoms.lastIndexOf(",");
                            if (lastIndex != -1) {
                                allergicSymptoms = allergicSymptoms.substring(0, lastIndex) + allergicSymptoms.substring(lastIndex + 1);
                            }
                            allergyReactionOrder.setAllergicSymptoms(allergicSymptoms);
                        }else {
                            QueryWrapper<AllergicSymptoms> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("id",dataElement.get(key).toString());
                            AllergicSymptoms obj = allergicSymptomsService.getOne(queryWrapper);
                            if(obj!=null){
                                allergyReactionOrder.setAllergicSymptoms(obj.getAllergicSymptoms());
                            }else {
                                allergyReactionOrder.setAllergicSymptoms("匹配数据失败");
                            }
                        }
                    }
                }
                if("657".equals(key)){
                    allergyReactionOrder.setAbnormalRemarks(dataElement.get(key).toString());
                }
                if("658".equals(key)){
                    allergyReactionOrder.setAllergicImages(dataElement.get(key).toString());
                }
                if("640".equals(key)){
                    allergyReactionOrder.setProductInfoTable(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    allergyReactionOrder.setId(dataElement.get(key).toString());
                }
            }

            QueryWrapper<AllergyReactionOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",allergyReactionOrder.getId());
            AllergyReactionOrder existAllergyReactionOrder =  allergyReactionOrderService.getOne(queryWrapper);
            if(existAllergyReactionOrder != null){
                if(!existAllergyReactionOrder.getModifyTime().equals(allergyReactionOrder.getModifyTime())){
                    updateAllergyReactionOrderList.add(allergyReactionOrder);
                }
                //updateAllergyReactionOrderList.add(allergyReactionOrder);
            }else{
                insertAllergyReactionOrderList.add(allergyReactionOrder);
            }
        }

        //插入数据
        allergyReactionOrderService.saveBatch(insertAllergyReactionOrderList);
        /*System.out.println("size1="+insertAllergyReactionOrderList.size());
        System.out.println("size2="+dataResultArray.size());*/
        //System.out.println("updateStoreAfSalesExceptionList="+updateAllergyReactionOrderList.toString());
        //System.out.println("ProductInfoList="+ProductInfoList.toString());

        //遍历仓库售后异常list 组装商品信息
        List<AllergyReactionOrderProduct> allergyReactionOrderProductList = new ArrayList<>();
        for(AllergyReactionOrder allergyReactionOrder :insertAllergyReactionOrderList){
            String productInfoTable = allergyReactionOrder.getProductInfoTable();
            if(StrUtil.isNotEmpty(productInfoTable)){
                JSONArray jsonArray = JSON.parseArray(productInfoTable);
                for(int j=0;j<jsonArray.size();j++){
                    AllergyReactionOrderProduct allergyReactionOrderProduct = new AllergyReactionOrderProduct();
                    allergyReactionOrderProduct.setId(allergyReactionOrder.getId());
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key1 : jsonObject.keySet()){
                        if("627".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setProductName(value1);
                        }
                        if("628".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setBrand(value1);
                        }
                        if("626".equals(key1)){
                            String value1 = jsonObject.get(key1).toString();
                            allergyReactionOrderProduct.setMerchantCode(value1);
                        }
                    }
//                    if("977".equals(allergyReactionOrder.getId())){
//                        System.out.println(allergyReactionOrder.getId());
//                    }
                    allergyReactionOrderProductList.add(allergyReactionOrderProduct);
                }
            }

        }

        if(allergyReactionOrderProductList.size()>0){
            allergyReactionOrderProductService.saveBatch(allergyReactionOrderProductList);
        }


        //更新数据
        for(AllergyReactionOrder allergyReactionOrder : updateAllergyReactionOrderList){
            UpdateWrapper<AllergyReactionOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",allergyReactionOrder.getId())
                    .set("task_status",allergyReactionOrder.getTaskStatus())
                    .set("processing_result",allergyReactionOrder.getProcessingResult())
                    .set("modify_time",allergyReactionOrder.getModifyTime())
                    .set("allergic_symptoms",allergyReactionOrder.getAllergicSymptoms())
                    .set("shop",allergyReactionOrder.getShop());

            allergyReactionOrderService.update(updateWrapper);
        }
    }

    @Override
    public Map<String, Integer> transRecycleStoreAfSalesException() {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        //System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "100");
        body.put("pageNum", "1");
        body.put("appId", "30777");
        body.put("projectId", project_id);

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        if(StrUtil.isEmpty(responseBody)){
            log.info("columnResponse="+responseBody);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesException> recycleList = new ArrayList<>();

        for (int i = 0; i < dataResultArray.size(); i++) {
            StoreAfSalesException storeAfSalesException = new StoreAfSalesException();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<StoreAfSalesException> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<StoreAfSalesException> resultList = storeAfSalesExceptionService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<StoreAfSalesException> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        storeAfSalesExceptionService.update(updateWrapper);
                    }
                }

            }
        }

        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }

    @Override
    public void transRecycleStoreAfSalesException(String pageNum) {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "100");
        body.put("pageNum", pageNum);
        body.put("appId", "30777");
        body.put("projectId", project_id);

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        if(StrUtil.isEmpty(responseBody)){
            log.info("columnResponse="+responseBody);
            throw new BusinessException("返回报文为空");
        }


        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        //定义
        // 仓库售后异常数组大小
        List<StoreAfSalesException> recycleList = new ArrayList<>();

        for (int i = 0; i < dataResultArray.size(); i++) {
            StoreAfSalesException storeAfSalesException = new StoreAfSalesException();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<StoreAfSalesException> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<StoreAfSalesException> resultList = storeAfSalesExceptionService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<StoreAfSalesException> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        storeAfSalesExceptionService.update(updateWrapper);
                    }
                }

            }
        }
    }

    @Override
    public Map<String, Integer> transRecycleAllergyReactionOrder() {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "100");
        body.put("pageNum", "1");
        body.put("appId", "30777");
        body.put("projectId", "558");

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        //System.out.println(responseBody);
        if(StrUtil.isEmpty(responseBody)){
            log.info("columnResponse="+responseBody);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");



        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<AllergyReactionOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<AllergyReactionOrder> resultList = allergyReactionOrderService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<AllergyReactionOrder> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        allergyReactionOrderService.update(updateWrapper);
                    }
                    //System.out.println(dataElement.get(key).toString());
                }
            }
        }

        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }

    @Override
    public void transRecycleAllergyReactionOrder(String pageNum) {
// 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "100");
        body.put("pageNum", pageNum);
        body.put("appId", "30777");
        body.put("projectId", "558");

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        //System.out.println(responseBody);
        if(StrUtil.isEmpty(responseBody)){
            log.info("columnResponse="+responseBody);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<AllergyReactionOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<AllergyReactionOrder> resultList = allergyReactionOrderService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<AllergyReactionOrder> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        allergyReactionOrderService.update(updateWrapper);
                    }
                }
            }
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Integer> transReissueOrder() {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", "1349");
        //params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        //System.out.println(dataResponse);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("dataResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        Map<String,Integer> map = new HashMap<>();

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        // 仓库售后异常数组大小
        //List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<ReissueOrder> insertReissueOrderList = new ArrayList<>();
        List<ReissueOrder> updateReissueOrderList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            ReissueOrder reissueOrder = new ReissueOrder();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        reissueOrder.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        reissueOrder.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        reissueOrder.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        reissueOrder.setTaskStatus("暂停中");
                    }
                }
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        reissueOrder.setCreator(byteNewUser.getNick());
                    }else {
                        reissueOrder.setCreator(dataElement.get(key).toString());
                    }
                }

                //店铺【旺店通】 需要配置到数据库
                if("1456".equals(key)){
                    //reissueOrder.setShop(dataElement.get(key).toString());
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                reissueOrder.setShop(shop.getShopName());
                            }else{
                                reissueOrder.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        reissueOrder.setShop("未匹配到数据");
                    }
                }

                if("1356".equals(key)){
                    reissueOrder.setOriginalOrderNumber(dataElement.get(key).toString());
                }
                if("1357".equals(key)){
                    reissueOrder.setReceivingInformation(dataElement.get(key).toString());
                }
                if("1608".equals(key)){
                    String value = dataElement.get(key).toString();
                    if(value != null){
                        String reissueReason = "";
                        //判断 value是否有逗号，如果有逗号证明过敏症状有多个需要转换成数组
                        if(value.contains(",")){
                            List<String> ids = Arrays.asList(value.split(","));
                            LambdaQueryWrapper<ReissueReason> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.in(ReissueReason::getId,ids);
                            List<ReissueReason> reissueReasonList = reissueReasonService.list(queryWrapper);
                            String reasonStr = "";
                            for(int i1 =0;i1<reissueReasonList.size();i1++){
                                reasonStr = reasonStr + reissueReasonList.get(i1).getReason() + "-";
                                if(i1 == reissueReasonList.size()-1){
                                    reasonStr = reasonStr.substring(0, reasonStr.length() - 1);
                                }
                            }
                            reissueOrder.setReissueReason(reasonStr);
                        }else {
                            QueryWrapper<ReissueReason> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("id",dataElement.get(key).toString());
                            ReissueReason obj = reissueReasonService.getOne(queryWrapper);
                            if(obj!=null){
                                reissueOrder.setReissueReason(obj.getReason());
                            }else {
                                reissueOrder.setReissueReason(dataElement.get(key).toString());
                            }
                        }
                    }
                }
                if("1929".equals(key)){
                    reissueOrder.setOrderNote(dataElement.get(key).toString());
                }
                if("1379".equals(key)){
                    reissueOrder.setCustomerServiceNote(dataElement.get(key).toString());
                }
                if("1355".equals(key)){
                    reissueOrder.setNetName(dataElement.get(key).toString());
                }
                if("3".equals(key)){
                    reissueOrder.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    reissueOrder.setModifyTime(dataElement.get(key).toString());
                }
                if("9".equals(key)){
                    reissueOrder.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("8".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        reissueOrder.setTaskFinisher(byteNewUser.getNick());
                    }else {
                        reissueOrder.setTaskFinisher(dataElement.get(key).toString());
                    }
                }
                if("1392".equals(key)){
                    reissueOrder.setProductInfo(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    reissueOrder.setId(dataElement.get(key).toString());
                }
            }

            //System.out.println(reissueOrder);
            /*if("285434630404".equals(reissueOrder.getOriginalOrderNumber())){
                System.out.println("dataResponse="+dataResponse);
            }*/
            QueryWrapper<ReissueOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",reissueOrder.getId());
            ReissueOrder existReissueOrder =  reissueOrderService.getOne(queryWrapper);
            if(existReissueOrder != null){
                if(!existReissueOrder.getModifyTime().equals(reissueOrder.getModifyTime())){
                    updateReissueOrderList.add(reissueOrder);
                }
            }else{
                reissueOrder.setIsDelete("0");
                insertReissueOrderList.add(reissueOrder);
            }
        }
        //插入数据
        reissueOrderService.saveBatch(insertReissueOrderList);

        List<ReissueOrderProduct> reissueOrderProductList = new ArrayList<>();

        //封装商品信息
        for(ReissueOrder reissueOrder1 :insertReissueOrderList){
            String productInfo = reissueOrder1.getProductInfo();
            if(StrUtil.isNotEmpty(productInfo)){
                JSONArray jsonArray = JSON.parseArray(productInfo);
                for(int j=0;j<jsonArray.size();j++){
                    ReissueOrderProduct reissueOrderProduct = new ReissueOrderProduct();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key : jsonObject.keySet()){
                        //货品名称
                        if("1387".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setProductName(value);
                        }
                        //商家编码
                        if("1393".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setMerchantCode(value);
                        }
                        //补发数量
                        if("1449".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setReissueNumber(value);
                        }
                        //品牌
                        if("1388".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setBrand(value);
                        }


                    }
                    reissueOrderProduct.setId(reissueOrder1.getId());
                    reissueOrderProductList.add(reissueOrderProduct);
                }
            }
        }
        //新增
        reissueOrderProductService.saveBatch(reissueOrderProductList);

        //更新数据
        for(ReissueOrder reissueOrder : updateReissueOrderList){
            UpdateWrapper<ReissueOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",reissueOrder.getId()).eq("is_delete","0")
                    .set("task_status",reissueOrder.getTaskStatus())
                    .set("modify_time",reissueOrder.getModifyTime())
                    .set("task_finisher",reissueOrder.getTaskFinisher())
                    .set("task_finish_time",reissueOrder.getTaskFinishTime());

            reissueOrderService.update(updateWrapper);
        }


        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }

    @Override
    public void transReissueOrder(String pageNum) {
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        params.put("project_id", "1349");
        params.put("page_num",pageNum);
        params.put("page_size","200");

        //获取工作表数据
        //task.list column.list
        params.put("method", "task.list.new");
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String dataResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(dataResponse)){
            log.info("dataResponse="+dataResponse);
            throw new BusinessException("返回报文为空");
        }

        Map<String,Integer> map = new HashMap<>();

        JSONObject dataJsonObject = JSON.parseObject(dataResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");

        // 仓库售后异常数组大小
        //List<StoreAfSalesExceptionProduct> storeAfSalesExceptionProductInfoList = new ArrayList<>();
        List<ReissueOrder> insertReissueOrderList = new ArrayList<>();
        List<ReissueOrder> updateReissueOrderList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            ReissueOrder reissueOrder = new ReissueOrder();
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for(String key :dataElement.keySet()){
                if("5".equals(key)){
                    String value = dataElement.get(key).toString();
                    if("0".equals(value)){
                        reissueOrder.setTaskStatus("待检查");
                    }
                    if("1".equals(value)){
                        reissueOrder.setTaskStatus("已检查");
                    }
                    if("2".equals(value)){
                        reissueOrder.setTaskStatus("待跟进");
                    }
                    if("3".equals(value)){
                        reissueOrder.setTaskStatus("暂停中");
                    }
                }
                if("1".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        reissueOrder.setCreator(byteNewUser.getNick());
                    }else {
                        reissueOrder.setCreator(dataElement.get(key).toString());
                    }
                }

                //店铺【旺店通】 需要配置到数据库
                if("1456".equals(key)){
                    //reissueOrder.setShop(dataElement.get(key).toString());
                    LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Shop::getId,dataElement.get(key).toString());
                    List<Shop> shopList = shopService.list(queryWrapper);
                    if(shopList.size()>0){
                        for(Shop shop : shopList){
                            if(shop.getId().equals(dataElement.get(key).toString())){
                                reissueOrder.setShop(shop.getShopName());
                            }else{
                                reissueOrder.setShop("未匹配到数据");
                            }
                        }
                    }else {
                        reissueOrder.setShop("未匹配到数据");
                    }
                }

                if("1356".equals(key)){
                    reissueOrder.setOriginalOrderNumber(dataElement.get(key).toString());
                }
                if("1357".equals(key)){
                    reissueOrder.setReceivingInformation(dataElement.get(key).toString());
                }
                if("1608".equals(key)){
                    String value = dataElement.get(key).toString();
                    if(value != null){
                        String reissueReason = "";
                        //判断 value是否有逗号，如果有逗号证明过敏症状有多个需要转换成数组
                        if(value.contains(",")){
                            List<String> ids = Arrays.asList(value.split(","));
                            LambdaQueryWrapper<ReissueReason> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.in(ReissueReason::getId,ids);
                            List<ReissueReason> reissueReasonList = reissueReasonService.list(queryWrapper);
                            String reasonStr = "";
                            for(int i1 =0;i1<reissueReasonList.size();i1++){
                                reasonStr = reasonStr + reissueReasonList.get(i1).getReason() + "-";
                                if(i1 == reissueReasonList.size()-1){
                                    reasonStr = reasonStr.substring(0, reasonStr.length() - 1);
                                }
                            }
                            reissueOrder.setReissueReason(reasonStr);
                        }else {
                            QueryWrapper<ReissueReason> queryWrapper = new QueryWrapper();
                            queryWrapper.eq("id",dataElement.get(key).toString());
                            ReissueReason obj = reissueReasonService.getOne(queryWrapper);
                            if(obj!=null){
                                reissueOrder.setReissueReason(obj.getReason());
                            }else {
                                reissueOrder.setReissueReason(dataElement.get(key).toString());
                            }
                        }
                    }
                }
                if("1929".equals(key)){
                    reissueOrder.setOrderNote(dataElement.get(key).toString());
                }
                if("1379".equals(key)){
                    reissueOrder.setCustomerServiceNote(dataElement.get(key).toString());
                }
                if("1355".equals(key)){
                    reissueOrder.setNetName(dataElement.get(key).toString());
                }
                if("3".equals(key)){
                    reissueOrder.setCreateTime(dataElement.get(key).toString());
                }
                if("4".equals(key)){
                    reissueOrder.setModifyTime(dataElement.get(key).toString());
                }
                if("9".equals(key)){
                    reissueOrder.setTaskFinishTime(dataElement.get(key).toString());
                }
                if("8".equals(key)){
                    QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    ByteNewUser byteNewUser = byteNewUserService.getOne(queryWrapper);
                    if(byteNewUser != null){
                        reissueOrder.setTaskFinisher(byteNewUser.getNick());
                    }else {
                        reissueOrder.setTaskFinisher(dataElement.get(key).toString());
                    }
                }
                if("1392".equals(key)){
                    reissueOrder.setProductInfo(dataElement.get(key).toString());
                }
                if("-1".equals(key)){
                    reissueOrder.setId(dataElement.get(key).toString());
                }
            }
            //System.out.println(reissueOrder);
            /*if("285434630404".equals(reissueOrder.getOriginalOrderNumber())){
                System.out.println("dataResponse="+dataResponse);
            }*/

            QueryWrapper<ReissueOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",reissueOrder.getId());
            ReissueOrder existReissueOrder =  reissueOrderService.getOne(queryWrapper);
            if(existReissueOrder != null){
                if(!existReissueOrder.getModifyTime().equals(reissueOrder.getModifyTime())){
                    updateReissueOrderList.add(reissueOrder);
                }
                //updateAllergyReactionOrderList.add(allergyReactionOrder);
            }else{
                reissueOrder.setIsDelete("0");
                insertReissueOrderList.add(reissueOrder);
            }
        }
        //插入数据
        reissueOrderService.saveBatch(insertReissueOrderList);

        List<ReissueOrderProduct> reissueOrderProductList = new ArrayList<>();

        //封装商品信息
        for(ReissueOrder reissueOrder1 :insertReissueOrderList){
            String productInfo = reissueOrder1.getProductInfo();
            if(StrUtil.isNotEmpty(productInfo)){
                JSONArray jsonArray = JSON.parseArray(productInfo);
                for(int j=0;j<jsonArray.size();j++){
                    ReissueOrderProduct reissueOrderProduct = new ReissueOrderProduct();
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    for(String key : jsonObject.keySet()){
                        //货品名称
                        if("1387".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setProductName(value);
                        }
                        //商家编码
                        if("1393".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setMerchantCode(value);
                        }
                        //补发数量
                        if("1449".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setReissueNumber(value);
                        }
                        //品牌
                        if("1388".equals(key)){
                            String value = jsonObject.get(key).toString();
                            reissueOrderProduct.setBrand(value);
                        }


                    }
                    reissueOrderProduct.setId(reissueOrder1.getId());
                    reissueOrderProductList.add(reissueOrderProduct);
                }
            }
        }
        reissueOrderProductService.saveBatch(reissueOrderProductList);

        //更新数据
        for(ReissueOrder reissueOrder : updateReissueOrderList){
            UpdateWrapper<ReissueOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",reissueOrder.getId()).eq("is_delete","0")
                    .set("task_status",reissueOrder.getTaskStatus())
                    .set("modify_time",reissueOrder.getModifyTime())
                    .set("task_finisher",reissueOrder.getTaskFinisher())
                    .set("task_finish_time",reissueOrder.getTaskFinishTime());

            reissueOrderService.update(updateWrapper);
        }
    }

    @Override
    public Map<String, Integer> transRecycleTransReissueOrder() {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "200");
        body.put("pageNum", "1");
        body.put("appId", "30777");
        body.put("projectId", "1349");

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        if(StrUtil.isEmpty(responseBody)){
            log.info("responseBody="+responseBody);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");


        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<ReissueOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<ReissueOrder> resultList = reissueOrderService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<ReissueOrder> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        reissueOrderService.update(updateWrapper);
                    }
                }
            }
        }

        JSONObject dataResultMap = dataJsonObject.getJSONObject("response").getJSONObject("map");
        map.put("total_page_num",dataResultMap.getIntValue("total_page_num"));
        map.put("page_num",dataResultMap.getIntValue("page_num"));
        return map;
    }

    @Override
    public void transRecycleTransReissueOrder(String pageNum) {
// 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "recycle.task.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        Map<String,Integer> map = new HashMap<>();

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ", "%20");
        System.out.println("full_url=" + full_url);

        // 设置请求体参数
        Map<String, String> body = new HashMap<>();
        body.put("pageSize", "200");
        body.put("pageNum", pageNum);
        body.put("appId", "30777");
        body.put("projectId", "1349");

        String responseBody = byteNewHttpService.send_post_request(full_url, body);
        if(StrUtil.isEmpty(responseBody)){
            log.info("responseBody="+responseBody);
            throw new BusinessException("返回报文为空");
        }

        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("total");


        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            for (String key : dataElement.keySet()) {
                if("-1".equals(key)){
                    QueryWrapper<ReissueOrder> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",dataElement.get(key).toString());
                    List<ReissueOrder> resultList = reissueOrderService.list(queryWrapper);
                    if(resultList.size()>0 && resultList.size()==1){
                        UpdateWrapper<ReissueOrder> updateWrapper = new UpdateWrapper<>();
                        updateWrapper.eq("id",resultList.get(0).getId())
                                .set("is_delete","1");
                        reissueOrderService.update(updateWrapper);
                    }
                }
            }
        }
    }

}
