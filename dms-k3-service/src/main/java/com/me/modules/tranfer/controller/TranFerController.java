package com.me.modules.tranfer.controller;

import com.me.modules.tran.service.TranService;
import com.me.modules.tranfer.dto.PutTrfReqDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
public class TranFerController {

    private TranService  tranService;

    @PostMapping("putHkTrf")
    public String putHkTrfUrl(@RequestBody PutTrfReqDto dto) throws Exception {
        String resJsonStr = tranService.putHkTrfUrl(dto);
        return resJsonStr;
    }

    @PostMapping("putTwTrf")
    public String putTwTrfUrl(@RequestBody PutTrfReqDto dto) throws Exception {
        String resJsonStr = tranService.putTwTrfUrl(dto);
        return resJsonStr;
    }
}
