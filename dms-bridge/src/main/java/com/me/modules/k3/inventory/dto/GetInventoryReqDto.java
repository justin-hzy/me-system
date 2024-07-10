package com.me.modules.k3.inventory.dto;

import lombok.Data;

@Data
public class GetInventoryReqDto {

    private String sku;

    private String stockNumber;

    private String storeType;
}
