package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("")
public class ShopActiveCustomerCard {

    @TableField("mainId")
    private Long mainId;

    @TableField("cardType")
    private Integer cardType;

    @TableField("cardReceivePlatform")
    private Integer cardReceivePlatform;

    @TableField("memberCard")
    private String memberCard;

    @TableField("cardReceiveTime")
    private Date cardReceiveTime;
}
