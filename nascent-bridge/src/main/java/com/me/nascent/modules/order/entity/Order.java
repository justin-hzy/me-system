package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("nascent_order")
public class Order {

    @TableField("sysTradeId")
    private Long sysTradeId;

    @TableField("outOrderId")
    private String outOrderId;

    @TableField("title")
    private String title;

    @TableField("shopId")
    private Long shopId;

    @TableField("shopName")
    private String shopName;

    @TableField("orderPrice")
    private String orderPrice;

    @TableField("orderNum")
    private BigDecimal orderNum;

    @TableField("orderPayment")
    private BigDecimal orderPayment;

    @TableField("orderDivideFee")
    private BigDecimal orderDivideFee;

    @TableField("picPath")
    private String picPath;

    @TableField("skuId")
    private String skuId;

    @TableField("skuOuterId")
    private String skuOuterId;

    @TableField("skuProperties")
    private String skuProperties;

    @TableField("orderStatus")
    private String orderStatus;

    @TableField("refundId")
    private String refundId;

    @TableField("orderRefundStatus")
    private String orderRefundStatus;

    @TableField("orderRefundNum")
    private BigDecimal orderRefundNum;

    @TableField("orderRefundFee")
    private BigDecimal orderRefundFee;

    @TableField("orderShippingType")
    private String orderShippingType;

    @TableField("orderConsignTime")
    private Date orderConsignTime;

    @TableField("orderLogisticsCompany")
    private String orderLogisticsCompany;

    @TableField("orderLogisticsNo")
    private String orderLogisticsNo;

    @TableField("outItemId")
    private String outItemId;

    @TableField("storeId")
    private Long storeId;

    @TableField("refundTimes")
    private Integer refundTimes;

    @TableField("goodsLibId")
    private Long goodsLibId;

    @TableField("warehouseId")
    private Long warehouseId;

    @TableField("outerId")
    private String outerId;

    @TableField("orderDiscountFee")
    private BigDecimal orderDiscountFee;

    @TableField("orderAdjustFee")
    private BigDecimal orderAdjustFee;

    @TableField("orderTotalFee")
    private BigDecimal orderTotalFee;

    @TableField("orderPayTime")
    private Date orderPayTime;

    @TableField("orderCreated")
    private Date orderCreated;

    @TableField("orderFrom")
    private String orderFrom;

    @TableField("orderExclusiveGuideId")
    private Integer orderExclusiveGuideId;

    @TableField("orderHandleGuideId")
    private Integer orderHandleGuideId;

    @TableField("orderExclusiveShopId")
    private Long orderExclusiveShopId;

    @TableField("orderHandleShopId")
    private Long orderHandleShopId;
}
