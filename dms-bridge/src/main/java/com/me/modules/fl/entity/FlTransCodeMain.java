package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_244")
public class FlTransCodeMain {
    
    @TableField("id")
    private int id;

    @TableField("requestid")
    private String requestid;

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

    @TableField("sm")
    private String sm;

    @TableField("scfj")
    private String scfj;

    @TableField("lcfzr")
    private int lcfzr;

    @TableField("yjrksj")
    private String yjrksj;
}
