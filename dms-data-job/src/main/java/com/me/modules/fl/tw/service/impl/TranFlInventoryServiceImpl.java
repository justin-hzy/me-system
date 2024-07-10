package com.me.modules.fl.tw.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.modules.fl.tw.entity.CommStore;
import com.me.modules.fl.tw.entity.FlInventory;
import com.me.modules.fl.tw.entity.FlStoreType;
import com.me.modules.fl.tw.service.CommStoreService;
import com.me.modules.fl.tw.service.FlInventoryService;
import com.me.modules.fl.tw.service.FlStoreTypeService;
import com.me.modules.fl.tw.service.TranFlInventoryService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TranFlInventoryServiceImpl implements TranFlInventoryService {

    private FlStoreTypeService flStoreTypeService;

    private CommStoreService commStoreService;

    private FlInventoryService flInventoryService;

    @Override
    public boolean tranFlInventoryByPage(String page) {

        //String url = "https://upace-api.ibiza.com.tw/v1/stocks?page="+page+"&skus[]=4531632422091";
        String url = "https://upace-api.ibiza.com.tw/v1/stocks?page="+page;
        String json = getStorePage(url);
        JSONArray jsonArr = JSONObject.parseArray(json);
        boolean flag = false;
        if (jsonArr.size()>0){
            flag = true;
            for(int i = 0 ; i<jsonArr.size(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String sku = jsonObj.getString("sku");
                /*if("4711401210740".equals(sku)){
                    log.info(sku);
                }*/
                JSONArray resultArr = jsonObj.getJSONArray("results");
                List<FlInventory> flInventories = new ArrayList<>();
                for(int j = 0 ;j<resultArr.size();j++){
                    JSONObject resultJson = resultArr.getJSONObject(j);
                    String storageType = resultJson.getString("storage_type");
                    String expirationDate = resultJson.getString("expiration_date");
                    String batch = resultJson.getString("batch");

                    QueryWrapper<FlInventory> flInventoryQuery = new QueryWrapper<>();
                    flInventoryQuery.eq("cb",storageType)
                            .eq("gjtm",sku);
                    if(StrUtil.isNotEmpty(expirationDate)){
                        flInventoryQuery.eq("xq",expirationDate);
                    }else {
                        flInventoryQuery.eq("xq","");
                    }

                    if(StrUtil.isNotEmpty(batch)){
                        flInventoryQuery.eq("pc",batch);
                    }else {
                        flInventoryQuery.isNull("pc");
                    }


                    FlInventory resultObj = flInventoryService.getOne(flInventoryQuery);
                    if(resultObj != null){
                        UpdateWrapper<FlInventory> updateWrapper = new UpdateWrapper<>();

                        updateWrapper.eq("cb",storageType)
                                .eq("gjtm",sku);
                        if(StrUtil.isNotEmpty(expirationDate)){
                            updateWrapper.eq("xq",expirationDate);
                        }else {
                            updateWrapper.eq("xq","");
                        }

                        if(StrUtil.isNotEmpty(batch)){
                            updateWrapper.eq("pc",batch);
                        }else {
                            updateWrapper.isNull("pc");
                        }

                        String availableStock = resultJson.getString("available_stock");
                        String stock = resultJson.getString("stock");
                        String transit = resultJson.getString("transit");


                        /*updateWrapper.set("kys",1);
                        updateWrapper.set("zkc",1);
                        updateWrapper.set("ztsl",1);*/

                        updateWrapper.set("kys",availableStock);
                        updateWrapper.set("zkc",stock);
                        updateWrapper.set("ztsl",transit);

                        flInventoryService.update(updateWrapper);
                    }else {
                        String availableStock = resultJson.getString("available_stock");
                        String stock = resultJson.getString("stock");
                        String transit = resultJson.getString("transit");
                        if (transit == null){
                            transit = "0";
                        }

                        FlInventory flInventory = new FlInventory();
                        flInventory.setCb(storageType);
                        QueryWrapper<FlStoreType> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("dh",storageType);
                        FlStoreType flStoreType = flStoreTypeService.getOne(queryWrapper);
                        if(flStoreType == null){
                            flInventory.setCbmc("无法匹配仓别名称");
                        }else {
                            String cb = flStoreType.getCb();
                            flInventory.setCbmc(cb);
                        }

                        flInventory.setXq(expirationDate);
                        flInventory.setPc(batch);
                        flInventory.setKys(availableStock);
                        flInventory.setZtsl(transit);
                        flInventory.setZyl("0");
                        flInventory.setGjtm(sku);
                        flInventory.setZkc(stock);

                        QueryWrapper<CommStore> commStoreQuery = new QueryWrapper<>();

                        //commStoreQuery.eq("hpbh",sku).eq("dq","2");
                        commStoreQuery.eq("hpbh",sku);
                        List<CommStore> commStores = commStoreService.list(commStoreQuery);
                        if (commStores.size()>0){
                            flInventory.setPm(commStores.get(0).getHpmc());
                        }else {
                            flInventory.setPm("无法匹配到品名");
                        }

                        if(!sku.contains("90000000000")){
                            flInventories.add(flInventory);
                        }
                    }
                }
                flInventoryService.saveBatch(flInventories);
            }
        }
        return flag;
    }

    public String getStorePage(String url){
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("Content-Type","application/json");
            request.addHeader("Authorization","Bearer gabikYQloi0CRLf-lZK9t-yuu-fN90SrrRcEcixaSCo");
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                    System.out.println(result);
                }
            } finally {
                response.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }
}
