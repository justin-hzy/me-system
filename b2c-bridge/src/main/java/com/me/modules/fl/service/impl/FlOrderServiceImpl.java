package com.me.modules.fl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.modules.eccang.entity.EcOrder;
import com.me.modules.eccang.entity.EcProduct;
import com.me.modules.eccang.service.EcOrderService;
import com.me.modules.eccang.service.EcProductService;
import com.me.modules.fl.entity.FlOrderErrLog;
import com.me.modules.fl.service.FlOrderErrLogService;
import com.me.modules.fl.service.FuLunHttpService;
import com.me.modules.fl.service.FlOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlOrderServiceImpl implements FlOrderService {


    private EcOrderService ecOrderService;

    private FuLunHttpService fuLunHttpService;

    private EcProductService ecProductService;

    private FlOrderErrLogService flOrderErrLogService;

    @Override
    public void sendOrder(String param) {

        JSONObject paramJsonObj = JSONObject.parseObject(param);

        String name = paramJsonObj.getString("name");


        String url = "";
        JSONObject apiRes = fuLunHttpService.doPostAction(param,url);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String todayString = today.format(formatter);


        String code = apiRes.getString("code");
        if("0".equals(code)){
            UpdateWrapper<EcOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status","1");
            updateWrapper.set("updateTime",todayString);
            updateWrapper.eq("orderCode",name);

            ecOrderService.update(updateWrapper);
        }else {
            UpdateWrapper<EcOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("status","2");
            updateWrapper.set("updateTime",todayString);
            //保存当前参数，用于用工干预
            updateWrapper.set("params",param);
            updateWrapper.eq("orderCode",name);
            ecOrderService.update(updateWrapper);


            //插入失败log
            FlOrderErrLog errorLog = new FlOrderErrLog();
            errorLog.setName(name);
            errorLog.setParam(param);
            errorLog.setCreateTime(todayString);
            errorLog.setIsDelete("0");
            errorLog.setStatus("0");

            flOrderErrLogService.save(errorLog);
        }
    }

    @Override
    public List<String> queryOrder() {
        QueryWrapper<EcOrder> ecOrderQuery = new QueryWrapper<>();
        ecOrderQuery.eq("status","80");
        ecOrderQuery.last("limit 100");

        List<EcOrder> ecOrders = ecOrderService.list(ecOrderQuery);

        List<String> params = new ArrayList<>();

        for(EcOrder ecOrder : ecOrders){

            String referenceNo = ecOrder.getReferenceNo();

            String orderCode = ecOrder.getOrderCode();

            QueryWrapper<EcProduct> ecProductQuery = new QueryWrapper<>();


            JSONObject reqJsonObj = new JSONObject();
            reqJsonObj.put("name",orderCode);
            reqJsonObj.put("callback_url","");

            JSONArray products = new JSONArray();


            ecProductQuery.eq("referenceNo",referenceNo);

            List<EcProduct> ecProducts  = ecProductService.list(ecProductQuery);

            for (EcProduct ecProduct : ecProducts){
                JSONObject product = new JSONObject();

                String sku = ecProduct.getSku();
                String quantity = ecProduct.getQuantity();
                product.put("sku",sku);
                product.put("quantity",quantity);

                products.add(product);
            }

            reqJsonObj.put("products",products);

            String param = reqJsonObj.toJSONString();

            params.add(param);
        }

        return params;
    }
}
