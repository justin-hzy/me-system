package com.me.modules.k3.assembly.jsonpojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssyJson {
    private List NeedUpDateFields = new ArrayList();

    private List NeedReturnFields = new ArrayList();

    private String IsDeleteEntry;

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

    private AssyModelJson Model;


}
