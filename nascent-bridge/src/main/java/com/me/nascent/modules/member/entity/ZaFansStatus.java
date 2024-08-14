package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("za_member_fans_status")
public class ZaFansStatus {

    @TableField("mainId")
    private Long mainId;

    @TableField("officialAccountName")
    private String officialAccountName;

    @TableField("subscribe")
    private Integer subscribe;
}
