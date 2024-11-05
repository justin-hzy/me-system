package com.me.modules.retpur.controller;

import com.me.modules.retpur.dto.GetPutRePurReqDto;
import com.me.modules.tran.service.TranService;
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

        String resJsonStr = tranService.tranHkRePur(dto);

        return resJsonStr;
    }

    @PostMapping("putGyjRePur")
    public String putGyjRePur(@RequestBody GetPutRePurReqDto dto) throws Exception {
        String resJsonStr = tranService.tranTwRePur(dto);
        return resJsonStr;
    }

    @PostMapping("putGyjTwRePur")
    public String putGyjTwRePur(@RequestBody GetPutRePurReqDto dto) throws Exception {
        String resJsonStr = tranService.tranTwRePur(dto);
        return resJsonStr;
    }

    @PostMapping("putTwRePur")
    public String putTwRePur(@RequestBody GetPutRePurReqDto dto) throws Exception {
        String resJsonStr = tranService.tranTwRePur(dto);
        return resJsonStr;
    }

}
