package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("nascent_card_receive_info")
public class CardReceiveInfo {

    @TableField("mainId")
    private Long mainId;

    @TableField("cardReceivePlatform")
    private Integer cardReceivePlatform;

    @TableField("cardReceiveStatus")
    private Integer cardReceiveStatus;

    @TableField("cardReceiveTime")
    private Date cardReceiveTime;

    @TableField("cardType")
    private Integer cardType;

    @TableField("memberCard")
    private String memberCard;

    @TableField("sourceType")
    private String sourceType;

    @TableField("viewId")
    private Long viewId;
}
