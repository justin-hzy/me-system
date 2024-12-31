package com.me.modules.refund.controller;

import com.me.common.core.JsonResult;
import com.me.modules.refund.dto.PutRefundDto;
import com.me.modules.refund.service.RefundService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("flash")
@AllArgsConstructor
public class RefundController {

    private RefundService refundService;

    @PostMapping("/order/putFlashRefund")
    public JsonResult putFlashRefund(@RequestBody PutRefundDto dto) throws IOException {
        JsonResult respJsonResult = refundService.putFlashRefund(dto);
        return respJsonResult;
    }
}
