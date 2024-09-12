package com.me.modules.retsale.jsonpojo;


import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class SaleInSubHeadEntityJson {

    private NumberJson FSettleOrgID;

    private NumberJson FSettleCurrId;

    private String FThirdBillNo;
}
