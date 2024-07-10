package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "store_af_sales_exception_sub_order")
public class StoreAfSalesExceptionSubOrder {

    @ApiModelProperty("报表id")
    @TableField("id")
    private String id;

    @ApiModelProperty("子订单号")
    @TableField("sub_order_number")
    private String subOrderNumber;

    @ApiModelProperty("商品ID")
    @TableField("product_id")
    private String productId;

    @ApiModelProperty("商品标题")
    @TableField("product_title")
    private String productTitle;

    @ApiModelProperty("购买数")
    @TableField("purchase_quantity")
    private String purchaseQuantity;

    @ApiModelProperty("[子]实付金额")
    @TableField("actual_payment")
    private String actualPayment;

    @ApiModelProperty("SKU属性")
    @TableField("sku_properties")
    private String skuProperties;

    @ApiModelProperty("商品单价")
    @TableField("unit_price")
    private String unitPrice;

    @ApiModelProperty("SKUID")
    @TableField("sku_id")
    private String skuId;

    @ApiModelProperty("商家编码")
    @TableField("merchant_code")
    private String merchantCode;

    @ApiModelProperty("退款ID")
    @TableField("refund_id")
    private String refundId;

    @ApiModelProperty("退款状态")
    @TableField("refund_status")
    private String refundStatus;

    @ApiModelProperty("[子]发货快递")
    @TableField("sub_express_company")
    private String subExpressCompany;

    @ApiModelProperty("[子]运单号")
    @TableField("waybill_number")
    private String waybillNumber;

    @ApiModelProperty("退货单号")
    @TableField("return_order_number")
    private String returnOrderNumber;

    @ApiModelProperty("退货物流公司")
    @TableField("return_logistics_company")
    private String returnLogisticsCompany;

    @ApiModelProperty("退款原因")
    private String refundReason;

    @ApiModelProperty("商品编号")
    private String productNumber;

    @ApiModelProperty("物流公司")
    private String logisticsCompany;

    @ApiModelProperty("退款申请时间")
    private String refundApplicationTime;

    @ApiModelProperty("退款金额")
    private String refundAmount;

}
