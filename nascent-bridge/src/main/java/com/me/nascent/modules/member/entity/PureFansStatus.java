package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@TableName("pure_member_fans_status")
public class PureFansStatus {

    @TableField("mainId")
    private Long mainId;

    @TableField("officialAccountName")
    private String officialAccountName;

    @TableField("subscribe")
    private Integer subscribe;
}
