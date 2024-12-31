package com.me.modules.sale.in.controller;

import com.me.common.core.JsonResult;
import com.me.modules.sale.in.dto.PutInOrderDto;
import com.me.modules.sale.in.service.InOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("flash")
@AllArgsConstructor
public class FlashInOrderController {

    private InOrderService inOrderService;

    @PostMapping("/order/putFlashInOrder")
    public JsonResult putFlashInOrder(@RequestBody PutInOrderDto dto) throws IOException {

        inOrderService.putFlashInOrder(dto);

        return JsonResult.ok();
    }
}
