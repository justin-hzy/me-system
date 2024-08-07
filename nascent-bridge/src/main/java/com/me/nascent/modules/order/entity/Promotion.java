package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trade_promotion")
public class Promotion {

    @TableField("mainid")
    private Long mainid;

    @TableField("activityType")
    private Integer activityType;

    @TableField("couponType")
    private Integer couponType;

    @TableField("discountFee")
    private String discountFee;

    @TableField("giftItemId")
    private String giftItemId;

    @TableField("giftItemName")
    private String giftItemName;

    @TableField("giftItemNum")
    private String giftItemNum;

    @TableField("outOrderId")
    private String outOrderId;

    @TableField("outTradeId")
    private String outTradeId;

    @TableField("promotionActivityId")
    private String promotionActivityId;

    @TableField("promotionDesc")
    private String promotionDesc;

    @TableField("promotionDetailId")
    private String promotionDetailId;

    @TableField("promotionName")
    private String promotionName;

    @TableField("promotionToolId")
    private String promotionToolId;

    @TableField("promotionType")
    private Integer promotionType;

    @TableField("shopId")
    private Long shopId;

}
