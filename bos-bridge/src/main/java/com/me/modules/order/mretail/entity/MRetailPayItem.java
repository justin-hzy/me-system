package com.me.modules.order.mretail.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_retailpayitem")
public class MRetailPayItem {

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

    @TableField("m_retail_id")
    private Integer mRetailId;

    @TableField("c_payway_id")
    private Integer cPayWayId;

    @TableField("payamount")
    private BigDecimal payAmount;

    @TableField("description")
    private String description;









}
