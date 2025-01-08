package com.me.modules.sale.controller;

import com.me.common.core.JsonResult;
import com.me.modules.sale.dto.PutSaleOrderDto;
import com.me.modules.sale.dto.SaleCancelDto;
import com.me.modules.sale.service.SaleOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("sf")
@AllArgsConstructor
public class SaleOrderController {

    private SaleOrderService service;

    @PostMapping("/sale/putSaleOrder")
    public JsonResult putSaleOrder(@RequestBody PutSaleOrderDto dto) throws IOException {
        JsonResult respResult = service.putSaleOrder(dto);
        return respResult;
    }

    @PostMapping("/sale/cancel")
    public JsonResult saleCancel(@RequestBody SaleCancelDto dto) throws IOException {
        JsonResult respResult =  service.saleCancel(dto);
        return respResult;
    }

}
