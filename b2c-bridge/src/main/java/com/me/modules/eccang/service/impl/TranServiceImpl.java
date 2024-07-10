package com.me.modules.eccang.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.me.common.utils.EcHttpUtil;
import com.me.modules.eccang.entity.EcReOrder;
import com.me.modules.eccang.entity.ReOrderCollArr;
import com.me.modules.eccang.service.EcReOrderService;
import com.me.modules.eccang.service.ReOrderCollArrService;
import com.me.modules.eccang.service.TranService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class TranServiceImpl implements TranService {



    private EcReOrderService ecReOrderService;


    private ReOrderCollArrService reOrderCollArrService;

    @Override
    public JSONObject tranEcReOrder(Integer page,Integer pageSize) {

        JSONObject apiResp = new JSONObject();

        String service = "rmaReturnList";
        JSONObject reqJsonObj = new JSONObject();

        reqJsonObj.put("page",page);

        reqJsonObj.put("pageSize",pageSize);
        reqJsonObj.put("createDateFrom","2024-04-15 00:00:00");
        reqJsonObj.put("createDateEnd","2024-04-15 23:59:59");


        String params = reqJsonObj.toJSONString();

        JSONObject result = EcHttpUtil.soapRequest(service, "eb", params);
        log.info("result="+result.toJSONString());

        apiResp.put("total",result.getString("total"));


        if(result != null){
            String code = result.getString("code");
            String message = result.getString("message");
            if("200".equals(code) && "Success.".equals(message)){
                JSONArray array = result.getJSONArray("data");

                List<EcReOrder> ecReOrders = new ArrayList<>();
                List<ReOrderCollArr> reOrderCollArrs = new ArrayList<>();

                for(int i=0;i<array.size();i++){
                    JSONObject jsonObject = array.getJSONObject(i);

                    QueryWrapper<EcReOrder> ecReOrderQuery = new QueryWrapper<>();
                    String roCode = jsonObject.getString("ro_code");
                    ecReOrderQuery.eq("ro_code",roCode);
                    EcReOrder ExistEcReOrder = ecReOrderService.getOne(ecReOrderQuery);
                    if(ExistEcReOrder == null){
                        EcReOrder ecReOrder = new EcReOrder();
                        ecReOrder.setSellerAccount(jsonObject.getString("seller_account"));
                        ecReOrder.setBuyerId(jsonObject.getString("buyer_id"));
                        ecReOrder.setBuyerName(jsonObject.getString("buyer_name"));
                        ecReOrder.setEmail(jsonObject.getString("email"));
                        ecReOrder.setOrderId(jsonObject.getString("order_id"));
                        ecReOrder.setOrderStatus(jsonObject.getString("order_status"));
                        ecReOrder.setRefrenceNoWarehouse(jsonObject.getString("refrence_no_warehouse"));
                        ecReOrder.setRoCode(roCode);
                        ecReOrder.setRoStatus(jsonObject.getString("ro_status"));
                        ecReOrder.setProductSku(jsonObject.getString("product_sku"));
                        ecReOrder.setProductTitle(jsonObject.getString("product_title"));
                        ecReOrder.setQty(jsonObject.getString("qty"));
                        ecReOrder.setExceptionTitle(jsonObject.getString("exception_title"));
                        ecReOrder.setProcessType(jsonObject.getString("process_type"));
                        ecReOrder.setTrackingNo(jsonObject.getString("tracking_no"));
                        ecReOrder.setOutWarehouse(jsonObject.getString("out_warehouse"));
                        ecReOrder.setInWarehouse(jsonObject.getString("in_warehouse"));
                        ecReOrder.setLocationCode(jsonObject.getString("location_code"));
                        ecReOrder.setCreateDate(jsonObject.getString("create_date"));
                        ecReOrder.setCompleteDate(jsonObject.getString("complete_date"));
                        ecReOrder.setConfirmDate(jsonObject.getString("confirm_date"));
                        ecReOrder.setCreateUser(jsonObject.getString("create_user"));
                        ecReOrder.setNote(jsonObject.getString("note"));
                        ecReOrder.setReason(jsonObject.getString("reason"));
                        ecReOrder.setAmountTotal(jsonObject.getString("amount_total"));
                        ecReOrder.setCurrency(jsonObject.getString("currency"));
                        ecReOrder.setRoType(jsonObject.getString("ro_type"));
                        ecReOrder.setStatus("80");

                        LocalDateTime today = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String todayTimeString = today.format(formatter);

                        ecReOrder.setCreateTime(todayTimeString);
                        ecReOrder.setIsDelete("0");



                        ecReOrders.add(ecReOrder);

                        JSONArray reOrderCollAddr = jsonObject.getJSONArray("return_order_collector_address");

                        if(reOrderCollAddr != null){
                            if(reOrderCollAddr.size()>0){
                                for(int j = 0;j<reOrderCollAddr.size();j++){
                                    JSONObject reOrderCollJsonObj = reOrderCollAddr.getJSONObject(j);

                                    String buyerId1 = reOrderCollJsonObj.getString("buyer_id");
                                    String refrenceNoPlatform = reOrderCollJsonObj.getString("refrence_no_platform");
                                    String name = reOrderCollJsonObj.getString("Name");
                                    String phone = reOrderCollJsonObj.getString("Phone");
                                    String companyName = reOrderCollJsonObj.getString("Company_name");
                                    String country = reOrderCollJsonObj.getString("Country");
                                    String stateOrProvince = reOrderCollJsonObj.getString("StateOrProvince");
                                    String street1 = reOrderCollJsonObj.getString("Street1");
                                    String street2 = reOrderCollJsonObj.getString("Street2");
                                    String cityName = reOrderCollJsonObj.getString("CityName");
                                    String district = reOrderCollJsonObj.getString("District");
                                    String postalCode = reOrderCollJsonObj.getString("PostalCode");
                                    String doorplate = reOrderCollJsonObj.getString("doorplate");

                                    ReOrderCollArr reOrderCollArr = new ReOrderCollArr();
                                    reOrderCollArr.setBuyerId(buyerId1);
                                    reOrderCollArr.setRefrenceNoPlatform(refrenceNoPlatform);
                                    reOrderCollArr.setName(name);
                                    reOrderCollArr.setPhone(phone);
                                    reOrderCollArr.setCompanyName(companyName);
                                    reOrderCollArr.setCountry(country);
                                    reOrderCollArr.setStateOrProvince(stateOrProvince);
                                    reOrderCollArr.setStreet1(street1);
                                    reOrderCollArr.setStreet2(street2);
                                    reOrderCollArr.setCityName(cityName);
                                    reOrderCollArr.setDistrict(district);
                                    reOrderCollArr.setPostalCode(postalCode);
                                    reOrderCollArr.setDoorplate(doorplate);

                                    reOrderCollArrs.add(reOrderCollArr);
                                }
                            }
                        }
                    }
                }

                if(ecReOrders.size()>0){
                    ecReOrderService.saveBatch(ecReOrders);
                }
                if(reOrderCollArrs.size()>0){
                    reOrderCollArrService.saveBatch(reOrderCollArrs);
                }
            }else {
                log.info("获取退单失败");
            }
        }
        return apiResp;
    }
}
