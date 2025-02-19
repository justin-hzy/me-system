package com.me.modules.dms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_272")
public class DmsTwSaleOrder {

    @TableField("requestid")
    private Integer requestId;

    @TableField("is_tw_enough")
    private String isTwEnough;

    @TableField("is_hk_enough")
    private String isHkEnough;
}
