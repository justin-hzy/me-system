package com.me.modules.transaction.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("transaction_detail")
public class TransactionDetail {


    @TableField("accountBalance")
    private String accountBalance;

    @TableField("accountName")
    private String accountName;

    @TableField("accountNature")
    private String accountNature;

    @TableField("accountNo")
    private String accountNo;

    @TableField("accountStatus")
    private String accountStatus;

    @TableField("associatedCustomerNumber")
    private String associatedCustomerNumber;

    @TableField("bankSerialNumber")
    private String bankSerialNumber;

    @TableField("bankTransactionDate")
    private String bankTransactionDate;

    @TableField("bankType")
    private String bankType;

    @TableField("bookingDate")
    private String bookingDate;

    @TableField("checkCode")
    private String checkCode;

    @TableField("corporateIdentityCode")
    private String corporateIdentityCode;

    @TableField("currency")
    private String currency;

    @TableField("detailSource")
    private String detailSource;

    @TableField("detailType")
    private String detailType;

    @TableField("digest")
    private String digest;

    @TableField("erpSerialNumber")
    private String erpSerialNumber;

    @TableField("extensionMsg")
    private String extensionMsg;

    @TableField("incurredAmount")
    private String incurredAmount;

    @TableField("loanType")
    private String loanType;

    @TableField("merchantName")
    private String merchantName;

    @TableField("merchantNumber")
    private String merchantNumber;

    @TableField("openBank")
    private String openBank;

    @TableField("oppositeAccount")
    private String oppositeAccount;

    @TableField("oppositeName")
    private String oppositeName;

    @TableField("oppositeOpeningBank")
    private String oppositeOpeningBank;

    @TableField("payApplyRemark1")
    private String payApplyRemark1;

    @TableField("payApplyRemark2")
    private String payApplyRemark2;

    @TableField("payApplyRemark3")
    private String payApplyRemark3;

    @TableField("paymentNature")
    private String paymentNature;

    @TableField("paymentNatureFlag")
    private String paymentNatureFlag;

    @TableField("personalizeField1")
    private String personalizeField1;

    @TableField("personalizeField2")
    private String personalizeField2;

    @TableField("personalizeField3")
    private String personalizeField3;

    @TableField("personalizeField4")
    private String personalizeField4;

    @TableField("personalizeField5")
    private String personalizeField5;

    @TableField("postscript")
    private String postscript;

    @TableField("protocolType")
    private String protocolType;

    @TableField("protocolTypeName")
    private String protocolTypeName;

    @TableField("purpose")
    private String purpose;

    @TableField("remark")
    private String remark;

    @TableField("reserveField1")
    private String reserveField1;

    @TableField("reserveField2")
    private String reserveField2;

    @TableField("reserveField3")
    private String reserveField3;

    @TableField("reserveField4")
    private String reserveField4;

    @TableField("transactionCode")
    private String transactionCode;

    @TableField("transactionSerialNumber")
    private String transactionSerialNumber;

    @TableField("unitCode")
    private String unitCode;

    @TableField("unitName")
    private String unitName;

    @TableField("updateTime")
    private String updateTime;

    @TableField("valueDate")
    private String valueDate;

    @TableField("virtualAccount")
    private String virtualAccount;

    @TableField("virtualAccountName")
    private String virtualAccountName;

    @TableField("voucherCode")
    private String voucherCode;
}
