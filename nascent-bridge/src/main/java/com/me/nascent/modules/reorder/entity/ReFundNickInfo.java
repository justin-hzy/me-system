package com.me.nascent.modules.reorder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("refund_nick_info")
public class ReFundNickInfo {

    @TableField("mainid")
    private Long mainid;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private String platform;

}
