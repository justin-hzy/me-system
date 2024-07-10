package com.me.modules.k3.tranfer.jsonpojo;

import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

@Data
public class FBillEntryJson {

    private String FRowType;

    private NumberJson FMaterialId;

    private String FQty;

    private NumberJson FSrcStockId;

    private NumberJson FDestStockId;
}
