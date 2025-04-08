package com.me.modules.order.out.controller;

import com.alibaba.fastjson.JSONObject;
import com.me.common.core.JsonResult;
import com.me.modules.order.out.dto.PutOutOrderDto;
import com.me.modules.order.out.service.OutOrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("flash")
@AllArgsConstructor
public class FlashOrderController {

    private OutOrderService outOrderService;

    @PostMapping("/order/putFlashOutOrder")
    public JsonResult putFlashOrder(@RequestBody PutOutOrderDto dto) throws IOException {
        JSONObject respJsonResult = outOrderService.putFlashOutOrder(dto);

        return JsonResult.ok(respJsonResult);
    }

}
