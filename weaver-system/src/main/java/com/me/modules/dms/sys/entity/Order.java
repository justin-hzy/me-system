package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("formtable_main_272")
public class Order {

    @TableField("lcbh")
    private String lcbh;
}
