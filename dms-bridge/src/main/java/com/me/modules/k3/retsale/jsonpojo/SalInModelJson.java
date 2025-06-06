package com.me.modules.k3.retsale.jsonpojo;


import com.me.modules.k3.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.List;

@Data
public class SalInModelJson {

    private String FID;

    private NumberJson FBillTypeID;

    private String FBillNo;

    private String FDate;

    private NumberJson FSaleOrgId;

    private NumberJson FStockOrgId;

    private NumberJson FRetcustId;

    private SaleInSubHeadEntityJson SubHeadEntity;


    private List<SalInFEntityJson> FEntity;
}
