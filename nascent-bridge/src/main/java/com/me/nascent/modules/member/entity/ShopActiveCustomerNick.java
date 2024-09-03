package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("shop_active_customer_nick")
public class ShopActiveCustomerNick {

    @TableField("mainId")
    private Long mainId;

    @TableField("nick")
    private String nick;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private Integer platform;

}
