package com.me.modules.inventory.controller;

import com.alibaba.fastjson.JSONObject;
import com.me.common.core.JsonResult;
import com.me.modules.inventory.dto.GetBatInventoryReqDto;
import com.me.modules.inventory.dto.GetInventoryReqDto;
import com.me.modules.inventory.service.InventoryService;
import jdk.jfr.Label;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("k3")
@AllArgsConstructor
@Slf4j
public class InventoryController {

    private InventoryService inventoryService;


    @PostMapping("getInventory")
    public String getInventory(@RequestBody GetInventoryReqDto dto){

        String sku = dto.getSku();

        String stockNumber = dto.getStockNumber();


        String type = dto.getStoreType();

        String resStr = "";

        if("TW".equals(type)){
            try {
                resStr = inventoryService.getTWInventory(sku,stockNumber);
            } catch (Exception e) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",500);
                jsonObject.put("data","接口异常");
                resStr = jsonObject.toJSONString();
                return resStr;
            }
        }else if("HK".equals(type)){
            return resStr;
        }

        return resStr;
    }


    @PostMapping("getBatchInventory")
    public String getBatchInventory(@RequestBody GetBatInventoryReqDto dto) throws Exception {
        String resStr = inventoryService.getBatTWInventory(dto.getSkus(),dto.getStockNumber());
        log.info("resStr="+resStr);
        return resStr;
    }


}
