package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("allergy_reaction_order_product")
public class  AllergyReactionOrderProduct {

    @TableField(value = "id")
    @ApiModelProperty("id")
    private String id;

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
