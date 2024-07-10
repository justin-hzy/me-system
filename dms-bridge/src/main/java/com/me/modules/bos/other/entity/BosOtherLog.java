package com.me.modules.bos.other.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("uf_bos_other_log")
@Data
public class BosOtherLog {

    @TableField("requestId")
    private String requestId;

    @TableField("param")
    private String param;

    @TableField("status")
    private String status;

    @TableField("updateTime")
    private String updateTime;

    @TableField("createTime")
    private String createTime;

    @TableField("is_delete")
    private String is_delete;
}
