package com.me.modules.purchase.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("K3_M_PURCHASE")
public class K3MPurchase {

    @TableField("ID")
    private String id;

    @TableField("EDIFLAG")
    private String ediFlag;

    @TableField("WRITEDATE")
    private String writeDate;

    @TableField("RESULTS")
    private String results;

    @TableField("BOS_ID")
    private Long bosId;

    @TableField("FBILLTYPEID")
    private String fbillTypeId;

    @TableField("FBILLNO")
    private String fbillNo;

    @TableField("FDATE")
    private String fdate;

    @TableField("FSTOCKORGID")
    private String fstockOrgId;

    @TableField("FDEMANDORGID")
    private String fdemandOrgId;

    @TableField("FPURCHASEORGID")
    private String fpurchaseOrgId;

    @TableField("FSUPPLIERID")
    private String fsupplierId;

    @TableField("F_DH_DESCRIPTION")
    private String fdhDescription;

    @TableField("F_DH_SOURCETYPE")
    private String fdhSourceType;

    @TableField("F_DH_SOURCENO")
    private String fdhSourceNo;

    @TableField("FSETTLECURRID")
    private String fsettleCurrId;

    @TableField("FSETTLEORGID")
    private String fsettleOrgId;

    @TableField("FEXCHANGERATE")
    private String fexchangeRate;

    @TableField("F_WK_BZ1")
    private String fwkBz1;

    @TableField("F_WK_KH1")
    private String fwkKh1;

    @TableField("FBUSINESSTYPE")
    private String fbusinesstype;

    @TableField("FID")
    private String fid;

    @TableField("FORMID")
    private String formId;

    @TableField("FSRCBILLNO")
    private String fsrcBillNo;

    @TableField("FTHIRDBILLNO")
    private String fthirdBillNo;
}
