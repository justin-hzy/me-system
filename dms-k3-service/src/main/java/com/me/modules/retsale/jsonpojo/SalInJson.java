package com.me.modules.retsale.jsonpojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SalInJson {

    private String Creator;

    private String IsDeleteEntry ;

    private String IsVerifyBaseDataField;

    private String IsAutoSubmitAndAudit;

    private List NeedUpDateFields = new ArrayList();

    private List NeedReturnFields = new ArrayList();

    private String SubSystemId;

    private String InterationFlags;

    private String IsAutoAdjustField;

    private SalInModelJson Model;
}
