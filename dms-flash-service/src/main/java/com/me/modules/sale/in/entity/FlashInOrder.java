package com.me.modules.sale.in.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_350")
public class FlashInOrder {

    @TableField("order_sn")
    private String orderSn;

    @TableField("requestid")
    private String requestId;

    @TableField("is_receive")
    private String isReceive;
}
