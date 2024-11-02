package com.me.modules.inventory.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.common.config.K3Config;
import com.me.common.core.JsonResult;
import com.me.modules.inventory.dto.K3InventoryReqDto;
import com.me.modules.inventory.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private K3Config k3Config;

    @Override
    public String getTWInventory(String sku, String stockNumber) throws Exception {
        K3InventoryReqDto dto = new K3InventoryReqDto();

        dto.setFormId("STK_Inventory");
        dto.setFieldKeys("FStockId.fnumber,FStockId.fname,FBaseQty");

        String FilterString = "FStockOrgId.fnumber in ('ZT026') and FMaterialId.fnumber="+"'"+sku+"'" +
                "and FStockStatusId.fname='可用' and FStockId.fnumber = "+"'"+stockNumber+"'"+
                "and FLot.fnumber  = '"+k3Config.getFlot()+"' " +
                "and FProduceDate = '"+k3Config.getFProduceDate()+"' " +
                "and FExpiryDate = '"+k3Config.getFExpiryDate()+"'";
        dto.setFilterString(FilterString);

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(dto);

        log.info("json="+json);

        IdentifyInfo iden = new IdentifyInfo();

        iden.setAppId(k3Config.getTwAppId());
        iden.setdCID(k3Config.getTwdCID());
        iden.setAppSecret(k3Config.getTwAppSecret());

        iden.setUserName(k3Config.getUserName());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());

        K3CloudApi api = new K3CloudApi(iden);

        String resultJson = String.valueOf(api.billQuery(json));
        log.info("resultJson="+resultJson);

        JSONObject resJson = new JSONObject();
        resJson.put("code",200);
        resJson.put("data",resultJson);

        return resJson.toJSONString();
    }

    @Override
    public String getBatTWInventory(List<String> skuList, String stockNumber) throws Exception {

        K3InventoryReqDto dto = new K3InventoryReqDto();

        dto.setFormId("STK_Inventory");
        dto.setFieldKeys("FStockId.fnumber,FStockId.fname,FBaseQty,FMaterialId.fnumber");

        String skus = "";

        for(String sku : skuList){
            skus = skus + "'" + sku + "'"+",";
        }

        skus = skus.substring(0,skus.length()-1);

        String FilterString = "FStockOrgId.fnumber in ('ZT026') and FMaterialId.fnumber in("+skus+") and FStockStatusId.fname='可用' " +
                "and FStockId.fnumber = "+"'"+stockNumber+"'"+"and " +
                "FLot.fnumber  = '"+k3Config.getFlot()+"' " +
                "and FProduceDate = '"+k3Config.getFProduceDate()+"' " +
                "and FExpiryDate = '"+k3Config.getFExpiryDate()+"'";
        dto.setFilterString(FilterString);

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();
        String json = gson.toJson(dto);

        log.info("json="+json);

        IdentifyInfo iden = new IdentifyInfo();
        iden.setAppId(k3Config.getTwAppId());
        iden.setdCID(k3Config.getTwdCID());
        iden.setAppSecret(k3Config.getTwAppSecret());

        iden.setUserName(k3Config.getUserName());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());


        K3CloudApi api = new K3CloudApi(iden);

        String resultJsonStr = String.valueOf(api.billQuery(json));
        log.info("resultJsonStr="+resultJsonStr);

        JSONObject resJson = new JSONObject();
        resJson.put("code",200);
        resJson.put("data",resultJsonStr);
        return resJson.toJSONString();
    }


}
