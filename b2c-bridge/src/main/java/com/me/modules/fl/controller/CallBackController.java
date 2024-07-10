package com.me.modules.fl.controller;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.fl.dto.GetOrderCBDto;
import com.me.modules.fl.dto.GetReturnOrderCBDto;
import com.me.modules.fl.pojo.Product;
import com.me.modules.fl.pojo.Shipping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("fl")
@Slf4j
public class CallBackController {

    @PostMapping("getOrderCallBack")
    public String getOrderCB(@RequestBody GetOrderCBDto getOrderCBDto){

        log.info("reqDto="+getOrderCBDto.toString());

        /*富仑单号(订单号)*/
        String name = getOrderCBDto.getName();

        /*物流信息*/
        List<Shipping> shippings = getOrderCBDto.getShippings();

        /*明细*/
        List<Product> products = getOrderCBDto.getProducts();

        /*订单类型*/
        String status = getOrderCBDto.getStatus();

        //to do
        String internal_name = getOrderCBDto.getInternal_name();

        JSONObject jsonObject = new JSONObject();

        Shipping shipping = shippings.get(0);

        if("done".equals(status)){
            /*物流号*/
            String tracking_number = shipping.getTracking_number();
            /*物流类新*/
            String shipping_type = shipping.getShipping_type();
            if("hct".equals(shipping_type)){
                shipping_type = "新竹物流";
            }else if ("tcawt".equals(shipping_type)){
                shipping_type = "黑貓物流";
            }else if ("dpex".equals(shipping_type)){
                shipping_type = "迪比翼";
            }else if ("ecpay-seven-cod".equals(shipping_type)){
                shipping_type = "綠界711-超取代收";
            }else if ("ecpay-seven-prepaid".equals(shipping_type)){
                shipping_type = "綠界711-超取已付款";
            }else {
                shipping_type = "匹配物流类型失败";
            }

            //todo 调用易仓接口
        }
        return "success";
    }


    @PostMapping("getReturnOrdersCB")
    public String getReturnOrdersCB(@RequestBody GetReturnOrderCBDto reqDto){

        log.info("reqDto="+reqDto.toString());

        //退货单号
        String title = reqDto.getTitle();

        //状态
        String status = reqDto.getStatus();

        JSONObject jsonObject = new JSONObject();

        if("done".equals(status)){

        }else {
            log.info("当前状态不是完成状态，暂不执行提交");
        }

        return "success";
    }
}
