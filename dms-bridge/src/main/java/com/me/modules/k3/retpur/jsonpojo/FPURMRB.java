package com.me.modules.k3.retpur.jsonpojo;

import com.me.modules.k3.sale.jsonpojo.NumberJson;
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
}
