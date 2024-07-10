package com.me.wangdiantong.stockout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.me.modules.wangdianstockout.entity.WangDianStockOut;
import com.me.modules.wangdianstockout.entity.WangDianStockOutList;
import com.me.modules.wangdianstockout.service.WangDianStockOutListService;
import com.me.modules.wangdianstockout.service.WangDianStockOutService;
import com.wangdian.api.WdtClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class StockOutTest {

    @Autowired
    private WangDianStockOutService wangDianStockOutService;

    @Autowired
    private WangDianStockOutListService wangDianStockOutListService;

    @Test
    //@Transactional(rollbackFor = Exception.class)
    public void getWangDianTongStockOut(){
        String sid = "wmt2";
        String appKey = "wmt2-ot";
        String appSecret = "1e167eecd6a41a771632a1ad1611f40c";
        String baseUrl = "https://api.wangdian.cn/openapi2";
        WdtClient client = new WdtClient(sid, appKey, appSecret, baseUrl);
        Map<String, String> params = new HashMap<String, String>();
        params.put("start_time", "2023-12-01 00:00:00");
        params.put("end_time", "2023-12-30 23:59:59");
        try {
            String response = client.execute("stockout_order_query_trade.php", params);
            //解析 response
            JSONObject dataJsonObject = JSON.parseObject(response);
            JSONArray stockOutArray = dataJsonObject.getJSONArray("stockout_list");
            List<WangDianStockOut> wangDianStockOutList = new ArrayList<>();
            List<WangDianStockOutList> wangDianStockOutDetailList = new ArrayList<>();
            for(int i=0;i<stockOutArray.size();i++){
                JSONObject stockOut = stockOutArray.getJSONObject(i);
                WangDianStockOut wangDianStockOut = new WangDianStockOut();
                wangDianStockOut.setIsAllocated(stockOut.getString("is_allocated"));
                wangDianStockOut.setReceiverDistrictCode(stockOut.getString("receiver_district_code"));
                wangDianStockOut.setFenxiaoNick(stockOut.getString("fenxiao_nick"));
                wangDianStockOut.setPostAmount(stockOut.getString("post_amount"));
                wangDianStockOut.setErrorInfo(stockOut.getString("error_info"));
                wangDianStockOut.setReceiverRing(stockOut.getString("receiver_ring"));
                wangDianStockOut.setTradeTime(stockOut.getString("trade_time"));
                wangDianStockOut.setBADREASON(stockOut.getString("bad_reason"));
                wangDianStockOut.setDISCOUNT(stockOut.getString("discount"));
                wangDianStockOut.setPackageId(stockOut.getString("package_id"));
                wangDianStockOut.setPRINTBATCHNO(stockOut.getString("print_batch_no"));
                wangDianStockOut.setRawGoodsCount(stockOut.getString("raw_goods_count"));
                wangDianStockOut.setPackScore(stockOut.getString("pack_score"));
                wangDianStockOut.setPickerName(stockOut.getString("picker_name"));
                wangDianStockOut.setTradeId(stockOut.getString("trade_id"));
                wangDianStockOut.setStockOutReceiverDTB(stockOut.getString("stockout_receiver_dtb"));
                wangDianStockOut.setPostWeight(stockOut.getString("post_weight"));
                wangDianStockOut.setReceiverCountry(stockOut.getString("receiver_country"));
                wangDianStockOut.setCheckerName(stockOut.getString("checker_name"));
                wangDianStockOut.setWATCHERID(stockOut.getString("watcher_id"));
                wangDianStockOut.setRefundStatus(stockOut.getString("refund_status"));
                wangDianStockOut.setBuyerMessage(stockOut.getString("buyer_message"));
                wangDianStockOut.setReceiverProvince(stockOut.getString("receiver_province"));
                wangDianStockOut.setLogisticsCode(stockOut.getString("logistics_code"));
                wangDianStockOut.setINVOICEPRINTSTATUS(stockOut.getString("invoice_print_status"));
                wangDianStockOut.setPackageCost(stockOut.getString("package_cost"));
                wangDianStockOut.setSRCTIDS(stockOut.getString("src_tids"));
                wangDianStockOut.setMd5Str(stockOut.getString("md5_str"));
                wangDianStockOut.setWarehouseName(stockOut.getString("warehouse_name"));
                wangDianStockOut.setNickname(stockOut.getString("nick_name"));
                wangDianStockOut.setReceiverProvinceCode(stockOut.getString("receiver_province_code"));
                wangDianStockOut.setIdCardType(stockOut.getString("id_card_type"));
                wangDianStockOut.setPackageFee(stockOut.getString("package_fee"));
                wangDianStockOut.setStatus(stockOut.getString("status"));
                wangDianStockOut.setFLAGID(stockOut.getString("flag_id"));
                wangDianStockOut.setFreezeReason(stockOut.getString("freeze_reason"));
                wangDianStockOut.setSrcTradeNo(stockOut.getString("src_trade_no"));
                wangDianStockOut.setPostFee(stockOut.getString("post_fee"));
                wangDianStockOut.setCustomType(stockOut.getString("custom_type"));
                wangDianStockOut.setGoodsCount(stockOut.getString("goods_count"));
                wangDianStockOut.setStockOutId(stockOut.getString("stockout_id"));
                wangDianStockOut.setSrcOrderNo(stockOut.getString("src_order_no"));
                wangDianStockOut.setLogisticsTemplateId(stockOut.getString("logistics_template_id"));
                wangDianStockOut.setInvoiceContent(stockOut.getString("invoice_content"));
                wangDianStockOut.setPostCost(stockOut.getString("post_cost"));
                wangDianStockOut.setReceiverName(stockOut.getString("receiver_name"));
                wangDianStockOut.setCURRENCY(stockOut.getString("currency"));
                wangDianStockOut.setSENDBILLTEMPLATEID(stockOut.getString("sendbill_template_id"));
                wangDianStockOut.setExaminerName(stockOut.getString("examiner_name"));
                wangDianStockOut.setPICKLISTNO(stockOut.getString("picklist_no"));
                wangDianStockOut.setLogisticsType(stockOut.getString("logistics_type"));
                wangDianStockOut.setNOTECOUNT(stockOut.getString("note_count"));
                wangDianStockOut.setPickErrorCount(stockOut.getString("pick_error_count"));
                wangDianStockOut.setDeliveryTerm(stockOut.getString("delivery_term"));
                wangDianStockOut.setLogisticsNo(stockOut.getString("logistics_no"));
                wangDianStockOut.setReceiverDistrict(stockOut.getString("receiver_district"));
                wangDianStockOut.setSrcOrderType(stockOut.getString("src_order_type"));
                wangDianStockOut.setHasInvoice(stockOut.getString("has_invoice"));
                wangDianStockOut.setGoodsTotalAmount(stockOut.getString("goods_total_amount"));
                wangDianStockOut.setReceivable(stockOut.getString("receivable"));
                wangDianStockOut.setReceiverMobile(stockOut.getString("receiver_mobile"));
                wangDianStockOut.setSTOCKCHECKTIME(stockOut.getString("receiver_mobile"));
                wangDianStockOut.setSTOCKCHECKTIME(stockOut.getString("stock_check_time"));
                wangDianStockOut.setOPERATORNAME(stockOut.getString("operator_name"));
                wangDianStockOut.setPackagerId(stockOut.getString("packager_id"));
                wangDianStockOut.setCsRemark(stockOut.getString("cs_remark"));
                wangDianStockOut.setPICKLISTPRINTSTATUS(stockOut.getString("picklist_print_status"));
                wangDianStockOut.setEBillStatus(stockOut.getString("ebill_status"));
                wangDianStockOut.setReceiverCityCode(stockOut.getString("receiver_city_code"));
                wangDianStockOut.setReceiverAddress(stockOut.getString("receiver_address"));
                wangDianStockOut.setCustomerName(stockOut.getString("customer_name"));
                wangDianStockOut.setPrinterName(stockOut.getString("printer_name"));
                wangDianStockOut.setCustomerId(stockOut.getString("customer_id"));
                wangDianStockOut.setWarehouseId(stockOut.getString("warehouse_id"));

                wangDianStockOutList.add(wangDianStockOut);
                //log.info(wangDianStockOut.toString());



                JSONArray detailsArray = stockOut.getJSONArray("details_list");

                for(int j=0;j<detailsArray.size();j++){
                    JSONObject details = detailsArray.getJSONObject(j);
                    WangDianStockOutList wangDianStockOutDetail = new WangDianStockOutList();
                    wangDianStockOutDetail.setSpecCode(details.getString("spec_code"));
                    wangDianStockOutDetail.setBatchNo(details.getString("brand_no"));
                    wangDianStockOutDetail.setSellPrice(details.getString("sell_price"));
                    wangDianStockOutDetail.setNum(details.getString("num"));
                    wangDianStockOutDetail.setIsZeroCost(details.getString("is_zero_cost"));
                    wangDianStockOutDetail.setDiscount(details.getString("discount"));
                    wangDianStockOutDetail.setScanType(details.getString("scan_type"));
                    wangDianStockOutDetail.setSpecNo(details.getString("spec_no"));
                    wangDianStockOutDetail.setTaxRate(details.getString("tax_rate"));
                    wangDianStockOutDetail.setStockOutOrderDetailId(details.getString("stockout_order_detail_id"));
                    wangDianStockOutDetail.setPrice(details.getString("price"));
                    wangDianStockOutDetail.setModified(details.getString("modified"));
                    wangDianStockOutDetail.setBarcode(details.getString("barcode"));
                    wangDianStockOutDetail.setGoodsName(details.getString("goods_name"));
                    wangDianStockOutDetail.setSrcTid(details.getString("src_tid"));
                    wangDianStockOutDetail.setRefundStatus(details.getString("refund_status"));
                    wangDianStockOutDetail.setCreated(details.getString("created"));
                    wangDianStockOutDetail.setUnitRatio(details.getString("unit_ratio"));
                    wangDianStockOutDetail.setGoodsId(details.getString("goods_id"));
                    wangDianStockOutDetail.setWeight(details.getString("weight"));
                    wangDianStockOutDetail.setBrandName(details.getString("brand_name"));
                    wangDianStockOutDetail.setFromMask(details.getString("from_mask"));
                    wangDianStockOutDetail.setUnitName(details.getString("unit_name"));
                    wangDianStockOutDetail.setIsExamined(details.getString("is_examined"));
                    wangDianStockOutDetail.setSaleOrderId(details.getString("sale_order_id"));
                    wangDianStockOutDetail.setTotalAmount(details.getString("total_amount"));
                    wangDianStockOutDetail.setBatchRemark(details.getString("batch_remark"));
                    wangDianStockOutDetail.setMarketPrice(details.getString("market_price"));
                    wangDianStockOutDetail.setShareAmount(details.getString("share_amount"));
                    wangDianStockOutDetail.setPositionId(details.getString("position_id"));
                    wangDianStockOutDetail.setSrcOrderDetailId(details.getString("src_order_detail_id"));
                    wangDianStockOutDetail.setBatchId(details.getString("batch_id"));
                    wangDianStockOutDetail.setGoodsNo(details.getString("goods_no"));
                    wangDianStockOutDetail.setRemark(details.getString("remark"));
                    wangDianStockOutDetail.setGoodsCount(details.getString("goods_count"));
                    wangDianStockOutDetail.setStockoutId(details.getString("stockout_id"));
                    wangDianStockOutDetail.setSpecId(details.getString("spec_id"));
                    wangDianStockOutDetail.setUnitId(details.getString("unit_id"));
                    wangDianStockOutDetail.setCostPrice(details.getString("cost_price"));
                    wangDianStockOutDetail.setNum2(details.getString("num2"));
                    wangDianStockOutDetail.setBrandNo(details.getString("batch_no"));
                    wangDianStockOutDetail.setIsPackage(details.getString("is_package"));
                    wangDianStockOutDetail.setSrcOrderType(details.getString("src_order_type"));
                    wangDianStockOutDetail.setGoodProp3(details.getString("good_prop3"));
                    wangDianStockOutDetail.setGoodProp4(details.getString("good_prop4"));
                    wangDianStockOutDetail.setBaseUnitId(details.getString("base_unit_id"));
                    wangDianStockOutDetail.setGiftType(details.getString("gift_type"));
                    wangDianStockOutDetail.setGoodProp1(details.getString("good_prop1"));
                    wangDianStockOutDetail.setGoodProp2(details.getString("good_prop2"));
                    wangDianStockOutDetail.setRecId(details.getString("rec_id"));
                    wangDianStockOutDetail.setSharePost(details.getString("share_post"));
                    wangDianStockOutDetail.setProp5(details.getString("prop5"));
                    wangDianStockOutDetail.setProp4(details.getString("prop4"));
                    wangDianStockOutDetail.setProp3(details.getString("prop3"));
                    wangDianStockOutDetail.setProp2(details.getString("prop2"));
                    wangDianStockOutDetail.setProp1(details.getString("prop1"));
                    wangDianStockOutDetail.setExpireDate(details.getString("expire_date"));
                    wangDianStockOutDetail.setGoodProp5(details.getString("good_prop5"));
                    wangDianStockOutDetail.setGoodProp6(details.getString("good_prop6"));
                    wangDianStockOutDetail.setSuiteNo(details.getString("suite_no"));
                    wangDianStockOutDetail.setPaid(details.getString("paid"));
                    wangDianStockOutDetail.setPlatformId(details.getString("platform_id"));
                    wangDianStockOutDetail.setSrcOid(details.getString("src_oid"));
                    wangDianStockOutDetail.setGoodsType(details.getString("goods_type"));
                    wangDianStockOutDetail.setSpecName(details.getString("spec_name"));
                    wangDianStockOutDetailList.add(wangDianStockOutDetail);
                    //log.info(details.toJSONString());
                }
            }
            wangDianStockOutService.saveBatch(wangDianStockOutList);
            wangDianStockOutListService.saveBatch(wangDianStockOutDetailList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
