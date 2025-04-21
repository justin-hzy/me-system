package com.me.modules.dms.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("uf_kh")
public class Client {

    @TableField("khmcst")
    @ApiModelProperty("名称")
    private String mc;

}
