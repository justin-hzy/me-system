package com.me.modules.http.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("flash_resp_code")
public class FlashRespCode {

    @TableField("code")
    private String code;

    @TableField("meaning")
    private String meaning;

}
