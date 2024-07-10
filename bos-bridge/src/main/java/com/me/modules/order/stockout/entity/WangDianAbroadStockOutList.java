package com.me.modules.order.stockout.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/*@TableName("WANGDIAN_ABROAD_STOCKOUT_LIST")*/
@TableName("WANGDIAN_STOCKOUT_LIST")
public class WangDianAbroadStockOutList {

    @TableField("STOCKOUTID")
    @ApiModelProperty("出库单主键id")
    private String STOCKOUTID;

    @TableField("SPECNO")
    @ApiModelProperty("商家编码")
    private String SPECNO;

    @TableField("SELLPRICE")
    @ApiModelProperty("销售价")
    private String SELLPRICE;

    @TableField("GIFTTYPE")
    @ApiModelProperty("是否是赠品 0非赠品 1自动赠送 2手工赠送 3回购自动送赠品  4前N有礼送赠品 6天猫优仓赠品")
    private String GIFTTYPE;

    @TableField("SHAREPOST")
    @ApiModelProperty("邮费分摊")
    private String SHAREPOST;

    @TableField("SRCOID")
    @ApiModelProperty("原始子订单号")
    private String SRCOID;

    @TableField("SRCTID")
    @ApiModelProperty("子单原始单号")
    private String SRCTID;

    @TableField("NUM")
    @ApiModelProperty("货品数量")
    private String NUM;

    @TableField("MARKETPRICE")
    @ApiModelProperty("零售价")
    private String MARKETPRICE;

}
