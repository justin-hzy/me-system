package com.me.modules.order.profit.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.modules.order.profit.dto.QryProfitReqDto;
import com.me.modules.order.profit.dto.QryProfitRespDto;
import com.me.modules.order.profit.service.OrderProfitService;
import com.me.modules.sys.service.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class OrderProfitServiceImpl implements OrderProfitService {

    @Autowired
    private HttpService httpService;

    static final private String appName = "wmt";

    static final private String appKey = "d29851b508704fda872360e3760e3b1e";

    static final private String sid = "wmt";

    @Override
    public QryProfitRespDto queryProfit(QryProfitReqDto reqDto) {

        String baseUrl = "https://coss.qizhishangke.com/api/openservices/V1/finances/pageReportByOrderItems";
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

        Map<String,Object> body = new HashMap<>();
        body.put("pageNum",1);
        body.put("pageSize",100);
        body.put("skuSearchType",1);
        body.put("profitCalculateType",1);
        body.put("skuSearchVal",reqDto.getSkuSearchVal());
        //body.put("skuSearchVal","6926799657529");
        body.put("numberType",1);
        List<String> numberList = new ArrayList();
        //numberList.add("JY202402290126");
        numberList.add(reqDto.getNumberVal());
        body.put("numberVal",numberList);

        signMap.put("body", JSON.toJSONString(body));
        String signBe = httpService.linkParams(signMap,appKey);
        String sign = SecureUtil.md5(signBe);

        header.put("sign",sign);
        String response = httpService.sendPostRequest(baseUrl,header,body);
        //log.info("profit response="+response);

        //解析 response
        JSONObject dataJsonObject = JSON.parseObject(response);
        JSONArray dataResultArray = dataJsonObject.getJSONObject("data").getJSONArray("list");
        JSONObject element = dataResultArray.getJSONObject(0);
        String tradeNo = element.getString("tradeNo");
        String tid = element.getString("tid");
        String srcOid = element.getString("srcOid");
        BigDecimal exchangeRate = element.getBigDecimal("exchangeRate");

        BigDecimal totalIncome = element.getBigDecimal("totalIncome");
        if(!(totalIncome.compareTo(BigDecimal.ZERO) == 0)){
            totalIncome = totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(exchangeRate);
        }
        /*log.info("tradeNo="+tradeNo+",tid="+tid+",srcOid="+srcOid+",totalIncome="+totalIncome+",exchangeRate="+exchangeRate);*/

        log.info("totalIncome="+totalIncome.toString());

        QryProfitRespDto respDto = new QryProfitRespDto();

        respDto.setTradeNo(tradeNo);
        respDto.setTid(tid);
        respDto.setSrcOid(srcOid);
        respDto.setTotalIncome(totalIncome.toString());

        return respDto;
    }

}
