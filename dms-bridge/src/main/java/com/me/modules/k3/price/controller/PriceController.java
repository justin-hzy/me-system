package com.me.modules.k3.price.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.common.config.K3Config;
import com.me.modules.k3.price.dto.GetPriceReqDto;
import com.me.modules.k3.price.dto.K3PriceReqDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
@Slf4j
public class PriceController {

    private K3Config k3Config;

    @PostMapping("getPrice")
    public String getPrice(@RequestBody GetPriceReqDto reqDto) throws Exception {

        K3PriceReqDto dto = new K3PriceReqDto();
        /*String sku1 = "4531632422077-1";
        String sku2 = "4531632422077-2";*/

        List<String> skus = new ArrayList<>();
        skus.add(reqDto.getSku());
        /*skus.add(sku2);*/

        String skuStr= "";

        for (String sku : skus){
            skuStr = skuStr+"'"+sku+"',";
        }

        //log.info("skuStr="+skuStr);

        skuStr = skuStr.substring(0, skuStr.length() - 1);
        log.info("skuStr=" + skuStr);

        String stockNumber = "FL004";

        dto.setFormId("PUR_PriceCategory");
        dto.setFieldKeys("FMaterialId.FNumber,FTaxPrice,FTaxRate");

        String FilterString = " Fnumber = 'CGJM000032' and FMaterialId.FNumber in ("+skuStr+")";

        dto.setFilterString(FilterString);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        IdentifyInfo iden = new IdentifyInfo();
        iden.setUserName(k3Config.getUserName());
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());

        K3CloudApi api = new K3CloudApi(iden);
        log.info("json="+json);
        String resultJson = String.valueOf(api.billQuery(json));
        log.info("resultJson="+resultJson);

        JSONArray jsonArray = JSONArray.parseArray(resultJson);

        String fTaxPrice = "";

        if (jsonArray.size()>0){
            fTaxPrice = jsonArray.getJSONObject(0).getString("FTaxPrice");
        }else {
            fTaxPrice = "0.00";
        }

        return fTaxPrice;
    }


    @PostMapping("getDailyNecPrice")
    public String getDailyNecPrice(@RequestBody GetPriceReqDto reqDto) throws Exception {

        K3PriceReqDto dto = new K3PriceReqDto();
        /*String sku1 = "4531632422077-1";
        String sku2 = "4531632422077-2";*/

        List<String> skus = new ArrayList<>();
        skus.add(reqDto.getSku());
        /*skus.add(sku2);*/

        String skuStr= "";

        for (String sku : skus){
            skuStr = skuStr+"'"+sku+"',";
        }

        //log.info("skuStr="+skuStr);

        skuStr = skuStr.substring(0, skuStr.length() - 1);
        log.info("skuStr=" + skuStr);

        String stockNumber = "FL004";

        dto.setFormId("PUR_PriceCategory");
        dto.setFieldKeys("FMaterialId.FNumber,FTaxPrice,FTaxRate");

        String FilterString = " Fnumber = 'CGJM000023' and FMaterialId.FNumber in ("+skuStr+")";

        dto.setFilterString(FilterString);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        IdentifyInfo iden = new IdentifyInfo();
        iden.setUserName(k3Config.getUserName());
        iden.setAppId(k3Config.getAppId());
        iden.setdCID(k3Config.getDCID());
        iden.setAppSecret(k3Config.getAppSecret());
        iden.setlCID(k3Config.getLCID());
        iden.setServerUrl(k3Config.getServerUrl());

        K3CloudApi api = new K3CloudApi(iden);
        log.info("json="+json);
        String resultJson = String.valueOf(api.billQuery(json));
        log.info("resultJson="+resultJson);

        JSONArray jsonArray = JSONArray.parseArray(resultJson);

        String fTaxPrice = "";

        if (jsonArray.size()>0){
            fTaxPrice = jsonArray.getJSONObject(0).getString("FTaxPrice");
        }else {
            fTaxPrice = "0.00";
        }

        return fTaxPrice;
    }
}
