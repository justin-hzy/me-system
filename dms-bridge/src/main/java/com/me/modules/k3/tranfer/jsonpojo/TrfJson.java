package com.me.modules.k3.tranfer.jsonpojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrfJson {

    private List NeedUpDateFields = new ArrayList();

    private List NeedReturnFields = new ArrayList();

    private String IsDeleteEntry ;

    private String SubSystemId;

    private String IsVerifyBaseDataField;

    private String IsEntryBatchFill;

    private String ValidateFlag;

    private String NumberSearch;

    private String IsAutoAdjustField;

    private String InterationFlags;

    private String IgnoreInterationFlag;

    private String IsControlPrecision;

    private String ValidateRepeatJson;

    private String IsAutoSubmitAndAudit;

    private TrfModelJson Model;
}
