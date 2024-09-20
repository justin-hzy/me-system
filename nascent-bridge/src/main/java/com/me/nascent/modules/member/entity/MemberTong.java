package com.me.nascent.modules.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("member_tong")
public class MemberTong {

    @TableField("bindMixMobile")
    private String bindMixMobile;

    @TableField("bindMobile")
    private String bindMobile;

    @TableField("bindStatus")
    private Integer bindStatus;

    @TableField("bindTime")
    private Date bindTime;

    @TableField("extendObj")
    private String extendObj;

    @TableField("nascentID")
    private String nascentID;

    @TableField("nick")
    private String nick;

    @TableField("sellerNick")
    private String sellerNick;

    @TableField("unBindTime")
    private Date unBindTime;

    @TableField("ouid")
    private String ouid;

    @TableField("omid")
    private String omid;

    @TableField("nasOuid")
    private String nasOuid;

}
