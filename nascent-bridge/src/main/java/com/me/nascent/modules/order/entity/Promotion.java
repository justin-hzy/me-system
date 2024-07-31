package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("nascent_promotion")
public class Promotion {

    @TableField("id")
    private String id;

    @TableField("promotionToolId")
    private String promotionToolId;

    @TableField("promotionDetailId")
    private String promotionDetailId;

    @TableField("sysTradeId")
    private Long sysTradeId;

    @TableField("outTradeId")
    private String outTradeId;

    @TableField("shopId")
    private Long shopId;

    @TableField("outOrderId")
    private String outOrderId;

    @TableField("discountFee")
    private String discountFee;

}
