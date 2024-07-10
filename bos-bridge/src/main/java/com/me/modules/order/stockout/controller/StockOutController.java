package com.me.modules.order.stockout.controller;

import com.me.modules.order.stockout.service.TransAdStockOutOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("stockout")
@AllArgsConstructor
@Slf4j
public class StockOutController {

    private TransAdStockOutOrderService transAdStockOutOrderService;

    @GetMapping("getStockOutOrderDetails")
    public void getStockOutOrderDetails(){
        log.info("获取外海旺店通出库单开始...");
        Integer currentPageNo = 1;
        Map<String,Object> resMap = transAdStockOutOrderService.getStockOutOrderDetails(currentPageNo);
        Integer pageSize = (Integer) resMap.get("pageSize");
        Integer total = (Integer) resMap.get("total");
        Boolean empty = (Boolean) resMap.get("empty");
        log.info("pageSize="+pageSize+",total="+total+",empty="+empty);

        if(empty == false){
            for(;total>currentPageNo*pageSize;){
                currentPageNo = currentPageNo + 1;
                transAdStockOutOrderService.getStockOutOrderDetails(currentPageNo);
            }
        }
        log.info("获取外海旺店通出库单结束...");
    }
}
