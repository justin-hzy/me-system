package com.me.modules.order.mretail.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("M_RETAIL")
public class MRetail {

    @TableField("ID")
    private int id;

    @TableField("AD_CLIENT_ID")
    private int adClientId;

    @TableField("AD_ORG_ID")
    private int adOrgId;

    @TableField("CREATIONDATE")
    private Date creationDate;

    @TableField("MODIFIEDDATE")
    private Date modifiedDate;

    @TableField("OWNERID")
    private int ownerId;

    @TableField("MODIFIERID")
    private int modifierId;

    @TableField("ISACTIVE")
    private String isActive;

    @TableField("DOCNO")
    private String docNo;

    @TableField("BILLDATE")
    private int billDate;

    @TableField("DATEOUT")
    private int dateOUT;

    @TableField("DATEIN")
    private int dateIN;

    @TableField("c_store_id")
    private Integer cStoreId;

    @TableField("status")
    private Integer status;

    @TableField("description")
    private String description;

    @TableField("retailbilltype")
    private String retailBillType;

    @TableField("refno")
    private String refNo;

    @TableField("oms_sourcecode")
    private String omsSourceCode;

    @TableField("ISINTL")
    private String isInTl;

    @TableField("ORDERNO")
    private String orderNo;

    @TableField("Tradetype")
    private String tradeType;

    @TableField("wdt_c_store")
    private String wdtCStore;

}
