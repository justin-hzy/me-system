package com.me.modules.refund.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_348")
public class ThRefund {

    @TableField("erp_order")
    private String erpOrder;

    @TableField("requestid")
    private String requestId;

    @TableField("is_receive")
    private String isReceive;
}
