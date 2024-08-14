package com.me.nascent.modules.grade.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("grade_fans_status_vos")
public class GradeFansStatusVo {

    @TableField("nasOuid")
    private String nasOuid;

    @TableField("grade")
    private String grade;

    @TableField("gradeName")
    private String gradeName;
}
