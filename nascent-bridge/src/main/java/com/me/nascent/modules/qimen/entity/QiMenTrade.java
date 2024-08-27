package com.me.nascent.modules.qimen.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@TableName("qi_men_trade")
@Data
public class QiMenTrade {

    @TableField("consignTime")
    private String consignTime;

    @TableField("adjustFee")
    private String adjustFee;

    @TableField("num")
    private String num;

    @TableField("rxAuditStatus")
    private String rxAuditStatus;

    @TableField("availableConfirmFee")
    private String availableConfirmFee;

    @TableField("snapshotUrl")
    private String snapshotUrl;

    @TableField("type")
    private String type;

    @TableField("receivedPayment")
    private String receivedPayment;

    @TableField(value = "tid")
    private String tid;

    @TableField("stepPaidFee")
    private String stepPaidFee;

    @TableField("price")
    private String price;

    @TableField("expandCardBasicPriceUsed")
    private String expandCardBasicPriceUsed;

    @TableField("totalFee")
    private String totalFee;

    @TableField("modified")
    private String modified;

    @TableField("payment")
    private String payment;

    @TableField("canRate")
    private String canRate;

    @TableField("buyerMessage")
    private String buyerMessage;

    @TableField("sellerMemo")
    private String sellerMemo;

    @TableField("created")
    private String created;

    @TableField("payTime")
    private String payTime;

    @TableField("hasPostFee")
    private String hasPostFee;

    @TableField("couponFee")
    private String couponFee;

    /*@TableField(exist = false)
    private List<QiMenOrder> orders;*/

    /*@TableField(exist = false)
    private List<PromotionDetail> promotionDetails;*/

    @TableField("sellerCanRate")
    private String sellerCanRate;

    @TableField("status")
    private String status;

    @TableField("sellerRate")
    private String sellerRate;

    @TableField("postFee")
    private String postFee;

    @TableField("timeoutActionTime")
    private String timeoutActionTime;

    @TableField("expandCardExpandPriceUsed")
    private String expandCardExpandPriceUsed;

    @TableField("receiverCity")
    private String receiverCity;

    @TableField("shippingType")
    private String shippingType;

    @TableField("numIid")
    private String numIid;

    @TableField("title")
    private String title;

    @TableField("buyerRate")
    private String buyerRate;

    @TableField("expandCardExpandPrice")
    private String expandCardExpandPrice;

    @TableField("discountFee")
    private String discountFee;

    @TableField("receiverState")
    private String receiverState;

    @TableField("stepTradeStatus")
    private String stepTradeStatus;

    @TableField("buyerMemo")
    private String buyerMemo;

    @TableField("buyerOpenUid")
    private String buyerOpenUid;

    @TableField("tradeFrom")
    private String tradeFrom;

    @TableField("endTime")
    private String endTime;

    @TableField("picPath")
    private String picPath;

    @TableField("expandCardBasicPrice")
    private String expandCardBasicPrice;

    @TableField("sellerFlag")
    private String sellerFlag;

    @TableField("isPartConsign")
    private String isPartConsign;

    @TableField("sellerNick")
    private String sellerNick;
}