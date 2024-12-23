package com.me.modules.mabang.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_xsck")
public class MBOrder {

    @TableField("platform_order_id")
    private String platformOrderId;

    @TableField("currency_id")
    private String currencyId;

    @TableField("express_time")
    private String expressTime;

    @TableField("street")
    private String street;

    @TableField("shop_id")
    private String shopId;

    @TableField("order_status")
    private String orderStatus;

    @TableField("is_trans_k3")
    private String isTransK3;
}
