package com.me.modules.purchase.jsonpojo;


import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FInStockEntryJson {
    
    private String FEntryID;
    
    private NumberJson FMaterialId;

    private String FRealQty;
    
    private NumberJson FStockId;

    private String FPOOrderNo;

    private String FTAXPRICE;

    private String FNOTE;

    private String FGIVEAWAY;

    private String FENTRYTAXRATE;

    private String F_DH_EntrySourceNO;

    private String FSrcFormId;

    private String FSrcBillNo;

    private String FPOORDERNO;

    //List<FInStockEntryLinkJson> FInStockEntry_Link = new ArrayList<>();

    public String getFPOOrderNo() {
        return FPOOrderNo;
    }

    public void setFPOOrderNo(String FPOOrderNo) {
        this.FPOOrderNo = FPOOrderNo;
    }

    public String getFPOORDERNO() {
        return FPOORDERNO;
    }

    public void setFPOORDERNO(String FPOORDERNO) {
        this.FPOORDERNO = FPOORDERNO;
    }
}
