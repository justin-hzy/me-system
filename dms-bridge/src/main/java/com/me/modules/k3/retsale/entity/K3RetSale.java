package com.me.modules.k3.retsale.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("K3_RET_SALE")
public class K3RetSale {

    @TableField("ID")
    private String id;

    @TableField("FORMID")
    private String formId;

    @TableField("FID")
    private String fid;

    @TableField("FBILLTYPEID")
    private String fbillTypeId;

    @TableField("FBILLNO")
    private String fbillNo;

    @TableField("FDATE")
    private String fdate;

    @TableField("FSALEORGID")
    private String fsaleOrgId;

    @TableField("FRETCUSTID")
    private String fretCustId;

    @TableField("FSTOCKORGID")
    private String fstockOrgId;

    @TableField("EDIFLAG")
    private String ediFlag;

    @TableField("WRITEDATE")
    private String writeDate;

    @TableField("RESULTS")
    private String results;

    @TableField("BOS_ID")
    private Long bosId;

    @TableField("FENTRYTAXRATE")
    private Integer fentryTaxRate;

    @TableField("F_ZJM_AMOUNT_SCQ")
    private String fzjmAmountScq;

    @TableField("F_ZJM_AMOUNT_CSJF")
    private Integer fzjmAmountCsjf;

    @TableField("F_ZJM_AMOUNT_GWQ")
    private String fzjmAmountGwq;

    @TableField("F_ZJM_AMOUNT_JFXF")
    private Integer fzjmAmountJfxf;

    @TableField("FOWNERTYPEIDHEAD")
    private String fownerTypeIdHead;

    @TableField("FSETTLEORGID")
    private String fsettleOrgId;

    @TableField("FSETTLECURRID")
    private String fsettleCurrId;

    @TableField("FEXCHANGETYPEID")
    private String fexchangeTypeId;

    @TableField("FEXCHANGERATE")
    private String fexchangeRate;

    @TableField("F_ZJM_ASSISTANT_HUIYUAN")
    private String fzjmAssistantHuiyuan;

    @TableField("F_ZJM_DECIMAL")
    private Integer fzjmDecimal;

    @TableField("FSALEDEPTID")
    private String fsaleDeptId;

    @TableField("F_DH_YWMS")
    private String fdhYwms;

    @TableField("F_DSG_TEXT1")
    private String fdsgText1;

    @TableField("FTHIRDBILLNO")
    private String fthirdBillNo;

}
