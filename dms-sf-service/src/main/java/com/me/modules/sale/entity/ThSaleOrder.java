package com.me.modules.sale.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_347")
public class ThSaleOrder {

    @TableField("erp_order")
    private String erpOrder;

    @TableField("requestid")
    private String requestId;

    @TableField("is_send")
    private String isSend;
}
