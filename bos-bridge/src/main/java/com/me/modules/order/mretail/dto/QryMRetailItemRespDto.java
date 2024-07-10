package com.me.modules.order.mretail.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QryMRetailItemRespDto {

    @TableField("id")
    private Integer id;

    @TableField("m_product_id")
    private Integer mProductId;

    @TableField("m_attributesetinstance_id")
    private Integer mAttributeSetInstanceId;

    @TableField("num")
    private String num;

    @TableField("pricelist")
    private BigDecimal priceList;

    @TableField("sellprice")
    private String sellPrice;

    @TableField("srctid")
    private String srcTid;

    @TableField("srcoid")
    private String srcOid;

    @TableField("sharepost")
    private String sharePost;













}
