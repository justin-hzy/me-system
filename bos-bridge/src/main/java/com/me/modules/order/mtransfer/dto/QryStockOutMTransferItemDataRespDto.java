package com.me.modules.order.mtransfer.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QryStockOutMTransferItemDataRespDto {

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

    @TableField("mDim4Id")
    private Integer mDim4Id;

    @TableField("preCost")
    private BigDecimal preCost;

    @TableField("pricelist")
    private BigDecimal priceList;
}
