package com.me.modules.mabang.refund.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_xsth")
public class MBRefund {

    @TableField("platform_order_id")
    private String platformOrderId;

    @TableField("currency_id")
    private String currencyId;

    @TableField("refund_time")
    private String refundTime;

    @TableField("shop_id")
    private String shopId;

    @TableField("status")
    private String status;

    @TableField("is_trans_k3")
    private String isTransK3;

}
