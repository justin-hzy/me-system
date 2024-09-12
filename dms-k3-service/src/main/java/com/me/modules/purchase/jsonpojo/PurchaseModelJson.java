package com.me.modules.purchase.jsonpojo;


import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseModelJson {

    private NumberJson FBillTypeID;

    private String FBillNo;

    private String FDate;

    private FThirdBillNoJson FInStockFin;

    private NumberJson FStockOrgId;

    private NumberJson FPurchaseOrgId;

    private NumberJson FSupplierId;

    private NumberJson FDemandOrgId;

    List<FInStockEntryJson> FInStockEntry = new ArrayList<>();
}
