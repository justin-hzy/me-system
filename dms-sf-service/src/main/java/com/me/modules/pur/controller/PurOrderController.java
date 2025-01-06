package com.me.modules.pur.controller;

import com.me.common.core.JsonResult;
import com.me.modules.pur.dto.PurCancelDto;
import com.me.modules.pur.dto.PutRefundPurDto;
import com.me.modules.pur.service.PurOrderService;
import com.me.modules.sale.dto.PutSaleOrderDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("sf")
@AllArgsConstructor
public class PurOrderController {

    private PurOrderService purOrderService;

    @PostMapping("/refund/putRefundPur")
    private JsonResult putRefundPur(@RequestBody PutRefundPurDto dto) throws IOException {

        purOrderService.putPurOrder(dto);

        return JsonResult.ok();
    }


    @PostMapping("/pur/cancel")
    private JsonResult purCancel(@RequestBody PurCancelDto dto) throws IOException {
        JsonResult respResult = purOrderService.purCancel(dto);
        return respResult;
    }

}
