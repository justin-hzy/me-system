package com.me.modules.fl.dto;

import com.me.modules.fl.pojo.PurchaseInItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetPurInCBDto {

    private String title;

    private String status;

    List<PurchaseInItem> items = new ArrayList<>();
}
