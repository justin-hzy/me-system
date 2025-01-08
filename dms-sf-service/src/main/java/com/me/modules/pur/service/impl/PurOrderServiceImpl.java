package com.me.modules.pur.service.impl;

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
import com.me.modules.pur.dto.PurCancelDto;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.refund.entity.ThRefund;
import com.me.modules.refund.service.ThRefundService;
import com.me.modules.sale.entity.ThSaleOrder;
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
public class PurOrderServiceImpl implements PurOrderService {

    private JsonService jsonService;

    private SfConfig sfConfig;

    private SlHttpService httpService;

    private DmsConfig dmsConfig;

    private ThRefundService thRefundService;



    @Override
    public JsonResult putPurOrder(PutRefundPurDto dto) throws IOException {
        JSONObject saleOrderJson = jsonService.createPurOrderJson(dto);

        String unescapedJson = StringEscapeUtils.unescapeJson(saleOrderJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String str = unescapedJson+sfConfig.getKey();

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
            JSONArray jsonArray = apiRes.getJSONArray("PurchaseOrders");
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
    public JsonResult transPurOrderDtl(ThRefund thRefund) throws IOException {
        JSONObject purOrderDtlJson = jsonService.createPurOrderDtlJson(thRefund);

        //json报文转义
        String unescapedJson = StringEscapeUtils.unescapeJson(purOrderDtlJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String str = unescapedJson+sfConfig.getKey();

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

        json.put("requestId", Integer.valueOf(thRefund.getRequestId()));

        if("OK".equals(head)){
            JSONArray PurchaseOrdersJsonArr = apiRes.getJSONArray("PurchaseOrders");
            if (CollUtil.isNotEmpty(PurchaseOrdersJsonArr)){
                JSONObject itemsJson = PurchaseOrdersJsonArr.getJSONObject(0);
                JSONArray jsonArray = itemsJson.getJSONArray("Items");

                String erpOrder = itemsJson.getString("ErpOrder");
                for (int j = 0;j<jsonArray.size();j++){
                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                    String skuNo = jsonObject.getString("SkuNo");
                    String actualQty = jsonObject.getString("ActualQty");
                    //log.info("erpOrder="+erpOrder+",skuNo="+skuNo+",actualQty="+actualQty);
                    JSONObject workflowRequestTableRecords = new JSONObject();
                    Integer recordOrder = 0;

                    JSONArray workflowRequestTableFieldsArr = new JSONArray();

                    JSONObject workflowRequestTableFields1 = new JSONObject();

                    workflowRequestTableFields1.put("fieldName","po");
                    workflowRequestTableFields1.put("fieldValue",erpOrder);

                    JSONObject workflowRequestTableFields2 = new JSONObject();
                    workflowRequestTableFields2.put("fieldName","sku_no");
                    workflowRequestTableFields2.put("fieldValue",skuNo);

                    JSONObject workflowRequestTableFields3 = new JSONObject();
                    workflowRequestTableFields3.put("fieldName","actual_qty");
                    workflowRequestTableFields3.put("fieldValue",actualQty);

                    JSONObject workflowRequestTableFields4 = new JSONObject();
                    workflowRequestTableFields4.put("fieldName","receive_date");
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
                detailData.put("tableDBName",dmsConfig.getRefundDtl2());

                JSONArray mainDataArr = new JSONArray();
                JSONObject mainData1 = new JSONObject();
                mainData1.put("fieldName","is_receive");
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
                    log.info("订单回传异常，数据进入中间表,requestId="+ thRefund.getRequestId()+",提交失败");
                }
            }
        }
        return null;
    }

    @Override
    public JsonResult purCancel(PurCancelDto dto) throws IOException {
        JSONObject purJson  = jsonService.createPurCancel(dto);

        String unescapedJson = StringEscapeUtils.unescapeJson(purJson.toJSONString());

        log.info("logistics_interface="+unescapedJson);

        String str = unescapedJson+sfConfig.getKey();

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
