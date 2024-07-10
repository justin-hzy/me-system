package com.me.modules.wangdianstockout.controller;

import com.me.modules.bi.service.TransWangDianService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("WangDianStockOut")
@AllArgsConstructor
@Slf4j
public class WangDianStockOutController {

    private TransWangDianService transWangDianService;

    @GetMapping("getWangDianTongStockOut")
    public String getWangDianTongStockOut(){
        log.info("旺店通出库单据同步开始");
        Integer currentPageNo = 0;
        Integer pageSize = 100;
        Integer totalCount = transWangDianService.getWangDianTongStockOut(currentPageNo,pageSize);
        currentPageNo = currentPageNo + 1;
        for(;totalCount>pageSize*currentPageNo;currentPageNo++){
            transWangDianService.getWangDianTongStockOut(currentPageNo,pageSize);
        }
        log.info("totalCount="+totalCount);
        log.info("旺店通出库单据同步结束");
        return "success";
    }
}
