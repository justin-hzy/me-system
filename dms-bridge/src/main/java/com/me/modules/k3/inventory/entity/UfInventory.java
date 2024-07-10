package com.me.modules.k3.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_inventory")
public class UfInventory {

    @TableField("ckbm")
    private String ckbm;

    @TableField("wlbm")
    private String wlbm;

    @TableField("cksl")
    private Integer cksl;

    @TableField("qmkcsl")
    private Integer qmkcsl;

}
