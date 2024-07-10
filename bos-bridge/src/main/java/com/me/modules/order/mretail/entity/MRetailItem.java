package com.me.modules.order.mretail.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_retailitem")
public class MRetailItem {

    @TableField("ID")
    private int id;

    @TableField("AD_CLIENT_ID")
    private Integer adClientId;

    @TableField("AD_ORG_ID")
    private Integer adOrgId;

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

    @TableField("m_retail_id")
    private Integer mRetailId;

    @TableField("m_productalias_id")
    private Integer mProductAliasId;

    @TableField("m_product_id")
    private Integer mProductId;

    @TableField("m_attributesetinstance_id")
    private Integer mAttributeSetInstanceId;

    @TableField("qty")
    private String qty;

    @TableField("pricelist")
    private BigDecimal priceList;

    @TableField("priceactual")
    private BigDecimal priceActual;

    @TableField("tot_amt_list")
    private BigDecimal totAmtList;

    @TableField("status")
    private Integer status;

    @TableField("tot_amt_actual")
    private BigDecimal totAmtActual;

    @TableField("TYPE")
    private Integer type;

    @TableField("c_vip_id")
    private String cVipId;

    @TableField("srctid")
    private String srcTid;

    @TableField("srcoid")
    private String srcOid;

    @TableField("sharepost")
    private String sharePost;



}
