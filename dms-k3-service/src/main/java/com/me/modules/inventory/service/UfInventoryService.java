package com.me.modules.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.inventory.entity.UfInventory;
import com.me.modules.sale.dto.PutSaleReqDto;

public interface UfInventoryService extends IService<UfInventory> {

    void updateUfInventory(PutSaleReqDto dto);
}
