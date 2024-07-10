package com.me.k3.inventory;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kingdee.bos.webapi.entity.IdentifyInfo;
import com.kingdee.bos.webapi.sdk.K3CloudApi;
import com.me.modules.k3.inventory.dto.K3InventoryReqDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
public class InventoryTest {

    @Test
    public void getInventory() throws Exception {

        K3InventoryReqDto dto = new K3InventoryReqDto();

        String sku = "4531632502182-1";

        String stockNumber = "FL004";

        dto.setFormId("STK_Inventory");
        dto.setFieldKeys("FStockId.fnumber,FStockId.fname,FBaseQty");

        String FilterString = "FStockOrgId.fnumber in ('ZT026') and FMaterialId.fnumber="+"'"+sku+"'and FStockStatusId.fname='可用' and FStockId.fnumber = "+"'"+stockNumber+"'";
        dto.setFilterString(FilterString);

        Gson gson = new Gson();
        String json = gson.toJson(dto);

        IdentifyInfo iden = new IdentifyInfo();
        iden.setUserName("何子毅");
        //生成环境
        /*iden.setAppId("271221_x66IRZvH7ICWQYxEQeQPTYVr4uS9RtME");
        iden.setdCID("640c1e4c514f43");
        iden.setAppSecret("ebb3fd34a06d46f09105be5c44fba9e8");*/

        //测试环境
        iden.setAppId("276104_0f2O4ZDt1nhW3/SEx6QrS78E1hXWWBmv");
        iden.setdCID("661f3771555e3a");
        iden.setAppSecret("acaa9065e5b9491c9d96f46e0531bdaf");

        iden.setlCID(2052);
        iden.setServerUrl("http://kd.za-cosmetics.com/k3Cloud/");

        K3CloudApi api = new K3CloudApi(iden);

        String resultJson = String.valueOf(api.billQuery(json));
        log.info("resultJson="+resultJson);
    }

    @Test
    public void StrTest(){
        String fBaseQty = "607.0000000000";

        fBaseQty = fBaseQty.substring(0,fBaseQty.indexOf("."));

        log.info(fBaseQty);

        /*String xssl = "50";
        if(Long.compare(Long.valueOf(fBaseQty),Long.valueOf(xssl)) < 0){
            log.info("生成香港数据");
            Integer hkNumber = Integer.valueOf(xssl) - Integer.valueOf(fBaseQty) ;
        }*/
    }

    @Test
    public void getBatInv(){
        String meIp = "http://39.108.173.42:8090";
        String url = "/dmsBridge/k3/getBatchInventory";
        CloseableHttpResponse response;// 响应类,
        CloseableHttpClient httpClient = HttpClients.createDefault();


        HttpPost httpPost = new HttpPost(meIp+url);

        String param = "{\n" +
                "    \"stockNumber\":\"A1\",\n" +
                "    \"storeType\":\"TW\",\n" +
                "    \"skus\":[\n" +
                "        \"6952114005942\",\n" +
                "        \"4711401211372\",\n" +
                "        \"4711401211365\"\n" +
                "    ]\n" +
                "}";
        //设置请求头
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        httpPost.setEntity(new StringEntity(param, "UTF-8"));
        try {
            response = httpClient.execute(httpPost);
            String resulString = EntityUtils.toString(response.getEntity());
            log.info("获取接口数据成功，接口返回体：" + resulString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
