package com.me.modules.wangdianstockout.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("WANGDIAN_STOCKOUT_LIST")
public class WangDianStockOutList {
    @TableField("ID")
    private Integer id;

    @TableField("WANGDIANSTOCKOUTID")
    private String wangdianStockoutId;

    @TableField("STOCKOUTORDERDETAILID")
    private String stockOutOrderDetailId;

    @TableField("RECID")
    private String recId;

    @TableField("STOCKOUTID")
    private String stockoutId;

    @TableField("SPECNO")
    private String specNo;

    @TableField("SELLPRICE")
    private String sellPrice;

    @TableField("GOODSCOUNT")
    private String goodsCount;

    @TableField("BRANDNO")
    private String brandNo;

    @TableField("BRANDNAME")
    private String brandName;

    @TableField("GOODSTYPE")
    private String goodsType;

    @TableField("GIFTTYPE")
    private String giftType;

    @TableField("GOODSNAME")
    private String goodsName;

    @TableField("GOODSNO")
    private String goodsNo;

    @TableField("SPECNAME")
    private String specName;

    @TableField("SPECCODE")
    private String specCode;

    @TableField("SUITENO")
    private String suiteNo;

    @TableField("COSTPRICE")
    private String costPrice;

    @TableField("TOTALAMOUNT")
    private String totalAmount;

    @TableField("GOODSID")
    private String goodsId;

    @TableField("SPECID")
    private String specId;

    @TableField("WEIGHT")
    private String weight;

    @TableField("REMARK")
    private String remark;

    @TableField("PAID")
    private String paid;

    @TableField("REFUNDSTATUS")
    private String refundStatus;

    @TableField("MARKETPRICE")
    private String marketPrice;

    @TableField("DISCOUNT")
    private String discount;

    @TableField("SHAREAMOUNT")
    private String shareAmount;

    @TableField("TAXRATE")
    private String taxRate;

    @TableField("BARCODE")
    private String barcode;

    @TableField("UNITNAME")
    private String unitName;

    @TableField("SALEORDERID")
    private String saleOrderId;

    @TableField("SHAREPOST")
    private String sharePost;

    @TableField("SRCOID")
    private String srcOid;

    @TableField("SRCTID")
    private String srcTid;

    @TableField("FROMMASK")
    private String fromMask;

    @TableField("SRCORDERTYPE")
    private String srcOrderType;

    @TableField("SRCORDERDETAILID")
    private String srcOrderDetailId;

    @TableField("BASEUNITID")
    private String baseUnitId;

    @TableField("UNITID")
    private String unitId;

    @TableField("UNITRATIO")
    private String unitRatio;

    @TableField("NUM")
    private String num;

    @TableField("NUM2")
    private String num2;

    @TableField("PRICE")
    private String price;

    @TableField("BATCHID")
    private String batchId;

    @TableField("ISEXAMINED")
    private String isExamined;

    @TableField("ISPACKAGE")
    private String isPackage;

    @TableField("ISZEROCOST")
    private String isZeroCost;

    @TableField("SCANTYPE")
    private String scanType;

    @TableField("GOODPROP1")
    private String goodProp1;

    @TableField("GOODPROP2")
    private String goodProp2;

    @TableField("GOODPROP3")
    private String goodProp3;

    @TableField("GOODPROP4")
    private String goodProp4;

    @TableField("GOODPROP5")
    private String goodProp5;

    @TableField("GOODPROP6")
    private String goodProp6;

    @TableField("PROP1")
    private String prop1;

    @TableField("PROP2")
    private String prop2;

    @TableField("PROP3")
    private String prop3;

    @TableField("PROP4")
    private String prop4;

    @TableField("PROP5")
    private String prop5;

    @TableField("PROP6")
    private String prop6;

    @TableField("BATCHNO")
    private String batchNo;

    @TableField("BATCHREMARK")
    private String batchRemark;

    @TableField("EXPIREDATE")
    private String expireDate;


    @TableField("RESULTS")
    private String results;

    @TableField("LISTID")
    private String listId;

    @TableField("MODIFIED")
    private String modified;

    @TableField("CREATED")
    private String created;

    @TableField("POSITIONID")
    private String positionId;

    @TableField("PLATFORMID")
    private String platformId;




}
