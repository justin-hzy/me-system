package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("shop_active_customer_wechat")
public class ShopActiveCustomerWeChat {

    @TableField("mainId")
    private Long mainId;

    @TableField("wxAccountId")
    private String wxAccountId;

    @TableField("wxType")
    private Integer wxType;

    @TableField("wxOpenId")
    private String wxOpenId;
}
