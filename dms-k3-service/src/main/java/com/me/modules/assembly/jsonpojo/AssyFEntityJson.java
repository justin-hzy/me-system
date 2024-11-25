package com.me.modules.assembly.jsonpojo;

import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.List;

@Data
public class AssyFEntityJson {

    private NumberJson FMaterialID;

    private String FQty;

    private NumberJson FStockID;

    private String Fee_ETY;

    private NumberJson FRefBomID;

    private String FSrcBillNo;

    private String FCPWdtID;

    private NumberJson FLot;

    private String FProduceDate;

    private String FExpiryDate;

    private List<AssyFsubFntityJson> FSubEntity;
}
