package com.me.modules.order.mtransfer.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("m_transferitem")
public class MTransferItem {

    @TableField("id")
    private Integer id;

    @TableField("ad_client_id")
    private Integer adClientId;

    @TableField("ad_org_id")
    private Integer adOrgId;

    @TableField("ownerid")
    private Integer ownerId;

    @TableField("modifierid")
    private Integer modifierId;

    @TableField("creationdate")
    private Date creationdate;

    @TableField("modifieddate")
    private Date modifiedDate;

    @TableField("isactive")
    private String isActive;

    @TableField("m_transfer_id")
    private Integer mTransferId;

    @TableField("m_product_id")
    private Integer mProductId;

    @TableField("m_attributesetinstance_id")
    private Integer mAttributeSetInstanceId;

    @TableField("qtyout")
    private Integer qtyOut;

    @TableField("qtyin")
    private Integer qtyIn;

    @TableField("qty")
    private Integer qty;

    @TableField("m_dim4_id")
    private Integer mDim4Id;

    @TableField("precost")
    private BigDecimal preCost;

    @TableField("pricelist")
    private BigDecimal priceList;

}
