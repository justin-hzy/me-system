package com.me.modules.k3.purchase.jsonpojo;

import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class FThirdBillNoJson {

    private String FTHIRDBILLNO;

    private NumberJson FSettleCurrId;

    private String FIsIncludedTax;

    private String FAllDisCount;
}
