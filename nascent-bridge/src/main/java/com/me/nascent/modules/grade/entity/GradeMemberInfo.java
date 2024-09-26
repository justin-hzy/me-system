package com.me.nascent.modules.grade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("grade_member_info")
@Data
public class GradeMemberInfo {

    @TableField("customerId")
    private Long customerId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private int platform;

    @TableField("grade")
    private int grade;

    @TableField("shopId")
    private Long shopId;

    @TableField("gradeName")
    private String gradeName;

    @TableField("updateTime")
    private Date updateTime;
}
