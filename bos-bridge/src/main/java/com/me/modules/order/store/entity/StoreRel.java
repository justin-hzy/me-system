package com.me.modules.order.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("STORE_RELATION")
public class StoreRel {

    @TableField("ABD_WAREHOUSE_NO")
    @ApiModelProperty("伯俊仓库编号")
    private String abdWareHouseNo;

    @TableField("ERP_WAREHOUSE_NO")
    @ApiModelProperty("伯俊仓库编号")
    private String erpWareHouseNo;
}
