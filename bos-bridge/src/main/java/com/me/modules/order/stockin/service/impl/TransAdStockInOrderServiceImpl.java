package com.me.modules.order.stockin.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.exception.BusinessException;
import com.me.modules.order.shop.entity.ShopRel;
import com.me.modules.order.shop.service.ShopRelService;
import com.me.modules.order.stockin.entity.WangDianAbroadStockIn;
import com.me.modules.order.stockin.entity.WangDianAbroadStockInList;
import com.me.modules.order.stockin.service.TransAdStockInOrderService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInListService;
import com.me.modules.order.stockin.service.WangDianAbroadStockInService;
import com.me.modules.order.store.entity.StoreRel;
import com.me.modules.order.store.service.StoreRelService;
import com.me.modules.sys.service.HttpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class TransAdStockInOrderServiceImpl implements TransAdStockInOrderService {


    private WangDianAbroadStockInService wangDianAbroadStockInService;


    private WangDianAbroadStockInListService wangDianAbroadStockInListService;

    private HttpService httpService;

    private StoreRelService storeRelService;

    private ShopRelService shopRelService;

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Override
    public Map<String, Object> getStockInOrderDetails(Integer currentPageNo) {
        String baseUrl = "https://openapi.qizhishangke.com/api/openservices/stock/v1/getStockInOrderDetails";

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
        body.put("pageNo",currentPageNo);
        body.put("pageSize",200);
        //body.put("startTime", "2023-12-01 00:00:00");
        //body.put("timeType",2);
        //body.put("endTime","2023-12-01 16:00:00");
        body.put("src_order_type",3);
        body.put("order_status",80);
        body.put("status",0);
        body.put("start_time","2024-01-01 00:00:00");
        body.put("end_time","2024-01-09 11:18:59");
        //JY202312010003

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = httpService.sendPostRequest(baseUrl,header,body);
        log.info("response="+response);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("data");
        List<WangDianAbroadStockIn> stockIns = new ArrayList<>();
        List<WangDianAbroadStockInList> stockInDetails = new ArrayList<>();
        for(int i=0;i<dataResultArray.size();i++){
            String stockinId = dataResultArray.getJSONObject(i).getString("stockinId");
            QueryWrapper<WangDianAbroadStockIn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("STOCKINID",stockinId);
            WangDianAbroadStockIn existData = wangDianAbroadStockInService.getOne(queryWrapper);
            if (existData == null){
                WangDianAbroadStockIn abroadStockIn = new WangDianAbroadStockIn();
                String stockinNo = dataResultArray.getJSONObject(i).getString("stockinNo");
                String warehouseName = dataResultArray.getJSONObject(i).getString("warehouseName");
                String warehouseNo = dataResultArray.getJSONObject(i).getString("warehouseNo");
                //String shopName = dataResultArray.getJSONObject(i).getString("shopName");
                //查询仓库映射表,获取erp的店仓编码
                QueryWrapper<StoreRel> storeRelQueryWrapper = new QueryWrapper<>();
                storeRelQueryWrapper.eq("ABD_WAREHOUSE_NO",warehouseNo);
                StoreRel storeRel = storeRelService.getOne(storeRelQueryWrapper);
                if(storeRel == null){
                    log.info(warehouseName+"在仓库映射表中没有对应的仓库编码，不允许!");
                    continue;
                }
                String checkTime = dataResultArray.getJSONObject(i).getString("checkTime");
                abroadStockIn.setStockinId(stockinId);
                abroadStockIn.setOrderNo(stockinNo);
                abroadStockIn.setStockinno(stockinNo);
                abroadStockIn.setStockintime(checkTime);

                abroadStockIn.setWarehouseName(warehouseName);
                abroadStockIn.setWarehouseNo(storeRel.getErpWareHouseNo());
                abroadStockIn.setCheckTime(checkTime);
                //待接口优化再补充
                //获取erp店仓名称
//                QueryWrapper<ShopRel> shopRelQueryWrapper = new QueryWrapper<>();
//                shopRelQueryWrapper.eq("ABD_SHOPNAME",shopName);
//                ShopRel shopRel = shopRelService.getOne(shopRelQueryWrapper);
//                abroadStockIn.setShopName("");


                stockIns.add(abroadStockIn);

                //封装明细
                JSONArray stockInOrderDetailsVOList = dataResultArray.getJSONObject(i).getJSONArray("stockInOrderDetailsVOList");
                for(int j=0;j<stockInOrderDetailsVOList.size();j++){
                    JSONObject detailObj = stockInOrderDetailsVOList.getJSONObject(j);
                    String specNo = detailObj.getString("specNo");
                    String num = detailObj.getString("num");
                    WangDianAbroadStockInList stockInDetail = new WangDianAbroadStockInList();
                    stockInDetail.setSTOCKINID(stockinId);
                    stockInDetail.setSPECNO(specNo);
                    stockInDetail.setNUM(num);

                    stockInDetails.add(stockInDetail);
                }

            }
        }

        if(stockIns.size()>0){
            wangDianAbroadStockInService.saveBatch(stockIns);
        }
        if(stockInDetails.size()>0){
            wangDianAbroadStockInListService.saveBatch(stockInDetails);
        }

        Boolean empty = dataJsonObject.getJSONObject("data").getBoolean("empty");
        Integer pageSize = dataJsonObject.getJSONObject("data").getInteger("pageSize");
        Integer total = dataJsonObject.getJSONObject("data").getInteger("total");
        Map<String,Object> resMap = new HashMap<>();
        resMap.put("empty",empty);
        resMap.put("pageSize",pageSize);
        resMap.put("total",total);
        return resMap;
    }

}
