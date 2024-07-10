package com.me.modules.order.stockin.controller;

import com.me.modules.order.stockin.service.TransAdStockInOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("stockin")
@AllArgsConstructor
@Slf4j
public class StockInController {

    private TransAdStockInOrderService transAdStockInOrderService;

    @GetMapping("getStockInOrderDetails")
    public void getStockOutOrderDetails(){
        log.info("获取外海旺店通入库单开始...");
        Integer currentPageNo = 1;
        Map<String,Object> resMap = transAdStockInOrderService.getStockInOrderDetails(currentPageNo);
        Boolean empty = (Boolean) resMap.get("empty");
        if(empty==true){
           Integer pageSize = (Integer) resMap.get("pageSize");
           Integer total = (Integer) resMap.get("total");
           for(;total>currentPageNo*pageSize;){
               currentPageNo = currentPageNo + 1;
               transAdStockInOrderService.getStockInOrderDetails(currentPageNo);
           }
        }
        log.info(resMap.toString());
        log.info("获取外海旺店通入库单结束...");
    }
}
