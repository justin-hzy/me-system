package com.me.modules.sale.jsonpojo;

import lombok.Data;

@Data
public class SalOutFEntityJson {

    private String FEntryID;

    private NumberJson FMaterialId;

    private String FEntryTaxRate;

    private String FRealQty;

    private NumberJson FStockId;

    private String FTAXPRICE;

    private String FSoorDerno;

    private String F_DSG_srcoid;

    //批次1
    private NumberJson FLot;

    //生产日期
    private String FProduceDate;

    //效期
    private String FExpiryDate;


}
