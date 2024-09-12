package com.me.modules.inventory.dto;

import lombok.Data;

import java.util.List;

@Data
public class GetBatInventoryReqDto {

    private List<String> skus;

    private String stockNumber;

    private String storeType;
}
