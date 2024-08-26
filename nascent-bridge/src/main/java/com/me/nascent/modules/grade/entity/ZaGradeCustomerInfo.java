package com.me.nascent.modules.grade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("za_grade_customer_info")
@Data
public class ZaGradeCustomerInfo {

    @TableField("mainId")
    private long mainId;

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("platform")
    private Integer platform;

    @TableField("grade")
    private Integer grade;

    @TableField("gradeName")
    private String gradeName;

    @TableField("updateTime")
    private Date updateTime;
}
