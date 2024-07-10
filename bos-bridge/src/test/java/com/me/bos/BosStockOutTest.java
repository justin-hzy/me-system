package com.me.bos;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.modules.order.profit.dto.QryProfitReqDto;
import com.me.modules.order.profit.dto.QryProfitRespDto;
import com.me.modules.order.profit.service.OrderProfitService;
import com.me.modules.order.shop.entity.ShopRel;
import com.me.modules.order.shop.service.ShopRelService;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOut;
import com.me.modules.order.stockout.entity.WangDianAbroadStockOutList;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutListService;
import com.me.modules.order.stockout.service.WangDianAbroadStockOutService;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.service.StoreRelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
@Slf4j
public class BosStockOutTest {


    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Autowired
    private WangDianAbroadStockOutService wangDianAbroadStockOutService;

    @Autowired
    private WangDianAbroadStockOutListService wangDianAbroadStockOutListService;

    @Autowired
    private OrderProfitService orderProfitService;


    @Autowired
    private ShopRelService shopRelService;

    @Autowired
    private StoreRelService storeRelService;

    @Test
    public void getStockOutOrderDetails(){
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/stock/v1/getStockOutOrderDetails";

        long time = (System.currentTimeMillis() / 1000);
        String timestamp = String.valueOf(time);

        Map<String,String> header = new HashMap<>();
        header.put("appName",appName);
        header.put("sid",sid);
        header.put("timestamp",timestamp);

        Map<String,String> signMap = new TreeMap<>();
        signMap.put("sid", sid);
        signMap.put("appName", appName);
        signMap.put("timestamp",timestamp);

        //JSONObject body = new JSONObject();
        Map<String,Object> body = new HashMap<>();
        body.put("pageNo",1);
        body.put("pageSize",200);
        //body.put("startTime", "2023-12-01 00:00:00");
        //body.put("endTime","2023-12-01 16:00:00");
        body.put("order_status",95);
        //0 出库时间 1 创建时间
        body.put("status",0);
        body.put("start_time","2024-01-01 00:00:00");
        body.put("end_time","2024-01-31 23:59:59");
        body.put("src_order_type",1);
        //JY202312010003

        signMap.put("body", JSON.toJSONString(body));
        String signBe = linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = sendPostRequest(baseUrl,header,body);
        //log.info("response="+response);


        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("data");
        List<WangDianAbroadStockOut> stockOuts = new ArrayList<>();
        List<WangDianAbroadStockOutList> stockOutDetails = new ArrayList<>();
        for(int i=0;i<dataResultArray.size();i++){
            JSONObject jsonObject = dataResultArray.getJSONObject(i);
            String stockoutId = jsonObject.getString("stockoutId");
            QueryWrapper<WangDianAbroadStockOut> stockOutQueryWrapper = new QueryWrapper<>();
            stockOutQueryWrapper.eq("STOCKOUTID",stockoutId);
            WangDianAbroadStockOut existData = wangDianAbroadStockOutService.getOne(stockOutQueryWrapper);
            if(existData == null){
                String stockoutNo = jsonObject.getString("stockoutNo");

                String warehouseNo = jsonObject.getString("warehouseNo");
                QueryWrapper<StoreRel> storeRelQueryWrapper = new QueryWrapper<>();
                storeRelQueryWrapper.eq("ABD_WAREHOUSE_NO",warehouseNo);
                StoreRel storeRel = storeRelService.getOne(storeRelQueryWrapper);
                warehouseNo = storeRel.getErpWareHouseNo();

                String consignTime = jsonObject.getString("consignTime");

                String shopName = jsonObject.getString("shopName");
                QueryWrapper<ShopRel> shopRelQueryWrapper = new QueryWrapper<>();
                shopRelQueryWrapper.eq("ABD_SHOPNAME",shopName);
                ShopRel shopRel = shopRelService.getOne(shopRelQueryWrapper);
                shopName = shopRel.getErpShopNAME();


                String srcOrderNo = jsonObject.getString("srcOrderNo");
                String orderStatus = jsonObject.getString("orderStatus");
                //log.info("stockoutId="+stockoutId+",stockoutNo="+stockoutNo+",srcOrderNo="+srcOrderNo+",warehouseNo="+warehouseNo+",consignTime="+consignTime+",shopName="+shopName);
                WangDianAbroadStockOut wangDianAbroadStockOut = new WangDianAbroadStockOut();
                wangDianAbroadStockOut.setSTOCKOUTID(stockoutId);
                wangDianAbroadStockOut.setORDERNO(srcOrderNo);
                wangDianAbroadStockOut.setWAREHOUSENO(warehouseNo);
                wangDianAbroadStockOut.setCONSIGNTIME(consignTime);
                //1:网店销售
                wangDianAbroadStockOut.setTRADETYPE("1");
                wangDianAbroadStockOut.setSHOPNAME(shopName);
                wangDianAbroadStockOut.setSRCTIDS(stockoutNo);
                wangDianAbroadStockOut.setTRADESTATUS(orderStatus);
                stockOuts.add(wangDianAbroadStockOut);

                //解析明细接口
                JSONArray detailArray = jsonObject.getJSONArray("stockOutOrderDetailsVOList");
                for(int j=0;j<detailArray.size();j++){
                    WangDianAbroadStockOutList stockOutDetail = new WangDianAbroadStockOutList();
                    JSONObject detailObj = detailArray.getJSONObject(j);
                    String specNo = detailObj.getString("specNo");
                    String num = detailObj.getString("num");
                    stockOutDetail.setSTOCKOUTID(stockoutId);
                    stockOutDetail.setSPECNO(specNo);
                    // 待利润报表开发后获取收入小计作为销售价

                    QryProfitReqDto reqDto = new QryProfitReqDto();
                    reqDto.setSkuSearchVal(specNo);
                    reqDto.setNumberVal(srcOrderNo);


                    QryProfitRespDto respDto = orderProfitService.queryProfit(reqDto);

                    //log.info("tradeNo="+respDto.getTradeNo()+",tid="+respDto.getTid()+",srcOid="+respDto.getSrcOid()+",totalIncome="+respDto.getTotalIncome());

                    stockOutDetail.setSELLPRICE(respDto.getTotalIncome().toString());



                    //默认邮费为0
                    stockOutDetail.setSHAREPOST("0");
                    stockOutDetail.setSRCOID(stockoutNo);
                    stockOutDetail.setSRCTID(stockoutNo);
                    stockOutDetail.setNUM(num);
                    stockOutDetails.add(stockOutDetail);
                }
            }
        }
        if(stockOuts.size()>0){
            wangDianAbroadStockOutService.saveBatch(stockOuts);
        }
        if(stockOutDetails.size()>0){
            wangDianAbroadStockOutListService.saveBatch(stockOutDetails);
        }

        //log.info("stockOuts="+stockOuts.toString());

    }

    public static String linkParams(Map<String, String> map, String secret) {
        StringBuilder sb = new StringBuilder();
        sb.append(secret);
        for (Map.Entry<String, String> item : map.entrySet())
        {
            if (item.getKey().equals("sign"))
                continue;
            sb.append(item.getKey());
            sb.append(item.getValue());
        }
        sb.append(secret);
        return  sb.toString();
    }

    public static String urlencode(Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return encodedParams.toString();
    }


    public static String sendPostRequest(String url, Map<String, String> header) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (executionCount > 3) {
                            return false;
                        }
                        return true;
                    })
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            // 设置header参数
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            // 封装body参数
            List<NameValuePair> params = new ArrayList<>();
            /*for (Map.Entry<String, Integer> entry : body.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }*/
            /*for(String key : body.keySet()){
                params.add(new BasicNameValuePair(key,body.getString(key)));

            }

            HttpEntity entity = new UrlEncodedFormEntity(params);
            httpPost.setEntity(entity);*/

            Map<String,Object> map = new HashMap<>();
//            map.put("tradeStatusCode",4);
//            map.put("pageNo",1);
//            map.put("pageSize",200);
//            map.put("createTimeBegin","2023-11-22 00:00:00");
//            map.put("createTimeEnd","2023-11-25 15:00:00");


            StringEntity entity = new StringEntity(JSON.toJSONString(map),"utf-8");
            httpPost.setEntity(entity);


            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sendPostRequest(String url, Map<String, String> header, Map<String,Object> body) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setRetryHandler((exception, executionCount, context) -> {
                        if (executionCount > 3) {
                            return false;
                        }
                        return true;
                    })
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");

            // 设置header参数
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }

            // 封装body参数
            StringEntity entity = new StringEntity(JSON.toJSONString(body),"utf-8");
            log.info(JSON.toJSONString(body));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            return responseBody;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
