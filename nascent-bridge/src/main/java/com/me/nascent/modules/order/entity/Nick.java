package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("nascent_nick")
public class Nick {

    @TableField("mainid")
    private Long mainid;

    @TableField("nick")
    private String nick;

    @TableField("platform")
    private Integer platform;
}
