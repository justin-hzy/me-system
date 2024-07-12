package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_263")
public class FlReOrderSubMain {

    @TableField("id")
    private int id;

    @TableField("requestid")
    private int requestid;

    @TableField("lcbh")
    private String lcbh;

    @TableField("sqr")
    private int sqr;

    @TableField("sqrq")
    private String sqrq;

    @TableField("szbm")
    private int szbm;

    @TableField("szgs")
    private int szgs;

    @TableField("nf")
    private int nf;

    @TableField("ydh")
    private String ydh;

    @TableField("ydid")
    private String ydid;

    @TableField("bz")
    private String bz;

    @TableField("fj")
    private String fj;

    @TableField("ysddh1")
    private String ysddh1;

    @TableField("ysddh2")
    private String ysddh2;

    @TableField("thlx")
    private int thlx;

    @TableField("thsqje")
    private String thsqje;

    @TableField("thrkje")
    private String thrkje;

    @TableField("thsm")
    private String thsm;

    @TableField("thlxbj")
    private int thlxbj;

    @TableField("yxsdhwb")
    private String yxsdhwb;

    @TableField("kh")
    private String kh;

    @TableField("fhdc")
    private String fhdc;

    @TableField("shdc")
    private String shdc;

    @TableField("lx")
    private int lx;

    @TableField("djlx")
    private int djlx;

    @TableField("sfcd")
    private int sfcd;
}
