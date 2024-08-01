package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("nascent_sg_finish_info")
public class SgFinishInfo {

    @TableField("orderFinishGuideId")
    private Integer orderFinishGuideId;

    @TableField("orderFinishShopId")
    private Long orderFinishShopId;

    @TableField("salesRatio")
    private BigDecimal salesRatio;
}
