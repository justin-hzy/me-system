package com.me.modules.order.stockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("WANGDIAN_ABROAD_STOCKIN_LIST")
public class WangDianAbroadStockInList {

    @TableField("STOCKINID")
    @ApiModelProperty("出库单主键id")
    private String STOCKINID;

    @TableField("SPECNO")
    @ApiModelProperty("商家编码")
    private String SPECNO;

    @TableField("NUM")
    @ApiModelProperty("货品数量")
    private String NUM;
}
