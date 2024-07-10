package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
@TableName("formtable_main_234")
public class FlPurInFormTableMain {

    @TableField("id")
    private String id;

    @TableField("requestId")
    private String requestId;

    @TableField("lcbh")
    private String lcbh;

    @TableField("sqr")
    private String sqr;

    @TableField("sqrq")
    private String sqrq;

    @TableField("ssbm")
    private String ssbm;

    @TableField("gw")
    private String gw;

    @TableField("lcfzr")
    private String lcfzr;

    @TableField("rkck")
    private String rkck;

    @TableField("bb")
    private String bb;

    @TableField("gys")
    private String gys;

    @TableField("rkrq")
    private String rkrq;

    @TableField("cglx")
    private String cglx;

    @TableField("bz")
    private String bz;

    @TableField("rkdh")
    private String rkdh;

    @TableField("djlx")
    private String djlx;

    @TableField("yjjcr")
    private String yjjcr;
}
