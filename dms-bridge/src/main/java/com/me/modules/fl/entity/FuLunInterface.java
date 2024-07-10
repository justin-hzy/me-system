package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_fljkpz")
public class FuLunInterface {

    @TableField("id")
    private String id;

    @TableField("fjk")
    private String fjk;

}
