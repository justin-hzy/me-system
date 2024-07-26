package com.me.modules.fl.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("uf_fl_transcode_order_request")
public class FlTransCodeReqLog {

    @TableField("requestId")
    private String requestId;

    @TableField("apiId")
    private String apiId;

    @TableField("params")
    private String params;

    @TableField("message")
    private String message;

    @TableField("error")
    private String error;

    @TableField("status")
    private String status;

    @TableField("fail_count")
    private Integer failCount;

    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("is_delete")
    private String isDelete;

}
