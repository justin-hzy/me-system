package com.me.modules.mabang.order.dto;

import lombok.Data;

@Data
public class InsertMBOrderDtl {

    private String stockSku;

    private String quantity;

    private String sellPriceOrigin;
}
