package com.me.modules.k3.retpur.jsonpojo;

import com.me.modules.k3.purchase.jsonpojo.FPurMrbEntryLinkJson;

import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FPurMrbEntryJson {

    private NumberJson FUnitID;

    private NumberJson FStockStatusId;

    private String FOWNERTYPEID;

    private String FSRCBillTypeId;

    private NumberJson FOWNERID;

    private NumberJson FMaterialId;

    private String FRMREALQTY;

    private String FREPLENISHQTY;

    private String FKEAPAMTQTY;

    private NumberJson FStockId;

    private String FNOTE;

    private String FGIVEAWAY;

    private String FTAXPRICE;

    private String FPrice;

    private String FENTRYTAXRATE;

    private String F_DH_EntrySourceNO;

    private String FPOORDERNO;

    List<FPurMrbEntryLinkJson> FPURMRBENTRY_Link = new ArrayList<>();

}
