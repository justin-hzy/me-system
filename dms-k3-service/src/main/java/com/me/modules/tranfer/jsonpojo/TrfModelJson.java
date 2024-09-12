package com.me.modules.tranfer.jsonpojo;

import com.me.modules.sale.jsonpojo.NumberJson;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrfModelJson {

    private String FID;

    private String FBillNo;

    private NumberJson FBillTypeID;

    private String FBizType;

    private String FTransferDirect;

    private String FTransferBizType;

    private NumberJson FStockOutOrgId;

    private String FDate;

    private String FThirdSrcBillNo;

    List<FBillEntryJson> FBillEntry = new ArrayList<>();
}
