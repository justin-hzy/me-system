package com.me.modules.wangdianstockin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("WANGDIAN_STOCKIN_LIST")
public class WangDianStockInList {

    
    @TableField("ID")
    private String id;

    @TableField("WANGDIANSTOCKINID")
    private String wangdianStockInId;

    @TableField("RECID")
    private String recId;

    @TableField("STOCKINID")
    private String stockInId;

    @TableField("SRCORDERDETAILID")
    private String srcOrderDetailId;

    @TableField("SPECNO")
    private String specNo;

    @TableField("GOODSCOUNT")
    private String goodsCount;

    @TableField("DISCOUNT")
    private String discount;

    @TableField("COSTPRICE")
    private String costPrice;

    @TableField("PRODUCTIONDATE")
    private String productionDate;

    @TableField("VALIDITYDAYS")
    private String validityDays;

    @TableField("EXPIREDATE")
    private String expireDate;

    @TableField("SRCPRICE")
    private String srcPrice;

    @TableField("TAXPRICE")
    private String taxPrice;

    @TableField("TAXAMOUNT")
    private String taxAmount;

    @TableField("TAX")
    private String tax;

    @TableField("TOTALCOST")
    private String totalCost;

    @TableField("REMARK")
    private String remark;

    @TableField("ADJUSTNUM")
    private String adjustNum;

    @TableField("ADJUSTPRICE")
    private String adjustPrice;

    @TableField("RIGHTNUM")
    private String rightNum;

    @TableField("RIGHTPRICE")
    private String rightPrice;

    @TableField("NUM")
    private String num;

    @TableField("NUM2")
    private String num2;

    @TableField("RIGHTCOST")
    private String rightCost;

    @TableField("BRANDNO")
    private String brandNo;

    @TableField("BRANDNAME")
    private String brandName;

    @TableField("GOODSNAME")
    private String goodsName;

    @TableField("PROP2")
    private String prop2;

    @TableField("GOODSNO")
    private String goodsNo;

    @TableField("SPECNAME")
    private String specName;

    @TableField("SPECCODE")
    private String specCode;

    @TableField("GOODSUNIT")
    private String goodsUnit;

    @TableField("BATCHNO")
    private String batchNo;

    @TableField("BATCHREMARK")
    private String batchRemark;

    @TableField("POSITIONNO")
    private String positionNo;

    @TableField("TID")
    private String tid;

    @TableField("OID")
    private String oid;

    @TableField("SRCORDERTYPE")
    private String srcOrderType;

    @TableField("ORGSTOCKINDETAILID")
    private String orgStockInDetailId;

    @TableField("SPECID")
    private String specId;

    @TableField("BATCHID")
    private String batchId;

    @TableField("POSITIONID")
    private String positionId;

    @TableField("UNITID")
    private String unitId;

    @TableField("BASEUNITID")
    private String baseUnitId;

    @TableField("UNITRATIO")
    private String unitRatio;

    @TableField("EXPECTNUM")
    private String expectNum;

    @TableField("COSTPRICE2")
    private String costPrice2;

    @TableField("SHAREPOSTCOST")
    private String sharePostCost;

    @TableField("SHAREPOSTTOTAL")
    private String sharePostTotal;

    @TableField("MODIFIED")
    private String modified;

    @TableField("CREATED")
    private String created;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("BARCODE")
    private String barcode;

    @TableField("RESULTS")
    private String results;

    @TableField("LISTID")
    private String listId;
}
