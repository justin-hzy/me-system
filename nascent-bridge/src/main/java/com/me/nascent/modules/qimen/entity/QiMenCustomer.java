package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_customer")
public class QiMenCustomer {

    @TableField("firstBindCardTime")
    private String firstBindCardTime;

    @TableField("lastedUnbindCardTime")
    private String lastedUnbindCardTime;

    @TableField("level")
    private String level;

    @TableField("mixMobile")
    private String mixMobile;

    @TableField("omid")
    private String omid;

    @TableField("ouid")
    private String ouid;

    @TableField("point")
    private String point;

    @TableField("sellerNick")
    private String sellerNick;

    @TableField("nasOuid")
    private String nasOuid;
}