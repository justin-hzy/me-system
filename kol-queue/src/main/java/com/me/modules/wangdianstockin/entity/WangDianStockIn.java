package com.me.modules.wangdianstockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("WANGDIAN_STOCKIN")
public class WangDianStockIn {
    
    @TableField("ID")
    private Long id;

    @TableField("STOCKINID")
    private String stockInId;

    @TableField("ORDERNO")
    private String orderNo;

    @TableField("WAREHOUSENO")
    private String warehouseNo;

    @TableField("WAREHOUSENAME")
    private String warehouseName;

    @TableField("SHOPNO")
    private String shopNo;

    @TableField("SHOPREMARK")
    private String shopRemark;

    @TableField("SRCORDERNO")
    private String srcOrderNo;

    @TableField("PROCESSSTATUS")
    private String processStatus;

    @TableField("STATUS")
    private String status;

    @TableField("STOCKINTIME")
    private String stockInTime;

    @TableField("CREATEDTIME")
    private String createdTime;

    @TableField("REMARK")
    private String remark;

    @TableField("TRADENO")
    private String tradeNo;

    @TableField("TRADETYPE")
    private String tradeType;

    @TableField("ORDERTYPE")
    private String orderType;

    @TableField("ORDERTYPENAME")
    private String orderTypeName;

    @TableField("GOODSCOUNT")
    private String goodsCount;

    @TableField("GOODSAMOUNT")
    private String goodsAmount;

    @TableField("ACTUALREFUNDAMOUNT")
    private String actualRefundAmount;

    @TableField("TOTALPRICE")
    private String totalPrice;

    @TableField("DISCOUNT")
    private String discount;

    @TableField("TAXAMOUNT")
    private String taxAmount;

    @TableField("POSTFEE")
    private String postFee;

    @TableField("OTHERFEE")
    private String otherFee;

    @TableField("ADJUSTPRICE")
    private String adjustPrice;

    @TableField("RIGHTPRICE")
    private String rightPrice;

    @TableField("LOGISTICSTYPE")
    private String logisticsType;

    @TableField("LOGISTICSNO")
    private String logisticsNo;

    @TableField("LOGISTICSNAME")
    private String logisticsName;

    @TableField("LOGISTICSCODE")
    private String logisticsCode;

    @TableField("REFUNDNO")
    private String refundNo;

    @TableField("CHECKTIME")
    private String checkTime;

    @TableField("CUSTOMERNO")
    private String customerNo;

    @TableField("CUSTOMERNAME")
    private String customerName;

    @TableField("NICKNAME")
    private String nickname;

    @TableField("SHOPNAME")
    private String shopName;

    @TableField("STOCKINREASON")
    private String stockInReason;

    @TableField("FLAGNAME")
    private String flagName;

    @TableField("PLATFORMID")
    private String platformId;

    @TableField("TID")
    private String tid;

    @TableField("STOCKINOPERATORNAME")
    private String stockInOperatorName;

    @TableField("CHECKOPERATORNAME")
    private String checkOperatorName;

    @TableField("REFUNDREMARK")
    private String refundRemark;

    @TableField("REFUNDOPERATORNAME")
    private String refundOperatorName;

    @TableField("STOCKINNO")
    private String stockInNo;

    @TableField("WMSOUTERNO")
    private String wmsOuterNo;

    @TableField("WMSSTATUS")
    private String wmsStatus;

    @TableField("WMSRESULT")
    private String wmsResult;

    @TableField("WAREHOUSEID")
    private String warehouseId;

    @TableField("SRCORDERTYPE")
    private String srcOrderType;

    @TableField("SRCORDERID")
    private String srcOrderId;

    @TableField("REASONID")
    private String reasonId;

    @TableField("FASTATUS")
    private String faStatus;

    @TableField("LOGISTICSID")
    private String logisticsId;

    @TableField("POSTSHARETYPE")
    private String postShareType;

    @TableField("OPERATORID")
    private String operatorId;

    @TableField("CHECKOPERATORID")
    private String checkOperatorId;

    @TableField("GOODSTYPECOUNT")
    private String goodsTypeCount;

    @TableField("ADJUSTNUM")
    private BigDecimal adjustNum;

    @TableField("NOTECOUNT")
    private String noteCount;

    @TableField("FLAGID")
    private String flagId;

    @TableField("MODIFIED")
    private String modified;

    @TableField("RESULTS")
    private String results;

}
