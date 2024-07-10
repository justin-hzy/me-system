package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "store_af_sales_exception ")
@ApiModel(description = "仓库售后异常等级表实体类")
public class StoreAfSalesException {

    @ApiModelProperty("id")
    @TableField("id")
    private String id;

    @ApiModelProperty("创建人")
    @TableField("creator")
    private String creator;

    @ApiModelProperty("子订单")
    @TableField("sub_order")
    private String subOrder;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private String createTime;

    @ApiModelProperty("修改时间")
    @TableField("modify_time")
    private String modifyTime;

    @ApiModelProperty("任务状态")
    @TableField("task_status")
    private String taskStatus;

    @ApiModelProperty("仓库")
    @TableField("warehouse")
    private String warehouse;

    @ApiModelProperty("任务完结人")
    @TableField("task_finisher")
    private String taskFinisher;

    @ApiModelProperty("运单号")
    @TableField("waybill_number")
    private String waybillNumber;

    @ApiModelProperty("补偿金额")
    @TableField("compensation_amount")
    private BigDecimal compensationAmount;

    @ApiModelProperty("任务完结时间")
    @TableField("task_finish_time")
    private String taskFinishTime;

    @ApiModelProperty("实付金额")
    @TableField("actual_payment_amount")
    private BigDecimal actualPaymentAmount;

    @ApiModelProperty("问题类型")
    @TableField("issue_type")
    private String issueType;

    @ApiModelProperty("处理结果")
    @TableField("processing_result")
    private String processingResult;

    @ApiModelProperty("描述说明")
    @TableField("description")
    private String description;

    @ApiModelProperty("商品信息表")
    @TableField("product_info_table")
    private String productInfoTable;

    @ApiModelProperty("图片")
    @TableField("image")
    private String image;

    @ApiModelProperty("店铺")
    @TableField("shop")
    private String shop;

    @ApiModelProperty("订单号")
    @TableField("order_number")
    private String orderNumber;

    @ApiModelProperty("买家昵称")
    @TableField("buyer_nickname")
    private String buyerNickname;

    @ApiModelProperty("收货姓名")
    @TableField("recipient_name")
    private String recipientName;

    @ApiModelProperty("收货手机")
    @TableField("recipient_phone")
    private String recipientPhone;

    @ApiModelProperty("收货地址")
    @TableField("shipping_address")
    private String shippingAddress;

    @ApiModelProperty("客诉商品批次号")
    @TableField("complaint_product_batch_number")
    private String complaintProductBatchNumber;

    @ApiModelProperty("订单备注")
    @TableField("order_note")
    private String orderNote;

    @TableField("is_delete")
    private String isDelete;
}
