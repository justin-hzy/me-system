package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ec_return_order")
public class EcReOrder {

    @TableField("seller_account")
    private String sellerAccount;

    @TableField("buyer_id")
    private String buyerId;

    @TableField("buyer_name")
    private String buyerName;

    @TableField("email")
    private String email;

    @TableField("order_id")
    private String orderId;

    @TableField("order_status")
    private String orderStatus;

    @TableField("refrence_no_warehouse")
    private String refrenceNoWarehouse;

    @TableField("ro_code")
    private String roCode;

    @TableField("ro_status")
    private String roStatus;

    @TableField("product_sku")
    private String productSku;

    @TableField("product_title")
    private String productTitle;

    @TableField("qty")
    private String qty;

    @TableField("exception_title")
    private String exceptionTitle;

    @TableField("process_type")
    private String processType;

    @TableField("tracking_no")
    private String trackingNo;

    @TableField("out_warehouse")
    private String outWarehouse;

    @TableField("in_warehouse")
    private String inWarehouse;

    @TableField("location_code")
    private String locationCode;

    @TableField("create_date")
    private String createDate;

    @TableField("complete_date")
    private String completeDate;

    @TableField("confirm_date")
    private String confirmDate;

    @TableField("create_user")
    private String createUser;

    @TableField("note")
    private String note;

    @TableField("reason")
    private String reason;

    @TableField("amount_total")
    private String amountTotal;

    @TableField("currency")
    private String currency;

    @TableField("ro_type")
    private String roType;

    /*状态  90 失败 80 未推送 99 成功 */
    @TableField("status")
    private String status;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    /*0 未删除 1 已删除*/
    @TableField("is_delete")
    private String isDelete;


}
