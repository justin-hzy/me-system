package com.me.modules.retpur.jsonpojo;

import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class FPURMRB {

    private String FEntryID;

    private NumberJson FMaterialId;

    private String FRMREALQTY;

    private NumberJson FStockId;

    private String FTAXPRICE;

    private String fnote;

    private String FENTRYTAXRATE;

    private NumberJson FLot;

    private String FProduceDate;

    private String FExpiryDate;
}
