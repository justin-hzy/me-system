package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("za_member_nick_info")
public class ZaMemberNickInfo {

    @TableField("mainId")
    private Long mainId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private Integer platform;
}
