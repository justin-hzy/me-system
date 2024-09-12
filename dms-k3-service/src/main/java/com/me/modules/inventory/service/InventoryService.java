package com.me.modules.inventory.service;

import com.alibaba.fastjson.JSONObject;
import com.me.common.core.JsonResult;

import java.util.List;

public interface InventoryService {

    public String getTWInventory(String sku, String stockNumber) throws Exception;

    String getBatTWInventory(List<String> skus,String stockNumber) throws Exception;
}
