package com.me.modules.order.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.service.OrderService;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutService;
import com.me.modules.sys.service.HttpService;
import com.wangdian.api.WdtClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private WangDianAbroadStockOutService wangDianAbroadStockOutService;

    final private HttpService httpService;

    final private String appName = "wmt";

    final private String appKey = "d29851b508704fda872360e3760e3b1e";

    final private String sid = "wmt";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> getAbdOrder(Integer pageNo,Integer pageSize,Integer tradeStatusCode) {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/trade/v1/getSalesTradeList";
        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        //JSONObject body = new JSONObject();
        Map<String,Object> body = new HashMap<>();
        body.put("pageNo",pageNo);
        body.put("pageSize",pageSize);
        body.put("tradeStatusCode",tradeStatusCode);
        /*body.put("createTimeBegin", DateUtils.getAboardCreateDate());
        body.put("createTimeEnd",DateUtils.getAboardEndDate());
        log.info(DateUtils.getAboardCreateDate());
        log.info(DateUtils.getAboardEndDate());*/
        /*body.put("createTimeBegin", "2023-12-19 23:13:00");
        body.put("createTimeEnd","2023-12-20 00:00:00");*/
        body.put("createTimeBegin", "2023-12-18 00:00:00");
        body.put("createTimeEnd","2023-12-20 00:00:00");

        signMap.put("body",JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        //log.info("加密前字符串="+signBe);
        String sign = SecureUtil.md5(signBe);
        //log.info("sign="+sign);

        Map<String,String> parameter = new HashMap<>();
        parameter.put("sid", sid);
        parameter.put("appName", appName);
        parameter.put("timestamp",timestamp);
        parameter.put("sign",sign);

        String fullUrl = baseUrl+"?"+httpService.urlencode(parameter);
        String response = httpService.sendPostRequest(fullUrl,header,body);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("data");
        JSONObject jsonObj = dataJsonObject.getJSONObject("data");
        List<WangDianAbroadStockOut> wangDianAbroadStockOutList = new ArrayList<>();
        for (int i=0;i<dataResultArray.size();i++){




        }
        wangDianAbroadStockOutService.saveBatch(wangDianAbroadStockOutList);
        log.info("wangDianAbroadStockOutList.size()="+wangDianAbroadStockOutList.size());
        Boolean empty = jsonObj.getBoolean("empty");
        Integer size = jsonObj.getInteger("pageSize");
        Integer total = jsonObj.getInteger("total");
        Integer currentPage = jsonObj.getInteger("currentPage");

        Map<String,Object> reMap = new HashMap<>();
        reMap.put("empty",empty);
        reMap.put("pageSize",size);
        reMap.put("total",total);
        reMap.put("currentPage",currentPage);
        return reMap;
    }

    @Override
    public void getOrder() {
        String sid = "wmt2";
        String appKey = "1e167eecd6a41a771632a1ad1611f40c";

        String baseUrl = "https://api.wangdian.cn/openapi2/stockout_order_query_trade.php";

        WdtClient client = new WdtClient("wmt2", "1e167eecd6a41a771632a1ad1611f40c", "传入appsecret", "传入url");

    }

}
