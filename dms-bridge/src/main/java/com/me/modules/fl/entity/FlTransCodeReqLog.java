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

    @TableField("son_params")
    private String sonParams;

    @TableField("fr_params")
    private String frParams;

    @TableField("son_ret_params")
    private String sonRetParams;

    @TableField("fr_ret_params")
    private String frRetParams;

    @TableField("fr_message")
    private String frMessage;

    @TableField("son_message")
    private String sonMessage;

    @TableField("fr_error")
    private String frerror;

    @TableField("son_error")
    private String sonError;

    @TableField("fr_status")
    private String frStatus;

    @TableField("son_status")
    private String sonStatus;

    @TableField("fr_fail_count")
    private Integer frFailCount;

    @TableField("son_fail_count")
    private Integer sonFailCount;

    @TableField("createTime")
    private String createTime;

    @TableField("updateTime")
    private String updateTime;

    @TableField("is_delete")
    private String isDelete;

}
