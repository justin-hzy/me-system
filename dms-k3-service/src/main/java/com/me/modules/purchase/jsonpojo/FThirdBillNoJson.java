package com.me.modules.purchase.jsonpojo;

import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class FThirdBillNoJson {

    private String FTHIRDBILLNO;

    private NumberJson FSettleCurrId;

    private String FIsIncludedTax;

    private String FAllDisCount;
}
