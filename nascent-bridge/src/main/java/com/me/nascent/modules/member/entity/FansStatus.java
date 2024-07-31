package com.me.nascent.modules.member.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
public class FansStatus {

    @TableField("mainId")
    private Integer mainId;

    @TableField("officialAccountName")
    private String officialAccountName;

    @TableField("subscribe")
    private Integer subscribe;
}
