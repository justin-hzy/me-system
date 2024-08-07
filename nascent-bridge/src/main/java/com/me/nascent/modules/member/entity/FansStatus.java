package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@TableName("member_fans_status")
public class FansStatus {

    @TableField("mainId")
    private Long mainId;

    @TableField("officialAccountName")
    private String officialAccountName;

    @TableField("subscribe")
    private Integer subscribe;
}
