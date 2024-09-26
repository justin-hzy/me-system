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

    @TableField("availPoint")
    private BigDecimal availPoint;

    @TableField("integralAccount")
    private String integralAccount;
}
