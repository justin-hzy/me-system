package com.me.nascent.modules.point.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("pure_member_point")
public class PureMemberPoint {

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private int platform;

    @TableField("integralAccount")
    private String integralAccount;

    @TableField("bindMobile")
    private String bindMobile;

    @TableField("memberCard")
    private String memberCard;

    @TableField("score")
    private BigDecimal score;
}
