package com.me.nascent.modules.qimen.controller;

import com.me.nascent.modules.qimen.dto.QiMenDto;
import com.me.nascent.modules.qimen.service.QiMenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("QiMen")
@AllArgsConstructor
public class QiMenController {

    private QiMenService qiMenService;

    @PostMapping("transQiMen")
    private String transOrder(@RequestBody QiMenDto dto) throws Exception {
        //log.info("dto="+dto.toString());
        /*return null;*/

        String respStr = qiMenService.transQiMen(dto);
        //log.info("respStr="+respStr);
        return respStr;
    }

}
