package com.me.modules.bi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReissueReason {

    @TableField("id")
    @ApiModelProperty("id")
    private String id;

    @TableField("reason")
    @ApiModelProperty("补发原因")
    private String reason;

}
