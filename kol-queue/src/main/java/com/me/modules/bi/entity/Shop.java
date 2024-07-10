package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName(value = "shop")
@ApiModel(description = "店铺实体类")
public class Shop {
    @TableField(value = "id")
    private String id;

    @TableField(value = "shop_name")
    private String shopName;
}
