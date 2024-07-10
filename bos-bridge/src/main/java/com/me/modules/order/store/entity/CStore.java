package com.me.modules.order.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName("c_store")
public class CStore {

    @TableField("ID")
    private Integer id;

    @TableField("C_CUSTOMER_ID")
    private Integer cCustomerId;

    @TableField("is_special")
    private String isSpecial;

    @TableField("special_discount")
    private BigDecimal specialDiscount;

    /*@TableField("AD_CLIENT_ID")
    private Integer AD_CLIENT_ID;
    @TableField("AD_ORG_ID")
    private Integer AD_ORG_ID;
    @TableField("ISACTIVE")
    private char ISACTIVE;
    @TableField("MODIFIERID")
    private Integer MODIFIERID;
    @TableField("CREATIONDATE")
    private Date CREATIONDATE;
    @TableField("MODIFIEDDATE")
    private Date MODIFIEDDATE;
    @TableField("OWNERID")
    private Integer OWNERID;
    @TableField("NAME")
    private String NAME;
    @TableField("DESCRIPTION")
    private String DESCRIPTION;
    @TableField("C_AREA_ID")
    private Integer C_AREA_ID;
    @TableField("LOCKCASH")
    private BigDecimal LOCKCASH;
    @TableField("ADDRESS")
    private String ADDRESS;
    @TableField("PHONE")
    private String PHONE;
    @TableField("FAX")
    private String FAX;
    @TableField("CONTACTOR_ID")
    private Integer CONTACTOR_ID;
    @TableField("MONTHFEE")
    private BigDecimal MONTHFEE;
    @TableField("ISSTOP")
    private char ISSTOP;
    @TableField("RENTBEGIN")
    private Integer RENTBEGIN;
    @TableField("RENTEND")
    private Integer RENTEND;
    @TableField("PROPORTION")
    private Integer PROPORTION;
    @TableField("EMPCNT")
    private Integer EMPCNT;
    @TableField("CHECKDATE")
    private Integer CHECKDATE;
    @TableField("ISCENTER")
    private char ISCENTER;
    @TableField("ISRETAIL")
    private char ISRETAIL;
    @TableField("MOBIL")
    private String MOBIL;
    @TableField("SNAME")
    private String SNAME;
    @TableField("POSTCAL")
    private String POSTCAL;
    @TableField("CALCULATION")
    private char CALCULATION;

    @TableField("C_CUSTOMERUP_ID")
    private Integer C_CUSTOMERUP_ID;
    @TableField("C_PRICEAREA_ID")
    private Integer C_PRICEAREA_ID;
    @TableField("ISFAIRORIG")
    private char ISFAIRORIG;
    @TableField("AREAMNG_ID")
    private Integer AREAMNG_ID;
    @TableField("LIMITQTY")
    private Integer LIMITQTY;
    @TableField("LIMITAMT")
    private BigDecimal LIMITAMT;
    @TableField("LIMITMO")
    private String LIMITMO;
    @TableField("MARKDIS")
    private BigDecimal MARKDIS;
    @TableField("DATEBLOCK")
    private Integer DATEBLOCK;
    @TableField("C_STORETYPE_JZ_ID")
    private Integer C_STORETYPE_JZ_ID;
    @TableField("IMGURL1")
    private String IMGURL1;
    @TableField("IMGURL2")
    private String IMGURL2;
    @TableField("IMGURL3")
    private String IMGURL3;
    @TableField("IMGURL4")
    private String IMGURL4;
    @TableField("IMGURL5")
    private String IMGURL5;
    @TableField("BIGAREAMNG_ID")
    private Integer BIGAREAMNG_ID;
    @TableField("C_PROVINCE_ID")
    private Integer C_PROVINCE_ID;
    @TableField("C_CITY_ID")
    private Integer C_CITY_ID;
    @TableField("STORESIGN")
    private Integer STORESIGN;
    @TableField("C_STORETYPE")
    private String C_STORETYPE;
    @TableField("REMARK")
    private String REMARK;
    @TableField("CODE")
    private String CODE;*/
}
