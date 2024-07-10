package com.me.modules.k3.retsale.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
@TableName("K3_RET_SALEITEM")
public class K3RetSaleItem {

    @TableField("ID")
    private Long id;

    @TableField("FENTITYID")
    private String fentityId;

    @TableField("FMATERIALID")
    private String fmaterialId;

    @TableField("FREALQTY")
    private String frealQty;

    @TableField("FSTOCKID")
    private String fstockId;

    @TableField("FENTRYTAXRATE")
    private Integer fentryTaxRate;

    @TableField("FTAXPRICE")
    private String ftaxPrice;

    @TableField("FRETURNTYPE")
    private String freturnType;

    @TableField("FDISCOUNTRATE")
    private Integer fdiscountRate;

    @TableField("F_ZJM_AMOUNT_JFXF")
    private Integer fzjmAmountJfxf;

    @TableField("F_ZJM_AMOUNT_CSJF")
    private Integer fzjmAmountCsjf;

    @TableField("F_ZJM_AMOUNT_GWQ")
    private String fzjmAmountGwq;

    @TableField("F_ZJM_AMOUNT_SCQ")
    private String fzjmAmountScq;

    @TableField("EDIFLAG")
    private String ediFlag;

    @TableField("WRITEDATE")
    private String writeDate;

    @TableField("RESULTS")
    private String results;

    @TableField("BOS_ID")
    private Long bosId;

    @TableField("FDISCOUNT")
    private Integer fdiscount;

    @TableField("FISFREE")
    private String fisFree;

    @TableField("F_RLIZ_PP")
    private String frlizPp;

    @TableField("F_RLIZ_PT")
    private String frlizPt;

    @TableField("F_RLIZ_QD")
    private String frlizQd;

    @TableField("FORDERNO")
    private String forderNo;

    @TableField("F_RLIZ_JDCGDH")
    private String frlizJdcgdh;

    @TableField("F_RLIZ_WPHXSDDH")
    private String frlizWphxsddh;

    @TableField("F_DSG_SRCOID1")
    private String fdsgSrcoid1;
}
