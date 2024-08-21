package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("pure_member_nick_info")
public class PureMemberNickInfo {

    @TableId("mainId")
    private Long mainId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private Integer platform;

    @TableField("isTransGrade")
    private String isTransGrade;
}
