package com.me.modules.k3.sale.jsonpojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SalOutJson {

    private List NeedUpDateFields = new ArrayList();

    private List NeedReturnFields = new ArrayList();

    private String IsDeleteEntry ;

    private String SubSystemId;

    private String IsVerifyBaseDataField;

    private String IsEntryBatchFill;

    private String ValidateFlag;

    private String NumberSearch;

    private String IsAutoAdjustField;

    private String IsAutoSubmitAndAudit;

    private String InterationFlags;

    private String IgnoreInterationFlag;

    private String IsControlPrecision;

    private String ValidateRepeatJson;

    private SalOutModelJson Model;

}
