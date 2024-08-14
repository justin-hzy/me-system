package com.me.nascent.modules.grade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class GradeCustomerCardReceiveInfo {


    @TableField("nasOuid")
    private String nasOuid;

    @TableField("viewId")
    private Long viewId;

    @TableField("memberCard")
    private String memberCard;

    @TableField("cardReceivePlatform")
    private Integer cardReceivePlatform;

    @TableField("cardType")
    private Integer cardType;

    @TableField("cardReceiveTime")
    private Date cardReceiveTime;

    @TableField("cardReceiveStatus")
    private Integer cardReceiveStatus;

    @TableField("sourceType")
    private String sourceType;
}
