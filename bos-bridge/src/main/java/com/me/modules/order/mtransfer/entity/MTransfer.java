package com.me.modules.order.mtransfer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class MTransfer {
    @TableField("ID")
    private int id;

    @TableField("AD_CLIENT_ID")
    private int adClientId;

    @TableField("AD_ORG_ID")
    private int adOrgId;

    @TableField("OWNERID")
    private int ownerId;

    @TableField("MODIFIERID")
    private int modifierId;

    @TableField("CREATIONDATE")
    private Date creationDate;

    @TableField("MODIFIEDDATE")
    private Date modifiedDate;

    @TableField("ISACTIVE")
    private char isActive;

    @TableField("DOCNO")
    private String docNo;

    @TableField("DOCTYPE")
    private String docType;

    @TableField("C_ORIG_ID")
    private Integer cOrigId;

    @TableField("C_DEST_ID")
    private Integer cDestId;

    @TableField("description")
    private String description;

    @TableField("transfertype")
    private String transferType;

    @TableField("BILLDATE")
    private int billDate;

    @TableField("DATEOUT")
    private int dateOUT;

    @TableField("DATEIN")
    private int dateIN;

    @TableField("TRANSFERNO")
    private String transferNo;

    @TableField("ORDERNO")
    private String orderNo;
}
