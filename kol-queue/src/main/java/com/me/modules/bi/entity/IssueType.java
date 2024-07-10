package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "issue_type")
@ApiModel(description = "问题类型实体类")
public class IssueType implements Serializable {

    @TableField(value = "id")
    private String id;

    @TableField(value = "description")
    private String description;


}
