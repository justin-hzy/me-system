package com.me.modules.k3.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.modules.k3.inventory.entity.UfInventory;
import com.me.modules.k3.sale.dto.PutSaleReqDto;

public interface UfInventoryService extends IService<UfInventory> {

    void updateUfInventory(PutSaleReqDto dto);
}
