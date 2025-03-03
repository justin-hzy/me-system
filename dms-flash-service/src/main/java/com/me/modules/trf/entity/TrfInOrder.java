package com.me.modules.trf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_359")
public class TrfInOrder {

    @TableField("order_sn")
    private String orderSn;

    @TableField("requestid")
    private String requestId;

    @TableField("is_receive")
    private String isReceive;

    @TableField("in_warehouse_id")
    private String inWarehouseId;

    @TableField("in_store_code")
    private String inStoreCode;

}
