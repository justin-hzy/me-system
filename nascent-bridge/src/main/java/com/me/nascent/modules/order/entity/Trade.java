package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("nascent_trade")
public class Trade {

    @TableField("id")
    private Long id;

    @TableField("availableConfirmFee")
    private Integer availableConfirmFee;

    @TableField("discountFee")
    private Integer discountFee;

    @TableField("receiverPhone")
    private String receiverPhone;

    @TableField("stepPaidFee")
    private Integer stepPaidFee;

    @TableField("outTradeId")
    private String outTradeId;

    @TableField("sysTradeId")
    private Long sysTradeId;

    @TableField("platform")
    private Integer platform;

    @TableField("shopId")
    private Long shopId;

    @TableField("shopName")
    private String shopName;

    @TableField("sysCustomerId")
    private Long sysCustomerId;

    @TableField("outNick")
    private String outNick;

    @TableField("outAlias")
    private String outAlias;

    @TableField("customerName")
    private String customerName;

    @TableField("customerMobile")
    private String customerMobile;

    @TableField("activityNo")
    private String activityNo;

    @TableField("tradeFrom")
    private String tradeFrom;

    @TableField("tradeStatus")
    private String tradeStatus;

    @TableField("tradeType")
    private String tradeType;

    @TableField("created")
    private String created;

    @TableField("modifyTime")
    private String modifyTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("totalFee")
    private Integer totalFee;

    @TableField("num")
    private Integer num;

    @TableField("payTime")
    private String payTime;

    @TableField("payType")
    private Integer payType;

    @TableField("payment")
    private Integer payment;

    @TableField("postFee")
    private Integer postFee;

    @TableField("adjustFee")
    private Integer adjustFee;

    @TableField("svPayment")
    private Integer svPayment;

    @TableField("realPointFee")
    private Integer realPointFee;

    @TableField("consignTime")
    private String consignTime;

    @TableField("outCompanyName")
    private String outCompanyName;

    @TableField("shippingType")
    private String shippingType;

    @TableField("outSid")
    private String outSid;

    @TableField("outState")
    private Integer outState;

    @TableField("receiverMobile")
    private String receiverMobile;

    @TableField("receiverName")
    private String receiverName;

    @TableField("receiverProvince")
    private String receiverProvince;

    @TableField("receiverZip")
    private String receiverZip;

    @TableField("receiverCity")
    private String receiverCity;

    @TableField("receiverDistrict")
    private String receiverDistrict;

    @TableField("receiverAddress")
    private String receiverAddress;

    @TableField("buyerMemo")
    private String buyerMemo;

    @TableField("buyerMessage")
    private String buyerMessage;

    @TableField("sellerMemo")
    private String sellerMemo;

    @TableField("remarkSign")
    private Integer remarkSign;

    @TableField("tradeMemo")
    private String tradeMemo;

    @TableField("buyerRate")
    private Integer buyerRate;

    @TableField("isAllRefunding")
    private Integer isAllRefunding;

    @TableField("sgFinishGuideId")
    private Long sgFinishGuideId;

    @TableField("sgFinishShopId")
    private Long sgFinishShopId;

    @TableField("sgHandleGuideId")
    private Long sgHandleGuideId;

    @TableField("sgHandleShopId")
    private Long sgHandleShopId;

    @TableField("sgExclusiveGuideId")
    private Long sgExclusiveGuideId;

    @TableField("sgExclusiveShopId")
    private Long sgExclusiveShopId;

    @TableField("orderPayTime")
    private String orderPayTime;

    @TableField("sgShareShopId")
    private Long sgShareShopId;

    @TableField("sgShareGuideId")
    private Long sgShareGuideId;

    @TableField("extJson")
    private String extJson;

    @TableField("timeoutActionTime")
    private String timeoutActionTime;

    @TableField("endTime")
    private String endTime;

    @TableField("ladingCode")
    private String ladingCode;

}
