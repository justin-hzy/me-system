package com.me.modules.lock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("dms_tw_lock")
@Data
public class DmsTwLock {

    @TableField("request_id")
    private Integer requestId;

    @TableField("process_code")
    private String processCode;

    @TableField("lock_type")
    private String lockType;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

    @TableField("is_lock")
    private String isLock;
}
