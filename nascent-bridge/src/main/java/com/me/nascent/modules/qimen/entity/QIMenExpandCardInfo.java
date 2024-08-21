package com.me.nascent.modules.qimen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("qi_men_expand_card_info")
public class QIMenExpandCardInfo {

    @TableField("tid")
    private String tid;

    @TableField("basicPriceUsed")
    private String basicPriceUsed; // 用卡订单所用的本金

    @TableField("expandPrice")
    private String expandPrice; // 买卡订单权益金

    @TableField("basicPrice")
    private String basicPrice; // 买卡订单本金

    @TableField("expandPriceUsed")
    private String expandPriceUsed; // 用卡订单所用的权益金
}
