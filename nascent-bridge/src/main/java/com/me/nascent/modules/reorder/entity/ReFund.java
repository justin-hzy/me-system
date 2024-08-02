package com.me.nascent.modules.reorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("")
public class ReFund {

    @TableField("address")
    private String address;

    @TableField("applyTime")
    private String applyTime;

    @TableField("companyName")
    private String companyName;

    @TableField("goodsLibId")
    private int goodsLibId;

    @TableField("id")
    private int id;

    @TableField("mobile")
    private String mobile;

    @TableField("modified")
    private String modified;

    @TableField("number")
    private int number;

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
    private int platform;

    @TableField("price")
    private int price;

    @TableField("reasonStr")
    private String reasonStr;

    @TableField("refundDesc")
    private String refundDesc;

    @TableField("refundFee")
    private int refundFee;

    @TableField("refundGenre")
    private int refundGenre;

    @TableField("refundName")
    private String refundName;

    @TableField("refundNum")
    private int refundNum;

    @TableField("refundRemindTimeout")
    private String refundRemindTimeout;

    @TableField("refundShopId")
    private int refundShopId;

    @TableField("refundStatus")
    private String refundStatus;

    @TableField("refundType")
    private int refundType;

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
    private int totalFee;

    @TableField("updateTime")
    private String updateTime;
}
