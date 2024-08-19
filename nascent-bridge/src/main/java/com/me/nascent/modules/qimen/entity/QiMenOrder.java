package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_order")
public class QiMenOrder {

    @TableField("oid")
    private String oid; // 子单ID

    @TableField("sellerRate")
    private String sellerRate; // 卖家是否已评价

    @TableField("consignTime")
    private String consignTime; // 商品发货时间

    @TableField("timeoutActionTime")
    private String timeoutActionTime; // 商品超时时间

    @TableField("customization")
    private String customization; // 定制信息

    @TableField("adjustFee")
    private String adjustFee; // 商品手工调价

    @TableField("num")
    private String num; // 商品数量

    @TableField("shippingType")
    private String shippingType; // 创建交易时的物流方式

    @TableField("numIid")
    private String numIid; // 商品数字ID

    @TableField("snapshotUrl")
    private String snapshotUrl; // 交易快照地址

    @TableField("title")
    private String title; // 商品标题

    @TableField("buyerRate")
    private String buyerRate; // 买家是否已评价

    @TableField("discountFee")
    private String discountFee; // 优惠金额

    @TableField("price")
    private String price; // 商品价格

    @TableField("totalFee")
    private String totalFee; // 商品金额

    @TableField("payment")
    private String payment; // 商品实付金额

    @TableField("isOversold")
    private String isOversold; // 是否超卖

    @TableField("outerSkuId")
    private String outerSkuId; // 外部SKUID

    @TableField("partMjzDiscount")
    private String partMjzDiscount; // 优惠分摊

    @TableField("refundStatus")
    private String refundStatus; // 退款状态

    @TableField("bindOids")
    private String bindOids; // bind_oid字段的升级

    @TableField("invoiceNo")
    private String invoiceNo; // 子订单所在包裹的运单号

    @TableField("endTime")
    private String endTime; // 商品交易完成时间

    @TableField("picPath")
    private String picPath; // 服务图片地址

    @TableField("skuId")
    private String skuId; // 商品的最小库存单位Sku的id

    @TableField("skuPropertiesName")
    private String skuPropertiesName; // SKU的值

    @TableField("refundId")
    private String refundId; // 最近退款ID

    @TableField("divideOrderFee")
    private String divideOrderFee; // 分摊之后的实付金额

    @TableField("orderFrom")
    private String orderFrom; // 子订单来源

    @TableField("outerIid")
    private String outerIid; // 商家外部编码

    @TableField("logisticsCompany")
    private String logisticsCompany; // 子订单发货的快递公司名称

    @TableField("expandCardExpandPriceUsedSuborder")
    private String expandCardExpandPriceUsedSuborder; // 购物金核销子订单权益金分摊金额

    @TableField("expandCardBasicPriceUsedSuborder")
    private String expandCardBasicPriceUsedSuborder; // 购物金核销子订单本金分摊金额

    @TableField("status")
    private String status; // 订单状态

    @TableField("cid")
    private String cid; // 交易商品对应的类目ID

    @TableField("itemMealId")
    private String itemMealId; // 套餐ID
}
