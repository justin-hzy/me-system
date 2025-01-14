package com.me.modules.sale.trans.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.me.common.config.DmsConfig;
import com.me.common.config.FlashConfig;
import com.me.common.utils.DmsUtil;
import com.me.modules.http.service.FlashHttpService;
import com.me.modules.json.JsonService;
import com.me.modules.sale.in.entity.FlashInOrder;
import com.me.modules.sale.out.entity.FlashOutOrder;
import com.me.modules.sale.trans.TransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TransServiceImpl implements TransService {

    private JsonService jsonService;

    private FlashHttpService flashHttpService;

    private FlashConfig flashConfig;

    private DmsConfig dmsConfig;

    @Override
    public void transOutOrderDtl(FlashOutOrder flashOutOrder) throws IOException {

        String orderSn = flashOutOrder.getOrderSn();
        JSONObject param = jsonService.createTransOutOrderDtlJson(orderSn);

        Map<String,String> commonParam = flashHttpService.createCommonParam();

        String key = flashConfig.getKey1();

        String sign = flashHttpService.generateSign(commonParam,key,param.toJSONString());

        log.info("sign="+sign);

        String outBoundOrderDetailUrl = flashConfig.getOutOrderDetailUrl();

        outBoundOrderDetailUrl = flashHttpService.joinUrl(commonParam,sign,outBoundOrderDetailUrl);
        log.info("outBoundOrderListUrl="+outBoundOrderDetailUrl);

        JSONObject respJson = flashHttpService.doAction(outBoundOrderDetailUrl,param);

        JSONArray dataJsonArr = respJson.getJSONArray("data");

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("requestId", Integer.valueOf(flashOutOrder.getRequestId()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String completeTime = dateFormat.format(new Date());

        for (int i = 0;i<dataJsonArr.size();i++){
            JSONObject dataJsonObj = dataJsonArr.getJSONObject(i);

            log.info("completeTime="+completeTime);
            JSONArray goodsJsonArr = dataJsonObj.getJSONArray("goods");

            JSONArray detailDataArr = new JSONArray();
            JSONObject detailData = new JSONObject();

            JSONArray workflowRequestTableRecordsArr = new JSONArray();

            for (int j = 0;j<goodsJsonArr.size();j++){
                JSONObject goodJson = goodsJsonArr.getJSONObject(j);
                String barCode = goodJson.getString("barCode");
                String outNum = goodJson.getString("outNum");

                JSONObject workflowRequestTableRecords = new JSONObject();
                Integer recordOrder = 0;

                JSONArray workflowRequestTableFieldsArr = new JSONArray();

                JSONObject workflowRequestTableFields1 = new JSONObject();

                workflowRequestTableFields1.put("fieldName","po");
                workflowRequestTableFields1.put("fieldValue",orderSn);

                JSONObject workflowRequestTableFields2 = new JSONObject();
                workflowRequestTableFields2.put("fieldName","bar_code");
                workflowRequestTableFields2.put("fieldValue",barCode);

                JSONObject workflowRequestTableFields3 = new JSONObject();
                workflowRequestTableFields3.put("fieldName","send_qty");
                workflowRequestTableFields3.put("fieldValue",outNum);

                JSONObject workflowRequestTableFields4 = new JSONObject();
                workflowRequestTableFields4.put("fieldName","send_date");
                workflowRequestTableFields4.put("fieldValue",completeTime);

                workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                workflowRequestTableRecords.put("recordOrder",recordOrder);

                workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
            }

            detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
            detailData.put("tableDBName",dmsConfig.getOutOrderDtl2());

            JSONArray mainDataArr = new JSONArray();
            JSONObject mainData1 = new JSONObject();
            mainData1.put("fieldName","is_send");
            mainData1.put("fieldValue","0");
            mainDataArr.add(mainData1);

            jsonObject.put("mainData",mainDataArr);


            detailDataArr.add(detailData);


            jsonObject.put("detailData",detailDataArr);

            log.info(jsonObject.toJSONString());

            try{
                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }catch (Exception e){
                log.info("订单回传异常，数据进入中间表,requestId="+ flashOutOrder.getRequestId()+",提交失败");
            }
        }
    }

    @Override
    public void transInOrderList(FlashInOrder flashInOrder) throws IOException {
        String orderSn = flashInOrder.getOrderSn();
        JSONObject param = jsonService.createTransInOrderDtlJson(flashInOrder);
        Map<String,String> commonParam = flashHttpService.createCommonParam();
        String key = flashConfig.getKey1();

        String sign = flashHttpService.generateSign(commonParam,key,param.toJSONString());

        log.info("sign="+sign);

        String inOrderDetailUrl = flashConfig.getInOrderDetailUrl();

        inOrderDetailUrl = flashHttpService.joinUrl(commonParam,sign,inOrderDetailUrl);
        log.info("inOrderDetailUrl="+inOrderDetailUrl);

        JSONObject respJson = flashHttpService.doAction(inOrderDetailUrl,param);

        JSONArray dataJsonArr = respJson.getJSONArray("data");


        JSONObject jsonObject = new JSONObject();

        jsonObject.put("requestId", Integer.valueOf(flashInOrder.getRequestId()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String completeTime = dateFormat.format(new Date());


        for (int i = 0;i<dataJsonArr.size();i++){
            JSONObject dataJsonObj = dataJsonArr.getJSONObject(i);
            log.info(dataJsonObj.getString("completeTime"));

            JSONArray detailDataArr = new JSONArray();
            JSONObject detailData = new JSONObject();
            JSONArray workflowRequestTableRecordsArr = new JSONArray();

            JSONArray goodsListJsonArr = dataJsonObj.getJSONArray("goodsList");
            for (int j = 0;j<goodsListJsonArr.size();j++){
                JSONObject goodJson = goodsListJsonArr.getJSONObject(j);
                String barCode = goodJson.getString("barCode");
                String inNum = goodJson.getString("inNum");

                JSONObject workflowRequestTableRecords = new JSONObject();
                Integer recordOrder = 0;

                JSONArray workflowRequestTableFieldsArr = new JSONArray();

                JSONObject workflowRequestTableFields1 = new JSONObject();
                workflowRequestTableFields1.put("fieldName","po");
                workflowRequestTableFields1.put("fieldValue",orderSn);

                JSONObject workflowRequestTableFields2 = new JSONObject();
                workflowRequestTableFields2.put("fieldName","bar_code");
                workflowRequestTableFields2.put("fieldValue",barCode);

                JSONObject workflowRequestTableFields3 = new JSONObject();
                workflowRequestTableFields3.put("fieldName","entry_qty");
                workflowRequestTableFields3.put("fieldValue",inNum);

                JSONObject workflowRequestTableFields4 = new JSONObject();
                workflowRequestTableFields4.put("fieldName","entry_date");
                workflowRequestTableFields4.put("fieldValue",completeTime);

                workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

                workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                workflowRequestTableRecords.put("recordOrder",recordOrder);

                workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
            }

            detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
            detailData.put("tableDBName",dmsConfig.getInOrderDtl2());
            detailDataArr.add(detailData);

            JSONArray mainDataArr = new JSONArray();
            JSONObject mainData1 = new JSONObject();
            mainData1.put("fieldName","is_receive");
            mainData1.put("fieldValue","0");
            mainDataArr.add(mainData1);

            jsonObject.put("mainData",mainDataArr);

            jsonObject.put("detailData",detailDataArr);

            log.info(jsonObject.toJSONString());

            try{
                DmsUtil.testRegist(dmsConfig.getIp());
                DmsUtil.testGetoken(dmsConfig.getIp());
                DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
            }catch (Exception e){
                log.info("订单回传异常，数据进入中间表,requestId="+ flashInOrder.getRequestId()+",提交失败");
            }
        }
    }
}
