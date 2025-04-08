package com.me.modules.bos.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("C_VIP")
@Data
public class BosMember {

    @TableField("CARDNO")
    private String cardNo;

    @TableField("CODE")
    private String code;

    @TableField("NASOUID")
    private String nasouId;

}
