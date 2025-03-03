package com.me.modules.trf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_359")
public class TrfOutOrder {

    @TableField("order_sn")
    private String orderSn;

    @TableField("requestid")
    private String requestId;

    @TableField("is_send")
    private String isSend;

    @TableField("in_warehouse_id")
    private String inWarehouseId;

    @TableField("out_store_code")
    private String outStoreCode;
}
