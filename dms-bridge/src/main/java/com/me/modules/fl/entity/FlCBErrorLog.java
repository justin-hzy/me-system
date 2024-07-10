package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_fl_callback_error_log")
public class FlCBErrorLog {

    @TableField("requestId")
    private String requestId;

    @TableField("params")
    private String params;

    @TableField("message")
    private String message;

    @TableField("error")
    private String error;

    /*状态 1:成功 2:失败*/
    @TableField("status")
    private String status;

    /*类型 1:订单回传 2:退单回传*/
    @TableField("type")
    private String type;

    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("is_delete")
    private String is_delete;
}
