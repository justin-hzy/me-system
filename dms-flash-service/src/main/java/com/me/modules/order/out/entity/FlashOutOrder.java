package com.me.modules.order.out.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
/*
* flash 出库单
* */
@Data
@TableName("formtable_main_351")
public class FlashOutOrder {

    @TableField("order_sn")
    private String orderSn;

    @TableField("requestid")
    private String requestId;

    @TableField("is_send")
    private String isSend;

    @TableField("store_code")
    private String storeCode;
}
