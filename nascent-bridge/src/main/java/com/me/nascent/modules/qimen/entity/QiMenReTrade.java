package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_retrade")
public class QiMenReTrade {

    @TableField("snapshotUrl")
    private String snapshotUrl; // 支付宝交易号

    @TableField("tid")
    private String tid; // 订单号

    @TableField("modified")
    private String modified; // 退款变更时间

    @TableField("sellerMemo")
    private String sellerMemo; // 退款说明

    @TableField("created")
    private String created; // 退款申请时间

    @TableField("payTime")
    private String payTime; // 付款时间

    @TableField("status")
    private String status; // 货物状态

    @TableField("receiverCity")
    private String receiverCity; // 退款收货地址

    @TableField("buyerMemo")
    private String buyerMemo; // 退款原因

    @TableField("isPartConsign")
    private String isPartConsign; // 是否退货

    @TableField("sellerNick")
    private String sellerNick; // 卖家昵称
}
