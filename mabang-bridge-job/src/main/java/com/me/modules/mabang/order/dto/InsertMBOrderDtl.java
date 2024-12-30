package com.me.modules.mabang.order.dto;

import lombok.Data;

@Data
public class InsertMBOrderDtl {

    private String stockSku;

    private String quantity;

    private String sellPriceOrigin;

    private String voucherPriceOrigin;

    private String subsidyAmountOrigin;

    private String shippingTotalOrigin;

    private String otherIncome;

    private String itemTotalOrigin;
}
