package com.me.modules.resale.controller;

import com.me.modules.resale.dto.PutReSaleReqDto;
import com.me.modules.tran.service.TranService;
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

    @PostMapping("putGyjHkReSale")
    public String putReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.putHkReSaleOrder(dto);
        return resJsonStr;
    }

    @PostMapping("putGyjReSale")
    public String putGyjReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.putTwReSaleOrder(dto);
        return resJsonStr;
    }

    @PostMapping("putGyjTwReSale")
    public String putGyjTwReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.putTwReSaleOrder(dto);
        return resJsonStr;
    }

    @PostMapping("putTwReSale")
    public String putTwReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.putTwReSaleOrder(dto);
        return resJsonStr;
    }

    @PostMapping("putHkReSale")
    public String putHkReSale(@RequestBody PutReSaleReqDto dto) throws Exception {
        String resJsonStr = tranService.putHkReSaleOrder(dto);
        return resJsonStr;
    }
}
