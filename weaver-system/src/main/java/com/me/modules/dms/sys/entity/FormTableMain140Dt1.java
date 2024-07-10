package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_140_dt1")
public class FormTableMain140Dt1 {

    @TableField("id")
    private int id;
    
    @TableField("hptxm")
    private String hptxm;

    @TableField("hpmc")
    private String hpmc;

    @TableField("gg")
    private String gg;

    @TableField("dw")
    private String dw;

    @TableField("lsj")
    private Double lsj;

    @TableField("zk")
    private Double zk;

    @TableField("ddl")
    private Integer ddl;

    @TableField("fhl")
    private Integer fhl;

    @TableField("ddje")
    private Double ddje;

    @TableField("zlsje")
    private Double zlsje;

    @TableField("zfhje")
    private Double zfhje;

    @TableField("kykc")
    private Integer kykc;

    @TableField("lx")
    private Integer lx;

    @TableField("shsl")
    private Integer shsl;

    @TableField("zshje")
    private Double zshje;

    @TableField("ddjecbj")
    private Double ddjecbj;

    @TableField("sxjzt")
    private String sxjzt;

    @TableField("fhzt")
    private Integer fhzt;

    @TableField("qdskbs")
    private String qdskbs;

    @TableField("bjkc")
    private Integer bjkc;
}
