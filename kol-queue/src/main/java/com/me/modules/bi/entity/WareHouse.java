package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName(value = "warehouse")
@ApiModel(description = "仓库体类")
public class WareHouse {

    @TableField(value = "id")
    private String id;

    @TableField(value = "warehouse_name")
    private String wareHouseName;
}
