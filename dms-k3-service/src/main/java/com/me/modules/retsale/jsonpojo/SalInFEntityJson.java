package com.me.modules.retsale.jsonpojo;


import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class SalInFEntityJson {

    private String FEntryID;

    private NumberJson FMaterialId;

    private String FRealQty;

    private NumberJson FStockId;

    private String FTaxPrice;

    private Integer FEntryTaxRate;

    private String FOrderNo;

    private String F_DSG_srcoid1;

}
