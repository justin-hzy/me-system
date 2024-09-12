package com.me.modules.purchase.controller;

import com.me.modules.purchase.dto.PutPurReqDto;
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
@Slf4j
@AllArgsConstructor
public class PurController {

    private TranService tranService;

    @PostMapping("putPur")
    public String putPur(@RequestBody PutPurReqDto dto) throws Exception {

        log.info("dto="+dto.toString());
        String resJsonStr = tranService.tranPurchase(dto);
        return resJsonStr;
    }

}
