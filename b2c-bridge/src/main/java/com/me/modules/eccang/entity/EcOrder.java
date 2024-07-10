package com.me.modules.eccang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("EcOrder")
public class EcOrder {

    @TableField("platform")
    private String platform;

    @TableField("referenceNo")
    private String referenceNo;

    @TableField("orderCode")
    private String orderCode;

    @TableField("smCode")
    private String smCode;

    @TableField("addTime")
    private String addTime;

    @TableField("orderPayDate")
    private String orderPayDate;

    @TableField("warehouseCode")
    private String warehouseCode;

    @TableField("warehouseName")
    private String warehouseName;

    @TableField("serviceProviderCode")
    private String serviceProviderCode;

    @TableField("serviceProviderName")
    private String serviceProviderName;

    @TableField("channelCode")
    private String channelCode;

    @TableField("channelName")
    private String channelName;

    @TableField("sellerId")
    private String sellerId;

    @TableField("ebayTotal")
    private String ebayTotal;

    @TableField("currencyCode")
    private String currencyCode;

    @TableField("taxNumber")
    private String taxNumber;

    @TableField("ioss")
    private String ioss;

    @TableField("eori")
    private String eori;

    @TableField("cpf")
    private String cpf;

    @TableField("orderStatus")
    private Integer orderStatus;

    /*0 未同步 1 已同步 2 同步失败*/
    @TableField("status")
    private String status;

    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("isDelete")
    private String isDelete;

}
