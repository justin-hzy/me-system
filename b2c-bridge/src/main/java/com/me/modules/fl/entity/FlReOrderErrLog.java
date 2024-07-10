package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("fl_re_order_error_log")
public class FlReOrderErrLog {

    @TableField("token")
    private String token;

    @TableField("param")
    private String param;

    /*0 未干预 1 已干预成功 2 干预失败*/
    @TableField("status")
    private String status;

    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("isDelete")
    private String isDelete;
}
