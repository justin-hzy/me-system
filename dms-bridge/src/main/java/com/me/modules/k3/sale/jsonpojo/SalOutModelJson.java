package com.me.modules.k3.sale.jsonpojo;

import lombok.Data;

import java.util.List;

@Data
public class SalOutModelJson {

    private String FID;

    private NumberJson FBillTypeID;

    private String FBillNo;

    private String FDate;

    private NumberJson FStockOrgId;

    private NumberJson FSaleOrgId;

    private String FOwnerTypeIdHead;

    private NumberJson FCUSTOMERID;

    private NumberJson F_DSG_Base;

    private String F_DSG_FLLX;

    private String F_DSG_Text1;

    private String FReceiveAddress;

    private SaleOutSubHeadEntityJson SubHeadEntity;

    private List<SalOutFEntityJson> FEntity;

}
