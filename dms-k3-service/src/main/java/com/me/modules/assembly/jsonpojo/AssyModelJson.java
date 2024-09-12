package com.me.modules.assembly.jsonpojo;

import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.List;

@Data
public class AssyModelJson {

    private String FID;

    private String FBillNo;

    private NumberJson FStockOrgId;

    private String FAffairType;

    private String FDate;

    private List<AssyFEntityJson> FEntity;
}
