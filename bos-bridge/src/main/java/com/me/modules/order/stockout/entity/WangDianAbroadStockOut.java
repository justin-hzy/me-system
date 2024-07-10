package com.me.modules.order.stockout.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
//@TableName("WANGDIAN_ABROAD_STOCKOUT")
@TableName("WANGDIAN_STOCKOUT")
public class WangDianAbroadStockOut {

    @ApiModelProperty("出库单主键id")
    @TableField("STOCKOUTID")
    private String STOCKOUTID;

    @ApiModelProperty("出库单号")
    @TableField("ORDERNO")
    private String ORDERNO;

    @ApiModelProperty("仓库编号")
    @TableField("WAREHOUSENO")
    private String WAREHOUSENO;

    @ApiModelProperty("发货时间")
    @TableField("CONSIGNTIME")
    private String CONSIGNTIME;

    @ApiModelProperty("订单类型")
    @TableField("TRADETYPE")
    private String TRADETYPE;

    @ApiModelProperty("订单状态")
    @TableField("TRADESTATUS")
    private String TRADESTATUS;

    @ApiModelProperty("仓库名称")
    @TableField("WAREHOUSENAME")
    private String WAREHOUSENAME;

    @ApiModelProperty("店铺名称")
    @TableField("SHOPNAME")
    private String SHOPNAME;

    @ApiModelProperty("原始单号")
    @TableField("SRCTIDS")
    private String SRCTIDS;

    @ApiModelProperty("优惠金额")
    @TableField("DISCOUNT")
    private String DISCOUNT;

    @TableField("EDIFLAG")
    private String EDIFLAG;

}
