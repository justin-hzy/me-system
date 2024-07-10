package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("allergic_symptoms")
public class AllergicSymptoms {

    @TableField("id")
    @ApiModelProperty("id")
    private String id;

    @TableField("allergic_symptoms")
    @ApiModelProperty("过敏症状")
    private String allergicSymptoms;
}
