package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class CardReceiveInfo {

    @TableField("mainId")
    private Integer mainId;

    @TableField("cardReceivePlatform")
    private Integer cardReceivePlatform;

    @TableField("cardReceiveStatus")
    private Integer cardReceiveStatus;

    @TableField("cardReceiveTime")
    private String cardReceiveTime;

    @TableField("cardType")
    private Integer cardType;

    @TableField("memberCard")
    private String memberCard;

    @TableField("sourceType")
    private String sourceType;

    @TableField("viewId")
    private Integer viewId;
}
