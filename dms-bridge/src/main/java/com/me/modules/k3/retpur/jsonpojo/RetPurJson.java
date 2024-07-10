package com.me.modules.k3.retpur.jsonpojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RetPurJson {
    
    private String Creator;

    private List NeedUpDateFields = new ArrayList();;

    private List NeedReturnFields = new ArrayList();;

    private String IsDeleteEntry;

    private String SubSystemId;

    private String IsVerifyBaseDataField;

    private String IsAutoSubmitAndAudit;

    private String IsEntryBatchFill;

    private String InterationFlags;

    private RePurModelJson model;
}
