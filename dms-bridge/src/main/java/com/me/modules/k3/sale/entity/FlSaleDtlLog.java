package com.me.modules.k3.sale.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("flsaledtllog")
public class FlSaleDtlLog {

    @TableField("billNo")
    private String billNo;

    @TableField("sku")
    private String sku;

    @TableField("quantity")
    private String quantity;

    @TableField("sellorgid")
    private String sellorgid;

    @TableField("stockid")
    private String stockid;

    @TableField("createTime")
    private String createTime;
}
