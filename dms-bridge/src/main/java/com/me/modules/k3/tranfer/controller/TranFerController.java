package com.me.modules.k3.tranfer.controller;

import com.me.modules.k3.tran.service.TranService;
import com.me.modules.k3.tranfer.dto.PutTrfReqDto;
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

    @PostMapping("putTrf")
    public String putTrf(@RequestBody PutTrfReqDto dto) throws Exception {
        String resJsonStr = tranService.tranTrf(dto);
        return resJsonStr;
    }
}
