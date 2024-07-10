package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("store_af_sales_exception_product")
public class StoreAfSalesExceptionProduct {

    @TableField(value = "id")
    @ApiModelProperty("报表id")
    private String id;

    @TableField("quantity")
    @ApiModelProperty("商品数量")
    private String quantity;

    @TableField("merchant_code")
    @ApiModelProperty("商家编码/国际条码")
    private String merchantCode;

    @TableField("brand")
    @ApiModelProperty("品牌")
    private String brand;

    @TableField("product_name")
    @ApiModelProperty("货品名称")
    private String productName;
}
