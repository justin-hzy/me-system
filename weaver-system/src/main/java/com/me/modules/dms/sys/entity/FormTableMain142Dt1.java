package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("formtable_main_142_dt1")
public class FormTableMain142Dt1 {

    @TableField("id")
    private String id;

    @TableField("mainid")
    private String mainid;

    @TableField("spmc")
    private String spmc;

    @TableField("xslb")
    private String xslb;

    @TableField("gg")
    private String gg;

    @TableField("bzj")
    private BigDecimal bzj;

    @TableField("dbsl")
    private String dbsl;

    @TableField("cksl")
    private BigDecimal cksl;

    @TableField("ckbzje")
    private BigDecimal ckbzje;

    @TableField("bzje")
    private String bzje;

    @TableField("kdbsl")
    private String kdbsl;

    @TableField("rksl")
    private BigDecimal rksl;

    @TableField("rkbzje")
    private String rkbzje;

    @TableField("bz")
    private String bz;

    @TableField("xg")
    private String xg;

    @TableField("xq")
    private String xq;

    @TableField("pc")
    private String pc;

    @TableField("hpbh")
    private String hpbh;

    @TableField("hptxmtemp")
    private String hptxmtemp;

    @TableField("hptxm")
    private String hptxm;

    @TableField("zxs")
    private String zxs;

    @TableField("sxjzt")
    private String sxjzt;

    @TableField("bjkc")
    private String bjkc;

    @TableField("ckzt")
    private String ckzt;
}
