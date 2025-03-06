package com.me.modules.nascent.point.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@TableName("point_sum")
@Data
public class PointSum {

    @TableField("nas_ouid")
    private String nasouid;

    @TableField("point")
    private BigDecimal point;
}
