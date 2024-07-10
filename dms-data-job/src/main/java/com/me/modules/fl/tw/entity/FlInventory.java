package com.me.modules.fl.tw.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("uf_flkccx")
@Data
public class FlInventory {
    @TableField("requestId")
    private int id;

    @TableField("requestId")
    private int requestId;

    @TableField("cb")
    private String cb;

    @TableField("cbmc")
    private String cbmc;

    @TableField("gjtm")
    private String gjtm;

    @TableField("pm")
    private String pm;

    @TableField("pp")
    private String pp;

    @TableField("xsfl")
    private String xsfl;

    @TableField("pc")
    private String pc;

    @TableField("xq")
    private String xq;

    @TableField("zyl")
    private String zyl;

    @TableField("ztsl")
    private String ztsl;

    @TableField("kys")
    private String kys;

    @TableField("zkc")
    private String zkc;

// Getters and Setters
}
