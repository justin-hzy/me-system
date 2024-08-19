package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_retrade_order")
public class QiMenReTradeOrder {

    @TableField("consignTime")
    private String consignTime; // 退货时间

    @TableField("adjustFee")
    private String adjustFee; // 退款金额

    @TableField("num")
    private String num; // 商品数量

    @TableField("shippingType")
    private String shippingType; // 创建交易时的物流方式

    @TableField("numIid")
    private String numIid; // 申请退款的商品字符串编号

    @TableField("oid")
    private String oid; // 子单ID

    @TableField("title")
    private String title; // 商品标题

    @TableField("price")
    private String price; // 商品价格

    @TableField("totalFee")
    private String totalFee; // 商品金额

    @TableField("refundStatus")
    private String refundStatus; // 退款状态

    @TableField("invoiceNo")
    private String invoiceNo; // 退货单号

    @TableField("refundId")
    private String refundId; // 退款ID

    @TableField("outerIid")
    private String outerIid; // 商家外部编码

    @TableField("logisticsCompany")
    private String logisticsCompany; // 退款发货的快递公司名称

    @TableField("expandCardExpandPriceUsedSuborder")
    private String expandCardExpandPriceUsedSuborder; // 购物金权益金

    @TableField("expandCardBasicPriceUsedSuborder")
    private String expandCardBasicPriceUsedSuborder; // 购物金本金

    @TableField("status")
    private String status; // 订单状态
}
