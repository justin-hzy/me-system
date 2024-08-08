package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@TableName("trade")
public class Trade {

    @TableField("id")
    private Long id;

    @TableField("availableConfirmFee")
    private BigDecimal availableConfirmFee;

    @TableField("discountFee")
    private BigDecimal discountFee;

    @TableField("receiverPhone")
    private String receiverPhone;

    @TableField("stepPaidFee")
    private BigDecimal stepPaidFee;

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
    private Date created;

    @TableField("modifyTime")
    private Date modifyTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("totalFee")
    private BigDecimal totalFee;

    @TableField("num")
    private BigDecimal num;

    @TableField("payTime")
    private Date payTime;

    @TableField("payType")
    private Integer payType;

    @TableField("payment")
    private BigDecimal payment;

    @TableField("postFee")
    private BigDecimal postFee;

    @TableField("adjustFee")
    private BigDecimal adjustFee;

    @TableField("svPayment")
    private BigDecimal svPayment;

    @TableField("realPointFee")
    private BigDecimal realPointFee;

    @TableField("consignTime")
    private Date consignTime;

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

    @TableField("sgShareShopId")
    private Long sgShareShopId;

    @TableField("sgShareGuideId")
    private Long sgShareGuideId;

    @TableField("extJson")
    private String extJson;

    @TableField("timeoutActionTime")
    private Date timeoutActionTime;

    @TableField("endTime")
    private Date endTime;

    @TableField("ladingCode")
    private String ladingCode;

}
