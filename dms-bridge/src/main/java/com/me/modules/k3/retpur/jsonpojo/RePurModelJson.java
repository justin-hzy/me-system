package com.me.modules.k3.retpur.jsonpojo;

import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RePurModelJson {

    private NumberJson FBillTypeID;

    private String FBillNo;

    private String FDate;

    private NumberJson FStockOrgId;

    private NumberJson FPurchaseOrgId;

    private NumberJson FSupplierId;

    private NumberJson FDemandOrgId;

    private FPURMRBFIN FPURMRBFIN;

    List<FPURMRB> FPURMRBENTRY = new ArrayList<>();

}
