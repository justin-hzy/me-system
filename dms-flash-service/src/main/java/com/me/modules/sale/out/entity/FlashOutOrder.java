package com.me.modules.sale.out.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/*
* flash 出库单
* */
@Data
@TableName("formtable_main_345")
public class FlashOutOrder {

    @TableField("order_sn")
    private String orderSn;

    @TableField("requestid")
    private String requestId;

    @TableField("is_send")
    private String isSend;
}
