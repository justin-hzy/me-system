package com.me.k3.price;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.modules.k3.price.dto.K3PriceReqDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class PriceTest {

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
        iden.setUserName("何子毅");
        iden.setAppId("271221_x66IRZvH7ICWQYxEQeQPTYVr4uS9RtME");
        iden.setdCID("640c1e4c514f43");
        iden.setAppSecret("ebb3fd34a06d46f09105be5c44fba9e8");

        K3CloudApi api = new K3CloudApi(iden);

        String resultJson = String.valueOf(api.billQuery(json));
        log.info("resultJson="+resultJson);

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

        }

    }
}
