package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("formtable_main_147_dt1")
public class FormTableMain147Dt1 {

    @TableField("lx")
    @ApiModelProperty("类型")
    private String lx;

    @TableField("syfllx")
    @ApiModelProperty("使用返利类型")
    private String syfllx;

    @TableField("hptxm")
    @ApiModelProperty("货品条形码")
    private String hptxm;

    @TableField("hpmc")
    @ApiModelProperty("货品名称")
    private String hpmc;

    @TableField("rq")
    @ApiModelProperty("日期")
    private String rq;

    @TableField("dw")
    @ApiModelProperty("单位")
    private String dw;

    @TableField("gg")
    @ApiModelProperty("规格")
    private String gg;

    @TableField("hpsl")
    @ApiModelProperty("货品数量")
    private String hpsl;

    @TableField("dpzfje")
    @ApiModelProperty("单位支付金额")
    private String dpzfje;

    @TableField("hjjey")
    @ApiModelProperty("合计金额(元)")
    private String hjjey;

    @TableField("ydzhpsl")
    @ApiModelProperty("已对账数量")
    private String ydzhpsl;

    @TableField("ddzsl")
    @ApiModelProperty("待对账数量")
    private String ddzsl;

    @TableField("dzsl")
    @ApiModelProperty("对账数量")
    private String dzsl;

    @TableField("sydzsl")
    @ApiModelProperty("剩余待对账数量")
    private String sydzsl;

    @TableField("hpzfje")
    @ApiModelProperty("货品支持金额")
    private String hpzfje;

    @TableField("tfhjlid")
    @ApiModelProperty("退发货记录ID")
    private String tfhjlid;
}
