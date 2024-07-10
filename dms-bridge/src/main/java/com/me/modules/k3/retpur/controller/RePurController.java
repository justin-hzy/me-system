package com.me.modules.k3.retpur.controller;

import com.me.modules.k3.retpur.dto.GetPutRePurReqDto;
import com.me.modules.k3.tran.service.TranService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
public class RePurController {

    private TranService tranService;

    @PostMapping("putRePur")
    public String putRePur(@RequestBody GetPutRePurReqDto dto) throws Exception {

        String resJsonStr = tranService.tranRetPur(dto);

        return resJsonStr;
    }
}
