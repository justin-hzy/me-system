package com.me.modules.order.sku.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("SKU_RELATION")
@Data
public class SkuRel {

    @TableField("ABROAD_SKU")
    private String abroadSku;

    @TableField("ERP_SKU")
    private String erpSku;

    @TableField("NAME")
    private String name;
}
