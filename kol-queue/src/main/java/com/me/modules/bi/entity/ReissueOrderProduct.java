package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReissueOrderProduct {

    @TableField(value = "id")
    @ApiModelProperty("报表id")
    private String id;

    @TableField("product_name")
    @ApiModelProperty("货品名称")
    private String productName;

    @TableField("merchant_code")
    @ApiModelProperty("商家编码/国际条码")
    private String merchantCode;

    @TableField("reissue_number")
    @ApiModelProperty("补发数量")
    private String reissueNumber;

    @TableField("brand")
    @ApiModelProperty("品牌")
    private String brand;
}
