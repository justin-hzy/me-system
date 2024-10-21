package com.me.modules.sale.controller;

import com.alibaba.fastjson.JSONObject;
import com.me.modules.sale.dto.PutSaleReqDto;
import com.me.modules.tran.service.TranService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
@Slf4j
public class SaleController {

    private TranService tranService;

    @PostMapping("putSale")
    public String putSale(@RequestBody PutSaleReqDto dto) throws Exception {


        //log.info(dto.toString());
        String resJsonStr = tranService.tranSaleOrder(dto);


        return resJsonStr;
    }

    @PostMapping("putTWSale")
    public String putTWSale(@RequestBody PutSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.tranTWSaleOrder(dto);
        return resJsonStr;
    }
}
