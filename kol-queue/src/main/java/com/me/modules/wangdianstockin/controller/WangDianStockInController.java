package com.me.modules.wangdianstockin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("WangDianStockIn")
public class WangDianStockInController {

    @GetMapping("getWangDianTongStockIn")
    public String getWangDianTongStockIn(){
        return "success";
    }
}
