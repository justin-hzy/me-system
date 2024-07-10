package com.me.modules.bi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.modules.bi.entity.*;
import com.me.modules.bi.service.*;
import com.me.modules.http.service.ByteNewHttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ByteNewEumServiceImpl implements ByteNewEumService {

    private ByteNewHttpService byteNewHttpService;

    private ReissueReasonService reissueReasonService;

    private ShopService shopService;

    private WareHouseService wareHouseService;

    private IssueTypeService issueTypeService;

    private ProcessingResultService processingResultService;

    private ByteNewUserService byteNewUserService;

    private AllergicSymptomsService allergicSymptomsService;

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @Override
    public void initReissueOrder() {
        //获取枚举
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        String project_id = "1349";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "column.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("sign", null);

        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");


        // 发送请求并获取响应
        // 使用你喜欢的 HTTP 请求库发送 GET 请求
        // 这里只是一个示例
        String columnResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(columnResponse)){
            log.info("columnResponse="+columnResponse);
            throw new BusinessException("返回报文为空");
        }else {
            log.info("columnResponse="+columnResponse);
        }

        JSONObject dataJsonObject = JSON.parseObject(columnResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            String columnId = dataElement.getString("column_id");

            //同步商铺
            if("1456".equals(columnId)){
                log.info("同步商铺开始...");
                JSONArray relationOptions = dataElement.getJSONArray("options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject shopJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",shopJsonObj.getString("id"));
                    Shop shop = shopService.getOne(queryWrapper);
                    if(shop == null){
                        Shop saveShop = new Shop();
                        saveShop.setId(shopJsonObj.getString("id"));
                        saveShop.setShopName(shopJsonObj.getString("title"));
                        shopService.save(saveShop);
                    }
                }
                log.info("同步商铺结束...");
            }

            //同步补发原因
            if("1608".equals(columnId)){
                log.info("同步补发原因开始...");
                JSONArray relationOptions = dataElement.getJSONArray("relation_options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject reasonJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<ReissueReason> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",reasonJsonObj.getString("id"));
                    ReissueReason reissueReason = reissueReasonService.getOne(queryWrapper);
                    if(reissueReason == null){
                        ReissueReason saveReason = new ReissueReason();
                        saveReason.setId(reasonJsonObj.getString("id"));
                        saveReason.setReason(reasonJsonObj.getString("title"));
                        reissueReasonService.save(saveReason);
                    }
                }
                log.info("同步补发原因结束...");
            }

        }
    }

    @Override
    public void initStoreAfSalesException() {
        //获取组件枚举
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        String project_id = "359";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "column.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("sign", null);

        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");
        log.info("full_url="+full_url);


        // 发送请求并获取响应
        // 使用你喜欢的 HTTP 请求库发送 GET 请求
        // 这里只是一个示例
        String columnResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(columnResponse)){
            log.info("columnResponse="+columnResponse);
            throw new BusinessException("返回报文为空");
        }else {
            log.info("columnResponse="+columnResponse);
        }
        JSONObject dataJsonObject = JSON.parseObject(columnResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            String columnId = dataElement.getString("column_id");
            if("373".equals(columnId)){
                log.info("同步商铺开始...");
                JSONArray relationOptions = dataElement.getJSONArray("options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject shopJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",shopJsonObj.getString("id"));
                    Shop shop = shopService.getOne(queryWrapper);
                    if(shop == null){
                        Shop saveShop = new Shop();
                        saveShop.setId(shopJsonObj.getString("id"));
                        saveShop.setShopName(shopJsonObj.getString("title"));
                        shopService.save(saveShop);
                    }
                }
                log.info("同步商铺结束...");
            }

            if("456".equals(columnId)){
                log.info("同步仓库开始...");
                JSONArray relationOptions = dataElement.getJSONArray("options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject shopJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<WareHouse> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",shopJsonObj.getString("id"));
                    WareHouse wareHouse = wareHouseService.getOne(queryWrapper);
                    if(wareHouse == null){
                        WareHouse saveWareHouse = new WareHouse();
                        saveWareHouse.setId(shopJsonObj.getString("id"));
                        saveWareHouse.setWareHouseName(shopJsonObj.getString("title"));
                        wareHouseService.save(saveWareHouse);
                    }
                }
                log.info("同步仓库结束...");
            }

            if("459".equals(columnId)){
                log.info("同步处理结果开始...");
                JSONArray relationOptions = dataElement.getJSONArray("relation_options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject issueTypeJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<IssueType> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",issueTypeJsonObj.getString("id"));
                    IssueType issueType = issueTypeService.getOne(queryWrapper);
                    if(issueType == null){
                        WareHouse saveWareHouse = new WareHouse();
                        saveWareHouse.setId(issueTypeJsonObj.getString("id"));
                        saveWareHouse.setWareHouseName(issueTypeJsonObj.getString("title"));
                        wareHouseService.save(saveWareHouse);
                    }
                }
                log.info("同步处理结果结束...");
            }

            if("460".equals(columnId)){
                log.info("同步问题类型开始...");
                JSONArray options = dataElement.getJSONArray("options");
                for(int j = 0; j<options.size();j++){
                    JSONObject processResultJsonObj = options.getJSONObject(j);
                    QueryWrapper<ProcessingResult> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",processResultJsonObj.getString("id"));
                    ProcessingResult processingResult = processingResultService.getOne(queryWrapper);
                    if(processingResult == null){
                        ProcessingResult saveProcessingResult = new ProcessingResult();
                        saveProcessingResult.setId(processResultJsonObj.getString("id"));
                        saveProcessingResult.setDescription(processResultJsonObj.getString("title"));
                        processingResultService.save(saveProcessingResult);
                    }
                }
                log.info("同步问题类型结束...");
            }
        }

    }

    @Override
    public void initAllergyReactionOrder() {
        //获取组件枚举
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        //359 仓库售后异常等级表  558 过敏订单 1349 补发单
        String project_id = "558";
        String app_secret = "71dc01248f11697674f3e610971c3013";

        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method", "column.list");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        params.put("timestamp", date);
        params.put("project_id", project_id);
        params.put("sign", null);

        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));

        String base_url = "https://open.bytenew.com/gateway/api/miniAPI";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");
        log.info("full_url="+full_url);

        // 发送请求并获取响应
        // 使用你喜欢的 HTTP 请求库发送 GET 请求
        // 这里只是一个示例
        String columnResponse = byteNewHttpService.send_get_request(full_url);
        if(StrUtil.isEmpty(columnResponse)){
            log.info("columnResponse="+columnResponse);
            throw new BusinessException("返回报文为空");
        }else {
            log.info("columnResponse="+columnResponse);
        }
        JSONObject dataJsonObject = JSON.parseObject(columnResponse);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            String columnId = dataElement.getString("column_id");
            if("567".equals(columnId)){
                log.info("同步商铺开始...");
                JSONArray relationOptions = dataElement.getJSONArray("options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject shopJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",shopJsonObj.getString("id"));
                    Shop shop = shopService.getOne(queryWrapper);
                    if(shop == null){
                        Shop saveShop = new Shop();
                        saveShop.setId(shopJsonObj.getString("id"));
                        saveShop.setShopName(shopJsonObj.getString("title"));
                        shopService.save(saveShop);
                    }
                }
                log.info("同步商铺结束...");
            }

            if("655".equals(columnId)){
                log.info("同步过敏症状数据开始...");
                JSONArray relationOptions = dataElement.getJSONArray("options");
                for(int j = 0; j<relationOptions.size();j++){
                    JSONObject symptomsJsonObj = relationOptions.getJSONObject(j);
                    QueryWrapper<AllergicSymptoms> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id",symptomsJsonObj.getString("id"));
                    AllergicSymptoms allergicSymptoms = allergicSymptomsService.getOne(queryWrapper);
                    if(allergicSymptoms == null){
                        AllergicSymptoms saveAllergicSymptoms = new AllergicSymptoms();
                        saveAllergicSymptoms.setId(symptomsJsonObj.getString("id"));
                        saveAllergicSymptoms.setAllergicSymptoms(symptomsJsonObj.getString("title"));
                        allergicSymptomsService.save(saveAllergicSymptoms);
                    }
                }
                log.info("同步过敏症状数据结束...");
            }
        }
    }

    @Override
    public void initShop() {
        String app_key = "3331225291";
        String access_token = "f0507b2d2d2d3d91f741c4d4277344ba";
        String app_secret = "71dc01248f11697674f3e610971c3013";
        Map<String, String> params = new HashMap<>();
        params.put("app_key", app_key);
        params.put("v", "1.0");
        params.put("method", "common.getSeller");
        params.put("access_token", access_token);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, app_secret));
        String base_url = "https://open.bytenew.com/gateway/api/bnAPI2";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");

        String responseBody = byteNewHttpService.send_get_request(full_url);
        System.out.println("columnResponse="+responseBody);
        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("response").getJSONObject("map").getJSONArray("result");
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            String id = dataElement.getString("id");
            QueryWrapper<Shop> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",id);
            Shop shop = shopService.getOne(queryWrapper);
            if(shop == null){
                Shop saveShop = new Shop();
                saveShop.setId(dataElement.getString("id"));
                saveShop.setShopName(dataElement.getString("title"));
                shopService.save(saveShop);
            }
        }
    }

    @Override
    public Map<String,Integer> initUser() {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", "3331225291");
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method","user.list");
        params.put("access_token", "f0507b2d2d2d3d91f741c4d4277344ba");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, "71dc01248f11697674f3e610971c3013"));
        String base_url = "https://open.bytenew.com/gateway/api/user";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");
        //System.out.println("full_url="+full_url);
        // 设置请求体参数
        Map<String,String> body = new HashMap<>();
        body.put("pageSize","100");
        body.put("pageNum","1");
        String responseBody = byteNewHttpService.send_post_request(full_url,body);
        log.info("responseBody="+responseBody);
        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONObject("map").getJSONArray("result");
        List<ByteNewUser> byteNewUserList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            ByteNewUser byteNewUser = new ByteNewUser();
            for(String key : dataElement.keySet()){
                if("nick".equals(key)){
                    byteNewUser.setNick(dataElement.get(key).toString());
                }
                if("id".equals(key)){
                    byteNewUser.setId(dataElement.get(key).toString());
                }
            }
            QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",byteNewUser.getId());
            ByteNewUser existUser = byteNewUserService.getOne(queryWrapper);
            if(existUser == null){
                byteNewUserService.save(byteNewUser);
            }
        }
        Map<String,Integer> map = new HashMap<>();
        Integer total = dataJsonObject.getJSONObject("data").getJSONObject("map").getInteger("total");
        Integer pageNum = dataJsonObject.getJSONObject("data").getJSONObject("map").getInteger("page_num");
        Integer pageSize = dataJsonObject.getJSONObject("data").getJSONObject("map").getInteger("page_size");
        map.put("total",total);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        return map;
    }

    @Override
    public void initUser(String pageNum) {
        // 创建 HttpClient 对象
        HttpClient httpClient = HttpClients.createDefault();
        // 设置请求参数
        Map<String, String> params = new HashMap<>();
        params.put("app_key", "3331225291");
        params.put("v", "1.0");
        //task.list.new column.list
        params.put("method","user.list");
        params.put("access_token", "f0507b2d2d2d3d91f741c4d4277344ba");
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        params.put("timestamp", date);
        // 生成 sign 参数
        params.put("sign", byteNewHttpService.generate_sign(params, "71dc01248f11697674f3e610971c3013"));
        String base_url = "https://open.bytenew.com/gateway/api/user";
        String full_url = base_url + "?" + byteNewHttpService.urlencode(params);
        full_url = full_url.replace(" ","%20");
        //System.out.println("full_url="+full_url);
        // 设置请求体参数
        Map<String,String> body = new HashMap<>();
        body.put("pageSize","100");
        body.put("pageNum",pageNum);
        String responseBody = byteNewHttpService.send_post_request(full_url,body);
        log.info("responseBody="+responseBody);
        JSONObject dataJsonObject = JSON.parseObject(responseBody);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONObject("map").getJSONArray("result");
        List<ByteNewUser> byteNewUserList = new ArrayList<>();
        for (int i = 0; i < dataResultArray.size(); i++) {
            JSONObject dataElement = dataResultArray.getJSONObject(i);
            ByteNewUser byteNewUser = new ByteNewUser();
            for(String key : dataElement.keySet()){
                if("nick".equals(key)){
                    byteNewUser.setNick(dataElement.get(key).toString());
                }
                if("id".equals(key)){
                    byteNewUser.setId(dataElement.get(key).toString());
                }
            }
            QueryWrapper<ByteNewUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",byteNewUser.getId());
            ByteNewUser existUser = byteNewUserService.getOne(queryWrapper);
            if(existUser == null){
                byteNewUserService.save(byteNewUser);
            }
        }
    }
}
