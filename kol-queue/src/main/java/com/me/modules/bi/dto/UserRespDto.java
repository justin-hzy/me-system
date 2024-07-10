package com.me.modules.bi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRespDto {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("昵称")
    private String nick;
}
