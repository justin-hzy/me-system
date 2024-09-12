package com.me.modules.purchase.jsonpojo;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseJson{

    private List NeedUpDateFields = new ArrayList();

    private List NeedReturnFields = new ArrayList();

    private String IsDeleteEntry ;

    private String SubSystemId;

    private String IsVerifyBaseDataField;

    private String IsAutoSubmitAndAudit;

    private String IsEntryBatchFill;

    private String InterationFlags;

    private String IsAutoAdjustField;

    private PurchaseModelJson Model;

}
