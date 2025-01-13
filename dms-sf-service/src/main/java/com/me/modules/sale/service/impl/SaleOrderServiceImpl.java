package com.me.modules.sale.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.config.DmsConfig;
import com.me.common.config.SfConfig;
import com.me.common.core.JsonResult;
import com.me.common.utils.DmsUtil;
import com.me.modules.http.service.SlHttpService;
import com.me.modules.json.service.JsonService;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.dto.SaleCancelDto;
import com.me.modules.sale.entity.ThSaleOrder;
import com.me.modules.sale.service.SaleOrderService;
import com.me.modules.sale.service.ThSaleOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.me.common.utils.MD5Base64Signature.base64Signature;
import static com.me.common.utils.MD5Base64Signature.md5Encryption;


@Service
@Slf4j
@AllArgsConstructor
public class SaleOrderServiceImpl implements SaleOrderService {

    private JsonService jsonService;

    private SfConfig sfConfig;

    private SlHttpService httpService;

    private DmsConfig dmsConfig;

    private ThSaleOrderService thSaleOrderService;

    @Override
    public JsonResult putSaleOrder(PutSaleOrderDto dto) throws IOException {

        JSONObject saleOrderJson = jsonService.createSaleOrderJson(dto);

        String unescapedJson = StringEscapeUtils.unescapeJson(saleOrderJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String companyCode = dto.getCompanyCode();
        String str = "";
        if("ME-TH01".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey1();
        }else if("ME-TH02".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey2();
        }
        log.info("str="+str);

        String md5EncryptedString = md5Encryption(str);

        String base64SignedString = base64Signature(md5EncryptedString);

        log.info("data_digest="+base64SignedString);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("logistics_interface", unescapedJson));
        params.add(new BasicNameValuePair("data_digest", base64SignedString));
        JSONObject apiRes = httpService.doAction(sfConfig.getUrl(),params);
        String head = apiRes.getString("Head");
        JSONObject resultJson = new JSONObject();
        String note = "";

        if ("OK".equals(head)){
            note = "同步成功";
            resultJson.put("isSf",1);
            resultJson.put("note",note);
            return JsonResult.ok("200",note,resultJson);
        }else {
            JSONArray jsonArray = apiRes.getJSONArray("SaleOrders");
            if(jsonArray.size()==1){
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                note = jsonObject.getString("Note");
            }else {
                note = "顺丰返回数据异常，返回数组元素个数大于dms订单数，无法记录失败原因";
            }
            log.info("请求失败,原因:"+note);
            resultJson.put("isSf",2);
            resultJson.put("note",note);
            return JsonResult.ok("202",note,resultJson);
        }
    }

    @Override
    public JsonResult transSaleOrderDtl(ThSaleOrder thSaleOrder) throws IOException {
        JSONObject saleOrderDtlJson = jsonService.createSaleOrderDtlJson(thSaleOrder);
        //json报文转义
        String unescapedJson = StringEscapeUtils.unescapeJson(saleOrderDtlJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String companyCode = thSaleOrder.getCompanyCode();
        String str = "";
        if("ME-TH01".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey1();
        }else if("ME-TH02".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey2();
        }

        log.info("str="+str);

        String md5EncryptedString = md5Encryption(str);

        String base64SignedString = base64Signature(md5EncryptedString);

        log.info("data_digest="+base64SignedString);


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("logistics_interface", unescapedJson));
        params.add(new BasicNameValuePair("data_digest", base64SignedString));

        JSONObject apiRes = httpService.doAction(sfConfig.getUrl(),params);

        String head = apiRes.getString("Head");

        JSONObject json = new JSONObject();
        JSONArray detailDataArr = new JSONArray();
        JSONObject detailData = new JSONObject();

        JSONArray workflowRequestTableRecordsArr = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String completeTime = dateFormat.format(new Date());
        json.put("requestId", Integer.valueOf(thSaleOrder.getRequestId()));

        if("OK".equals(head)){
            JSONArray saleOrderJsonArr = apiRes.getJSONArray("SaleOrders");
            JSONObject saleOrderJson = saleOrderJsonArr.getJSONObject(0);
            JSONObject headerJson = saleOrderJson.getJSONObject("Header");
            String dataStatus = headerJson.getString("DataStatus");
            if("2900".equals(dataStatus)){
                JSONArray saleOrdersJsonArr = apiRes.getJSONArray("SaleOrders");
                if (CollUtil.isNotEmpty(saleOrdersJsonArr)){
                    JSONObject itemsJson = saleOrdersJsonArr.getJSONObject(0);
                    String erpOrder = itemsJson.getString("ErpOrder");
                    JSONArray jsonArray = itemsJson.getJSONArray("items");
                    for (int i = 0;i<jsonArray.size();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String skuNo = item.getString("SkuNo");
                        String actualQty = item.getString("ActualQty");
                        //log.info("SkuNo="+item.getString("SkuNo")+",ActualQty="+item.get("ActualQty"));
                        JSONObject workflowRequestTableRecords = new JSONObject();
                        Integer recordOrder = 0;

                        JSONArray workflowRequestTableFieldsArr = new JSONArray();

                        JSONObject workflowRequestTableFields1 = new JSONObject();

                        workflowRequestTableFields1.put("fieldName","erp_order");
                        workflowRequestTableFields1.put("fieldValue",erpOrder);

                        JSONObject workflowRequestTableFields2 = new JSONObject();
                        workflowRequestTableFields2.put("fieldName","sku_no");
                        workflowRequestTableFields2.put("fieldValue",skuNo);

                        JSONObject workflowRequestTableFields3 = new JSONObject();
                        workflowRequestTableFields3.put("fieldName","actual_qty");
                        workflowRequestTableFields3.put("fieldValue",actualQty);

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
                    detailData.put("tableDBName",dmsConfig.getSaleDt2());

                    JSONArray mainDataArr = new JSONArray();
                    JSONObject mainData1 = new JSONObject();
                    mainData1.put("fieldName","is_send");
                    mainData1.put("fieldValue","0");
                    mainDataArr.add(mainData1);

                    json.put("mainData",mainDataArr);


                    detailDataArr.add(detailData);


                    json.put("detailData",detailDataArr);

                    log.info(json.toJSONString());

                    try{
                        DmsUtil.testRegist(dmsConfig.getIp());
                        DmsUtil.testGetoken(dmsConfig.getIp());
                        DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),json.toJSONString());
                    }catch (Exception e){
                        log.info("订单回传异常，数据进入中间表,requestId="+ thSaleOrder.getRequestId()+",提交失败");
                    }
                }
            }else {
                log.info(thSaleOrder.getErpOrder()+"----------"+"暂未出库");
            }
        }
        return null;
    }

    public JsonResult transSaleOrderDtl(ThSaleOrder thSaleOrder,String dtlTableName) throws IOException {
        JSONObject saleOrderDtlJson = jsonService.createSaleOrderDtlJson(thSaleOrder);
        //json报文转义
        String unescapedJson = StringEscapeUtils.unescapeJson(saleOrderDtlJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String companyCode = thSaleOrder.getCompanyCode();
        String str = "";
        if("ME-TH01".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey1();
        }else if("ME-TH02".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey2();
        }

        log.info("str="+str);

        String md5EncryptedString = md5Encryption(str);

        String base64SignedString = base64Signature(md5EncryptedString);

        log.info("data_digest="+base64SignedString);


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("logistics_interface", unescapedJson));
        params.add(new BasicNameValuePair("data_digest", base64SignedString));

        JSONObject apiRes = httpService.doAction(sfConfig.getUrl(),params);

        String head = apiRes.getString("Head");

        JSONObject json = new JSONObject();
        JSONArray detailDataArr = new JSONArray();
        JSONObject detailData = new JSONObject();

        JSONArray workflowRequestTableRecordsArr = new JSONArray();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String completeTime = dateFormat.format(new Date());
        json.put("requestId", Integer.valueOf(thSaleOrder.getRequestId()));

        if("OK".equals(head)){
            JSONArray saleOrderJsonArr = apiRes.getJSONArray("SaleOrders");
            JSONObject saleOrderJson = saleOrderJsonArr.getJSONObject(0);
            JSONObject headerJson = saleOrderJson.getJSONObject("Header");
            String dataStatus = headerJson.getString("DataStatus");
            if("2900".equals(dataStatus)){
                JSONArray saleOrdersJsonArr = apiRes.getJSONArray("SaleOrders");
                if (CollUtil.isNotEmpty(saleOrdersJsonArr)){
                    JSONObject itemsJson = saleOrdersJsonArr.getJSONObject(0);
                    String erpOrder = itemsJson.getString("ErpOrder");
                    JSONArray jsonArray = itemsJson.getJSONArray("items");
                    for (int i = 0;i<jsonArray.size();i++){
                        JSONObject item = jsonArray.getJSONObject(i);
                        String skuNo = item.getString("SkuNo");
                        String actualQty = item.getString("ActualQty");
                        //log.info("SkuNo="+item.getString("SkuNo")+",ActualQty="+item.get("ActualQty"));
                        JSONObject workflowRequestTableRecords = new JSONObject();
                        Integer recordOrder = 0;

                        JSONArray workflowRequestTableFieldsArr = new JSONArray();

                        JSONObject workflowRequestTableFields1 = new JSONObject();

                        workflowRequestTableFields1.put("fieldName","erp_order");
                        workflowRequestTableFields1.put("fieldValue",erpOrder);

                        JSONObject workflowRequestTableFields2 = new JSONObject();
                        workflowRequestTableFields2.put("fieldName","sku_no");
                        workflowRequestTableFields2.put("fieldValue",skuNo);

                        JSONObject workflowRequestTableFields3 = new JSONObject();
                        workflowRequestTableFields3.put("fieldName","actual_qty");
                        workflowRequestTableFields3.put("fieldValue",actualQty);

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
                    detailData.put("tableDBName",dtlTableName);

                    JSONArray mainDataArr = new JSONArray();
                    JSONObject mainData1 = new JSONObject();
                    mainData1.put("fieldName","is_send");
                    mainData1.put("fieldValue","0");
                    mainDataArr.add(mainData1);

                    json.put("mainData",mainDataArr);


                    detailDataArr.add(detailData);


                    json.put("detailData",detailDataArr);

                    log.info(json.toJSONString());

                    try{
                        DmsUtil.testRegist(dmsConfig.getIp());
                        DmsUtil.testGetoken(dmsConfig.getIp());
                        DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),json.toJSONString());
                    }catch (Exception e){
                        log.info("订单回传异常，数据进入中间表,requestId="+ thSaleOrder.getRequestId()+",提交失败");
                    }
                }
            }else {
                log.info(thSaleOrder.getErpOrder()+"----------"+"暂未出库");
            }
        }
        return null;
    }


    @Override
    public JsonResult saleCancel(SaleCancelDto dto) throws IOException {
        JSONObject saleCancelJson = jsonService.createSaleCancel(dto);
        String unescapedJson = StringEscapeUtils.unescapeJson(saleCancelJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String companyCode = dto.getCompanyCode();
        String str = "";
        if("ME-TH01".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey1();
        }else if("ME-TH02".equals(companyCode)){
            str = unescapedJson+sfConfig.getKey2();
        }

        log.info("str="+str);

        String md5EncryptedString = md5Encryption(str);

        String base64SignedString = base64Signature(md5EncryptedString);

        log.info("data_digest="+base64SignedString);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("logistics_interface", unescapedJson));
        params.add(new BasicNameValuePair("data_digest", base64SignedString));

        JSONObject apiRes = httpService.doAction(sfConfig.getUrl(),params);

        String head = apiRes.getString("Head");

        return null;
    }
}
