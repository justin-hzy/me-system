package com.me.modules.order.shop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("SHOP_RELATION")
public class ShopRel {

    @TableField("ABD_SHOPNAME")
    private String abdShopName;

    @TableField("ERP_SHOPNAME")
    private String erpShopNAME;
}
