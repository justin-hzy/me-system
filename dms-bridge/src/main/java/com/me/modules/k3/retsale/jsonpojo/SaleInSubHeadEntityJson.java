package com.me.modules.k3.retsale.jsonpojo;


import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class SaleInSubHeadEntityJson {

    private NumberJson FSettleOrgID;

    private NumberJson FSettleCurrId;

    private String FThirdBillNo;
}
