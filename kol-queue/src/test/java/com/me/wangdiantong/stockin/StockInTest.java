package com.me.wangdiantong.stockin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.wangdianstockout.entity.WangDianStockOut;
import com.me.modules.wangdianstockout.entity.WangDianStockOutList;
import com.wangdian.api.WdtClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class StockInTest {

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void getWangDianTongStockIn(){
        String sid = "wmt2";
        String appKey = "wmt2-ot";
        String appSecret = "1e167eecd6a41a771632a1ad1611f40c";
        String baseUrl = "https://api.wangdian.cn/openapi2";
        WdtClient client = new WdtClient(sid, appKey, appSecret, baseUrl);
        Map<String, String> params = new HashMap<String, String>();
        params.put("start_time", "2023-12-30 00:00:00");
        params.put("end_time", "2024-12-31 23:59:59");
        // 2:28082 1:20104 0:31156
        params.put("time_type","2");
        params.put("process_status","90");
        try {
            String response = client.execute("refund_query.php", params);
            log.info(response);
            //解析 response
            JSONObject dataJsonObject = JSON.parseObject(response);
            JSONArray refundsArray = dataJsonObject.getJSONArray("refunds");
            Integer totalCount = dataJsonObject.getInteger("total_count");
            log.info("total_count="+totalCount);
//            List<WangDianStockOut> wangDianStockInList = new ArrayList<>();
//            List<WangDianStockOutList> wangDianStockInDetailList = new ArrayList<>();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
