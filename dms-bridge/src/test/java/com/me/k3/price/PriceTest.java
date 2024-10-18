package com.me.k3.price;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.common.config.K3Config;
import com.me.modules.k3.price.dto.K3PriceReqDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class PriceTest {

    @Autowired
    K3Config k3Config;

    @Test
    void getPrice() throws Exception {
        K3PriceReqDto dto = new K3PriceReqDto();
        String sku1 = "4531632422077-1";
        String sku2 = "4531632422077-2";

        List<String> skus = new ArrayList<>();
        skus.add(sku1);
        skus.add(sku2);

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

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
        }
    }
}
