package com.me.modules.order.in.controller;

import com.me.common.core.JsonResult;
import com.me.modules.order.in.service.InOrderService;
import com.me.modules.order.in.dto.PutInOrderDto;
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

        JsonResult jsonResult = inOrderService.putFlashInOrder(dto);

        return jsonResult;
    }
}
