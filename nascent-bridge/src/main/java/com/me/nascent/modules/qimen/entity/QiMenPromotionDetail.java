package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_promotion_detail")
public class QiMenPromotionDetail {

    @TableField("promotionName")
    private String promotionName; // 优惠信息的名称

    @TableField("giftItemId")
    private String giftItemId; // 赠品的宝贝id

    @TableField("giftItemName")
    private String giftItemName; // 赠品名称

    @TableField("promotionId")
    private String promotionId; // 优惠id

    @TableField("id")
    private String id; // 交易的主订单或子订单号

    @TableField("tid")
    private String tid; // 交易的主订单或子订单号

    @TableField("giftItemNum")
    private String giftItemNum; // 礼物数量

    @TableField("promotionDesc")
    private String promotionDesc; // 优惠活动的描述

    @TableField("discountFee")
    private String discountFee; // 优惠金额
}
