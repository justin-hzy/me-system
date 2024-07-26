package com.me.order;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.me.common.config.DmsConfig;
import com.me.common.utils.DmsUtil;
import com.me.modules.fl.controller.CallBackController;
import com.me.modules.fl.dto.GetOrderCBDto;
import com.me.modules.fl.dto.GetReConOrderCBDto;
import com.me.modules.fl.entity.*;
import com.me.modules.fl.pojo.*;
import com.me.modules.fl.service.*;
import com.me.modules.k3.log.entity.DmsK3ErrorLog;
import com.me.modules.k3.log.service.DmsK3ErrorLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class DmsSubmitTest {

    @Autowired
    FlOrderFormTableMainService flOrderFormTableMainService;

    @Autowired
    FlReOrderFormTableMainService flReOrderFormTableMainService;

    @Autowired
    DmsConfig dmsConfig;

    @Autowired
    FlTrfOrderFormTableMainService trfOrderFormTableMainService;

    @Autowired
    CallBackController callBackController;

    @Autowired
    FlPurInFormTableMainService flPurInFormTableMainService;

    @Autowired
    FlRePurOrderFormTableMainService flRePurOrderFormTableMainService;

    @Autowired
    FlTransCodeReqLogService flTransCodeReqLogService;

    @Autowired
    DmsK3ErrorLogService dmsK3ErrorLogService;


    @Test
    public void orderSubmit(){

        String name = "HK20240507002";

        JSONObject jsonObject = new JSONObject();

        QueryWrapper<FlOrderFormTableMain> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("fddh",name);

        FlOrderFormTableMain FlOrderFormTableMain = flOrderFormTableMainService.getOne(queryWrapper);

        /*销售出库单*/
        String pO = FlOrderFormTableMain.getPO();

        JSONArray mainDataArr = new JSONArray();

        jsonObject.put("requestId", Integer.valueOf(FlOrderFormTableMain.getRequestid()));


        String tracking_number = "12345";

        String shipping_type = "新竹物流";

        JSONObject mainData1 = new JSONObject();
        mainData1.put("fieldName","wldh");
        mainData1.put("fieldValue",tracking_number);

        JSONObject mainData2 = new JSONObject();
        mainData2.put("fieldName","wlgs");
        mainData2.put("fieldValue",shipping_type);

        JSONObject mainData3 = new JSONObject();
        mainData3.put("fieldName","xsckdh");
        mainData3.put("fieldValue",name);

        mainDataArr.add(mainData1);
        mainDataArr.add(mainData2);
        mainDataArr.add(mainData3);

        jsonObject.put("mainData",mainDataArr);

        JSONArray detailDataArr = new JSONArray();
        JSONObject detailData = new JSONObject();

        JSONArray workflowRequestTableRecordsArr = new JSONArray();

        List<Product> products = new ArrayList<>();

        Product product = new Product();
        product.setSku("4711401210962-11111");
        product.setShipped_quantity("1");

        products.add(product);

        for(int i=0;i<products.size();i++){
            JSONObject workflowRequestTableRecords = new JSONObject();
            Integer recordOrder = 0;

            JSONArray workflowRequestTableFieldsArr = new JSONArray();

            JSONObject workflowRequestTableFields1 = new JSONObject();
            Integer quantity  = Integer.valueOf(products.get(i).getShipped_quantity());
            workflowRequestTableFields1.put("fieldName","cksl");
            workflowRequestTableFields1.put("fieldValue",quantity);

            JSONObject workflowRequestTableFields2 = new JSONObject();
            workflowRequestTableFields2.put("fieldName","xsckdh");
            workflowRequestTableFields2.put("fieldValue",name);

            JSONObject workflowRequestTableFields3 = new JSONObject();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            workflowRequestTableFields3.put("fieldName","chrq");
            workflowRequestTableFields3.put("fieldValue",todayString);

            JSONObject workflowRequestTableFields4 = new JSONObject();
            String sku = products.get(i).getSku();
            workflowRequestTableFields4.put("fieldName","hpbh");
            workflowRequestTableFields4.put("fieldValue",sku);

            workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

            workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
            workflowRequestTableRecords.put("recordOrder",recordOrder);

            workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
        }

        detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
        detailData.put("tableDBName",dmsConfig.getOrderDetailTable2());
        detailDataArr.add(detailData);

        jsonObject.put("detailData",detailDataArr);


        log.info(jsonObject.toJSONString());

        DmsUtil.testRegist("http://120.77.244.137:8080/");
        DmsUtil.testGetoken("http://120.77.244.137:8080/");
        DmsUtil.testRestful("http://120.77.244.137:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());
    }

    @Test
    public void reSubmitTest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestId","163288");

        DmsUtil.testRegist("http://120.77.244.137:8080/");
        DmsUtil.testGetoken("http://120.77.244.137:8080/");
        DmsUtil.testRestful("http://120.77.244.137:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());

    }

    @Test
    public void returnOrderSubmit(){

        JSONObject jsonObject = new JSONObject();
        //退货单号
        String title = "DMSTW04-2024050701";

        QueryWrapper<FlReOrderFormTableMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcbh",title);

        FlReOrderFormTableMain flReOrderFormTableMain =
                flReOrderFormTableMainService.getOne(queryWrapper);

        Integer requestId = flReOrderFormTableMain.getRequestid();
        log.info("requestId="+requestId);

        jsonObject.put("requestId", requestId);

        JSONArray detailDataArr = new JSONArray();
        JSONObject detailData = new JSONObject();

        JSONArray workflowRequestTableRecordsArr = new JSONArray();

        List<ReturnOrderItem> returnOrderItems = new ArrayList<>();
        ReturnOrderItem item = new ReturnOrderItem();
        item.setIdentifier("9");
        item.setReceived_pcs("1");
        item.setSku("4711401210962-11111");

        for(ReturnOrderItem returnOrderItem : returnOrderItems){

            String  received_pcs = returnOrderItem.getReceived_pcs();

            JSONObject workflowRequestTableRecords = new JSONObject();
            Integer recordOrder = 0;

            JSONArray workflowRequestTableFieldsArr = new JSONArray();

            JSONObject workflowRequestTableFields1 = new JSONObject();
            String sku = returnOrderItem.getSku();
            workflowRequestTableFields1.put("fieldName","hptxm");
            workflowRequestTableFields1.put("fieldValue",sku);

            JSONObject workflowRequestTableFields2 = new JSONObject();
            workflowRequestTableFields2.put("fieldName","rksl");
            workflowRequestTableFields2.put("fieldValue",received_pcs);

            //获取当时日期
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String todayString = today.format(formatter);
            JSONObject workflowRequestTableFields3 = new JSONObject();
            workflowRequestTableFields3.put("fieldName","rkrq");
            workflowRequestTableFields3.put("fieldValue",todayString);

            //明细id
            String identifier = returnOrderItem.getIdentifier();
            JSONObject workflowRequestTableFields4 = new JSONObject();
            workflowRequestTableFields4.put("fieldName","mxid");
            workflowRequestTableFields4.put("fieldValue",identifier);

            workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
            workflowRequestTableFieldsArr.add(workflowRequestTableFields4);

            workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
            workflowRequestTableRecords.put("recordOrder",recordOrder);

            workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
        }

        detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
        detailData.put("tableDBName",dmsConfig.getReturnOrderDetailTable2());
        detailDataArr.add(detailData);

        jsonObject.put("detailData",detailDataArr);


        log.info(jsonObject.toJSONString());


        //DmsUtil.testRestful("http://39.108.136.182:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());
        DmsUtil.testRegist("http://120.77.244.137:8080/");
        DmsUtil.testGetoken("http://120.77.244.137:8080/");
        DmsUtil.testRestful("http://120.77.244.137:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());

    }


    @Test
    public void trfOrderSubmit(){

        JSONObject jsonObject = new JSONObject();

        /*调拨单号*/
        String title = "DMSTW05-2024042902";

        QueryWrapper<FlTrfOrderFormTableMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcbh",title);
        FlTrfOrderFormTableMain trfOrderFormTableMain = trfOrderFormTableMainService.getOne(queryWrapper);

        if(trfOrderFormTableMain != null){

            String requestId = trfOrderFormTableMain.getRequestid();
            log.info("requestId="+requestId);

            jsonObject.put("requestId", requestId);

            JSONArray detailDataArr = new JSONArray();
            JSONObject detailData = new JSONObject();

            JSONArray workflowRequestTableRecordsArr = new JSONArray();

            //退货明细
            List<InnerTrfCBItem> returnOrderItems = new ArrayList<>();
            InnerTrfCBItem item = new InnerTrfCBItem();
            item.setTransferred_quantity("1");
            item.setSku("4711401210962-11111");
            returnOrderItems.add(item);


            for(InnerTrfCBItem returnOrderItem : returnOrderItems){
                String transferred_quantity = returnOrderItem.getTransferred_quantity();

                JSONObject workflowRequestTableRecords = new JSONObject();
                Integer recordOrder = 0;

                JSONArray workflowRequestTableFieldsArr = new JSONArray();

                JSONObject workflowRequestTableFields1 = new JSONObject();
                String sku = returnOrderItem.getSku();
                workflowRequestTableFields1.put("fieldName","hpbh");
                workflowRequestTableFields1.put("fieldValue",sku);


                JSONObject workflowRequestTableFields2 = new JSONObject();
                workflowRequestTableFields2.put("fieldName","cksl");
                workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                JSONObject workflowRequestTableFields3 = new JSONObject();
                workflowRequestTableFields3.put("fieldName","rksl");
                workflowRequestTableFields3.put("fieldValue",transferred_quantity);


                workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields3);

                workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                workflowRequestTableRecords.put("recordOrder",recordOrder);

                workflowRequestTableRecordsArr.add(workflowRequestTableRecords);

                detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
                detailData.put("tableDBName",dmsConfig.getTrfOrderDetailTable2());
                detailDataArr.add(detailData);

                jsonObject.put("detailData",detailDataArr);

                log.info(jsonObject.toJSONString());

                DmsUtil.testRegist("http://120.77.244.137:8080/");
                DmsUtil.testGetoken("http://120.77.244.137:8080/");
                DmsUtil.testRestful("http://120.77.244.137:8080/","/api/workflow/paService/submitRequest",jsonObject.toJSONString());
            }
        }
    }

    @Test
    public void consOrderSubmit(){
        GetOrderCBDto getOrderCBDto = new GetOrderCBDto();
        getOrderCBDto.setName("DMSTW05-2024043007");
        getOrderCBDto.setStatus("done");
        getOrderCBDto.setInternal_name("");

        Shipping shipping = new Shipping();
        shipping.setTracking_number("123");
        shipping.setShipping_type("hct");

        List<Shipping> shippings = new ArrayList<>();
        shippings.add(shipping);

        Product product = new Product();
        product.setShipped_quantity("1");
        product.setSku("4711401210962-11111");

        List<Product> products = new ArrayList<>();
        products.add(product);

        getOrderCBDto.setProducts(products);

        getOrderCBDto.setShippings(shippings);
        callBackController.getConOrderCB(getOrderCBDto);
    }

    @Test
    public void consReOrderSubmit(){
        GetReConOrderCBDto getReConOrderCBDto = new GetReConOrderCBDto();
        getReConOrderCBDto.setTitle("DMSTW05-2024043008");
        getReConOrderCBDto.setStatus("done");

        List<ReConsOrderItem> items = new ArrayList<>();

        ReConsOrderItem reConsOrderItem = new ReConsOrderItem();
        reConsOrderItem.setSku("4711401210962-11111");
        reConsOrderItem.setReceived_pcs("1");

        items.add(reConsOrderItem);
        getReConOrderCBDto.setItems(items);

        callBackController.getReConOrderCB(getReConOrderCBDto);
    }

    @Test
    public void getPurInSubmit(){
        //进仓单
        String title = "DMSTW07-2024051701";

        QueryWrapper<FlPurInFormTableMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcbh",title);
        FlPurInFormTableMain purInFormTableMain = flPurInFormTableMainService.getOne(queryWrapper);
        //
        JSONObject jsonObject = new JSONObject();
        if(purInFormTableMain != null){
            String requestId = purInFormTableMain.getRequestId();
            log.info("requestId="+requestId);
            jsonObject.put("requestId",requestId);

            JSONArray detailDataArr = new JSONArray();
            JSONObject detailData = new JSONObject();

            JSONArray workflowRequestTableRecordsArr = new JSONArray();

            //采购入库明细
            List<PurchaseInItem> purchaseInItems  = new ArrayList<>();

            PurchaseInItem item = new PurchaseInItem();
            item.setReceived_pcs("2");
            item.setSku("4711401210962-11111");
            purchaseInItems.add(item);

            for (PurchaseInItem purchaseInItem : purchaseInItems){

                String received_pcs = purchaseInItem.getReceived_pcs();

                JSONObject workflowRequestTableRecords = new JSONObject();
                Integer recordOrder = 0;

                JSONArray workflowRequestTableFieldsArr = new JSONArray();

                JSONObject workflowRequestTableFields1 = new JSONObject();
                String sku = purchaseInItem.getSku();
                workflowRequestTableFields1.put("fieldName","wlbm");
                workflowRequestTableFields1.put("fieldValue",sku);

                JSONObject workflowRequestTableFields2 = new JSONObject();
                workflowRequestTableFields2.put("fieldName","rksl");
                workflowRequestTableFields2.put("fieldValue",received_pcs);

                //获取当时日期
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String todayString = today.format(formatter);
                JSONObject workflowRequestTableFields3 = new JSONObject();
                log.info("todayString="+todayString);
                workflowRequestTableFields3.put("fieldName","rkrq");
                workflowRequestTableFields3.put("fieldValue",todayString);

                workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields3);

                workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                workflowRequestTableRecords.put("recordOrder",recordOrder);

                workflowRequestTableRecordsArr.add(workflowRequestTableRecords);

            }

            detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
            detailData.put("tableDBName",dmsConfig.getPurInDetailTable2());
            detailDataArr.add(detailData);

            jsonObject.put("detailData",detailDataArr);

            log.info(jsonObject.toJSONString());

            DmsUtil.testRegist(dmsConfig.getIp());
            DmsUtil.testGetoken(dmsConfig.getIp());
            DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
        }
    }

    @Test
    public void rePurSubmit(){
        /*采购退单*/
        String title = "DMSTW08-2024051606";

        //物流单号
        String trackingNumber = "12345";

        //物流公司
        String shippingType = "hct";


        QueryWrapper<FlRePurOrderFormTableMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lcbh",title);
        FlRePurOrderFormTableMain flRePurOrderFormTableMain = flRePurOrderFormTableMainService.getOne(queryWrapper);

        JSONObject jsonObject = new JSONObject();

        if(flRePurOrderFormTableMain != null){
            String requestId = flRePurOrderFormTableMain.getRequestId();
            log.info("requestId="+requestId);

            jsonObject.put("requestId", requestId);

            JSONArray detailDataArr = new JSONArray();
            JSONObject detailData = new JSONObject();

            JSONArray workflowRequestTableRecordsArr = new JSONArray();

            //采购入库明细
            List<Product> products  = new ArrayList<>();

            Product product1 = new Product();
            product1.setSku("4711401210962-11111");
            product1.setShipped_quantity("1");

            products.add(product1);

            for(Product product : products){

                String transferred_quantity = product.getShipped_quantity();

                JSONObject workflowRequestTableRecords = new JSONObject();
                Integer recordOrder = 0;

                JSONArray workflowRequestTableFieldsArr = new JSONArray();

                JSONObject workflowRequestTableFields1 = new JSONObject();
                String sku = product.getSku();
                workflowRequestTableFields1.put("fieldName","wlbm");
                workflowRequestTableFields1.put("fieldValue",sku);

                JSONObject workflowRequestTableFields2 = new JSONObject();
                workflowRequestTableFields2.put("fieldName","cksl");
                workflowRequestTableFields2.put("fieldValue",transferred_quantity);

                //获取当时日期
                LocalDate today = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String todayString = today.format(formatter);
                JSONObject workflowRequestTableFields3 = new JSONObject();
                log.info("todayString="+todayString);
                workflowRequestTableFields3.put("fieldName","ckrq");
                workflowRequestTableFields3.put("fieldValue",todayString);

                JSONObject workflowRequestTableFields4 = new JSONObject();
                workflowRequestTableFields4.put("fieldName","wldh");
                workflowRequestTableFields4.put("fieldValue",trackingNumber);


                JSONObject workflowRequestTableFields5 = new JSONObject();
                workflowRequestTableFields5.put("fieldName","wlgs");
                workflowRequestTableFields5.put("fieldValue",shippingType);

                workflowRequestTableFieldsArr.add(workflowRequestTableFields1);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields2);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields3);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields4);
                workflowRequestTableFieldsArr.add(workflowRequestTableFields5);

                workflowRequestTableRecords.put("workflowRequestTableFields",workflowRequestTableFieldsArr);
                workflowRequestTableRecords.put("recordOrder",recordOrder);

                workflowRequestTableRecordsArr.add(workflowRequestTableRecords);
            }

            detailData.put("workflowRequestTableRecords",workflowRequestTableRecordsArr);
            detailData.put("tableDBName",dmsConfig.getRePurDtlTable2());
            detailDataArr.add(detailData);

            jsonObject.put("detailData",detailDataArr);

            log.info(jsonObject.toJSONString());
            DmsUtil.testRegist(dmsConfig.getIp());
            DmsUtil.testGetoken(dmsConfig.getIp());
            DmsUtil.testRestful(dmsConfig.getIp(),dmsConfig.getUrl(),jsonObject.toJSONString());
        }

    }

    @Test
    public void test(){
        QueryWrapper<DmsK3ErrorLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("billNo","TW_DMSTW09-2024070423");
        DmsK3ErrorLog errorLog = dmsK3ErrorLogService.getOne(queryWrapper);

        String message = errorLog.getMessage();

        JSONObject jsonObject = JSONObject.parseObject(message);

        JSONObject detail = jsonObject.getJSONObject("Detail");


        JSONArray jsonArray = detail.getJSONArray("Entry");
        log.info(jsonArray.size()+"");

        //log.info(jsonArray.toJSONString());

        Integer SUM = 0;

        for (int i = 0 ;i<jsonArray.size();i++){
            String MaterialNumber = jsonArray.getJSONObject(i).getString("MaterialNumber");

            String BaseQty = jsonArray.getJSONObject(i).getString("BaseQty");

            String Qty = jsonArray.getJSONObject(i).getString("Qty");

            String StockName = jsonArray.getJSONObject(i).getString("StockName");

            log.info("MaterialNumber="+MaterialNumber+",BaseQty="+BaseQty+",Qty="+Qty+",StockName="+StockName);

            SUM = SUM + Integer.valueOf(BaseQty);

        }
        log.info("SUM="+SUM);
    }
}
