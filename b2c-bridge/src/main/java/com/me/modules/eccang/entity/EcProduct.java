package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("EcProduct")
public class EcProduct {

    @TableField("referenceNo")
    private String referenceNo;

    @TableField("sku")
    private String sku;

    @TableField("warehouseProductBarcode")
    private String warehouseProductBarcode;

    @TableField("platformSalesSku")
    private Float platformSalesSku;

    @TableField("quantity")
    private String quantity;

    @TableField("opSalesPrice")
    private String opSalesPrice;

    @TableField("declaredValue")
    private String declaredValue;

    @TableField("productTitle")
    private String productTitle;

    @TableField("productWeight")
    private String productWeight;

    @TableField("recvAccount")
    private String recvAccount;

    @TableField("refTnx")
    private String refTnx;

    @TableField("refItemId")
    private String refItemId;

}
