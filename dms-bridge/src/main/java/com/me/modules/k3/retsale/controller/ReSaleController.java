package com.me.modules.k3.retsale.controller;

import com.me.modules.k3.retsale.dto.PutReSaleReqDto;
import com.me.modules.k3.tran.service.TranService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
public class ReSaleController {

    TranService tranService;

    @PostMapping("putReSale")
    public String putReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.tranSaleReOrder(dto);
        return resJsonStr;
    }
}
