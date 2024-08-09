package com.me.nascent.modules.reorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("refund")
public class ReFund {

    @TableField("id")
    private Long id;

    @TableField("address")
    private String address;

    @TableField("applyTime")
    private Date applyTime;

    @TableField("companyName")
    private String companyName;

    @TableField("goodsLibId")
    private int goodsLibId;

    @TableField("mobile")
    private String mobile;

    @TableField("modified")
    private Date modified;

    @TableField("number")
    private BigDecimal number;

    @TableField("outOrderId")
    private String outOrderId;

    @TableField("outRefundId")
    private String outRefundId;

    @TableField("outTradeId")
    private String outTradeId;

    @TableField("outerId")
    private String outerId;

    @TableField("picUrl")
    private String picUrl;

    @TableField("platform")
    private Integer platform;

    @TableField("price")
    private BigDecimal price;

    @TableField("reasonStr")
    private String reasonStr;

    @TableField("refundDesc")
    private String refundDesc;

    @TableField("refundFee")
    private BigDecimal refundFee;

    @TableField("refundGenre")
    private Integer refundGenre;

    @TableField("refundName")
    private String refundName;

    @TableField("refundNum")
    private BigDecimal refundNum;

    @TableField("refundRemindTimeout")
    private String refundRemindTimeout;

    @TableField("refundShopId")
    private int refundShopId;

    @TableField("refundStatus")
    private String refundStatus;

    @TableField("refundType")
    private Integer refundType;

    @TableField("refundWap")
    private int refundWap;

    @TableField("refuseReason")
    private String refuseReason;

    @TableField("shopId")
    private int shopId;

    @TableField("sid")
    private String sid;

    @TableField("skuOuterId")
    private String skuOuterId;

    @TableField("sysTradeId")
    private String sysTradeId;

    @TableField("title")
    private String title;

    @TableField("totalFee")
    private BigDecimal totalFee;

    @TableField("updateTime")
    private Date updateTime;
}
