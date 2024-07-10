package com.me.modules.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "B_PAYABLE")
@ApiModel(description = "测试表")
public class BPayable implements Serializable {

    @TableId(value = "id", type = IdType.NONE)
    private BigDecimal id;

    @TableField(value = "AD_CLIENT_ID")
    private BigDecimal adClientId;

    @TableField(value = "AD_ORG_ID")
    private BigDecimal adOrgId;

    @TableField(value = "OWNERID")
    private BigDecimal ownerId;

    @TableField(value = "MODIFIERID")
    private BigDecimal modifierId;

    @TableField(value = "CREATIONDATE")
    private String creationDate;

    @TableField(value = "MODIFIEDDATE")
    private String modifiedDate;

    @TableField(value = "ISACTIVE")
    private String isActive;

    @TableField(value = "DOCNO")
    private String docNo;

    @TableField(value = "DOCTYPE")
    private String docType;

    @TableField(value = "BILLDATE")
    private BigDecimal billDate;

    @TableField(value = "C_SUPPLIER_ID")
    private BigDecimal cSupplierId;

    @TableField(value = "DESCRIPTION")
    private String description;

    @TableField(value = "M_PURCHASE_ID")
    private BigDecimal mPurchaseId;

    @TableField(value = "M_RET_PUR_ID")
    private BigDecimal mRetPurId;

    @TableField(value = "STATUS")
    private BigDecimal status;

    @TableField(value = "AU_STATE")
    private String auState;

    @TableField(value = "AU_PI_ID")
    private BigDecimal auPiId;

    @TableField(value = "TOT_AMT_ACTUAL")
    private BigDecimal totAmtActual;

    @TableField(value = "STATUSERID")
    private BigDecimal statUserId;

    @TableField(value = "STATUSTIME")
    private String statusTime;

    @TableField(value = "C_FEETYPE_ID")
    private BigDecimal cFeeTypeId;

    @TableField(value = "C_PAYWAY_ID")
    private BigDecimal cPayWayId;

    @TableField(value = "B_SUPINVOICE_ID")
    private BigDecimal bSupInvoiceId;

    @TableField(value = "Y_PURCHASE_ID")
    private BigDecimal yPurchaseId;

    @TableField(value = "Y_PURRET_ID")
    private BigDecimal yPurRectId;

    @TableField(value = "B_BWPURINVOICE_ID")
    private BigDecimal bBwpurInvoiceId;

    @TableField(value = "B_RWPURINVOICE_ID")
    private BigDecimal bRwpurINVOICE_ID;

    @TableField(value = "B_BWMTLPURINVOICE_ID")
    private BigDecimal bBWMTLPURINVOICE_ID;

    @TableField(value = "B_RWMTLPURINVOICE_ID")
    private BigDecimal bRwmtlPurInvoiceId;

    @TableField(value = "TOT_QTYPAYED")
    private BigDecimal totQtyPayed;

    @TableField(value = "TOT_AMTPAYED")
    private BigDecimal totAmtPayed;

    @TableField(value = "NC_ACCOUNT_ID")
    private BigDecimal ncAccountId;

    @TableField(value = "NC_STATUS")
    private BigDecimal ncStatus;

    @TableField(value = "NC_STATUSERID")
    private BigDecimal ncStatUserId;

    @TableField(value = "NC_STATUSTIME")
    private String ncStatusTime;

    @TableField(value = "M_AGTPUR_ID")
    private BigDecimal mAgtpurId;

    @TableField(value = "M_AGTRET_PUR_ID")
    private BigDecimal mAgtretPurId;

    @TableField(value = "M_AGTPUR_ACC_ID")
    private BigDecimal mAgtpurAccId;

    @TableField(value = "TOT_AMT_SUM")
    private BigDecimal totAmtSum;

    @TableField(value = "TOT_AMT_DEPOSIT")
    private BigDecimal totAmtDeposit;


}
