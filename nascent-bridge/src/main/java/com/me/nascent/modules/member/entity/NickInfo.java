package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class NickInfo {

    @TableField("mainId")
    private Integer mainId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private String platform;
}
