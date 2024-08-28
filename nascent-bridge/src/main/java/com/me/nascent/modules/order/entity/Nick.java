package com.me.nascent.modules.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("trade_nick")
public class Nick {

    @TableId("mainid")
    private Long mainid;

    @TableField("nick")
    private String nick;

    @TableField("platform")
    private Integer platform;
}
