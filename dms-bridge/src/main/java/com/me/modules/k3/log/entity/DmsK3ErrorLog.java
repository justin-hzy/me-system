package com.me.modules.k3.log.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("dms_k3_error_log")
@Data
public class DmsK3ErrorLog {

    @TableField("billNo")
    private String billNo;

    @TableField("message")
    private String message;

    @TableField("createTime")
    private String createTime;

    @TableField("time")
    private String time;

    @TableField("date")
    private String date;
}
