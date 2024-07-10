package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value = "byte_new_user")
public class ByteNewUser {

    @ApiModelProperty("id")
    @TableField("id")
    private String id;

    @ApiModelProperty("成员昵称")
    @TableField("nick")
    private String nick;
}
